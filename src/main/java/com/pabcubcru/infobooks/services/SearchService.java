package com.pabcubcru.infobooks.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.pabcubcru.infobooks.models.Book;
import com.pabcubcru.infobooks.models.User;
import com.pabcubcru.infobooks.models.UserFavouriteBook;
import com.pabcubcru.infobooks.repository.BookRepository;
import com.pabcubcru.infobooks.repository.UserFavouriteBookRepository;
import com.pabcubcru.infobooks.repository.UserRepository;

import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.common.lucene.search.function.FunctionScoreQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SearchService {

    @Autowired
    private ElasticsearchRestTemplate elasticsearchTemplate;

    private BookRepository bookRepository;

    private UserFavouriteBookRepository userFavouriteBookRepository;

    private UserRepository userRepository;

    @Autowired
    public SearchService(ElasticsearchRestTemplate elasticsearchTemplate, BookRepository bookRepository, UserFavouriteBookRepository userFavouriteBookRepository,
    UserRepository userRepository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.bookRepository = bookRepository;
        this.userFavouriteBookRepository = userFavouriteBookRepository;
        this.userRepository = userRepository;
    }

    private Boolean isNumeric(String query) {
        try {
            Double.parseDouble(query);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private Boolean isCorrectRange(String query) {
        Boolean res = true;
        String[] splitQuery = query.split("-");
        if(splitQuery.length == 2) {
            for(String s : splitQuery) {
                if(!this.isNumeric(s)) {
                    res = false;
                    break;
                }
            }
        } else {
            res = false;
        }

        return res;
    }

    @Transactional(readOnly = true)
    public Map<Integer, List<Book>> searchBook(String query, Pageable pageable, String username) {
        Map<Integer, List<Book>> map = new HashMap<>();

        NativeSearchQueryBuilder searchQuery = new NativeSearchQueryBuilder();

        if(this.isNumeric(query)) {
            searchQuery.withQuery(QueryBuilders.multiMatchQuery(Double.parseDouble(query))
            .field("price")
            .field("publicationYear")
            .operator(Operator.OR)
            .type(MultiMatchQueryBuilder.Type.BEST_FIELDS));
        } else if(query.contains("-") && this.isCorrectRange(query)) {
            String[] splitQuery = query.split("-");
            searchQuery.withQuery(QueryBuilders.matchAllQuery())
            .withFilter(QueryBuilders.rangeQuery("publicationYear").gte(Integer.parseInt(splitQuery[0])).lte(splitQuery[1]))
            .withSort(SortBuilders.fieldSort("publicationYear").order(SortOrder.ASC));
        } else {
            searchQuery.withQuery(QueryBuilders.multiMatchQuery(query)
            .field("title")
            .field("description")
            .field("originalTitle")
            .field("isbn")
            .field("publisher")
            .field("genres")
            .field("author")
            .field("status")
            .operator(Operator.OR)
            .type(MultiMatchQueryBuilder.Type.BEST_FIELDS)
            .fuzziness(Fuzziness.ONE)
            .prefixLength(2));
        }

        searchQuery.withPageable(pageable);
        NativeSearchQuery queryPrincipal = searchQuery.build();

        SearchHits<Book> books = elasticsearchTemplate.search(queryPrincipal, Book.class, IndexCoordinates.of("books"));
        List<Book> res = new ArrayList<>();
        Integer numPages = 0;

        if(books.getTotalHits() > 0) {
            res = books.stream().map(x -> x.getContent()).collect(Collectors.toList());

            numPages = (int) Math.ceil(books.getTotalHits() / pageable.getPageSize())+1;
        } else if(username != null) {
            User user = this.userRepository.findByUsername(username);
            res = this.recommendBooks(user, PageRequest.of(0, 21)).values().stream().findFirst().orElse(new ArrayList<>());
            if(res.isEmpty()) {
                res = this.bookRepository.findByUsernameNot(user.getUsername(), PageRequest.of(0, 21)).getContent();
            }
        } else {
            res = this.bookRepository.findAll(PageRequest.of(0, 21)).getContent();
        }      

        map.put(numPages, res);
        return map;
    }

    @Transactional(readOnly = true)
    public Map<Integer, List<Book>> recommendBooks(User user, Pageable pageable) {
        Map<Integer, List<Book>> res = new HashMap<>();

        if(pageable.getPageNumber() < 5) {
            List<Book> booksOfUser = this.bookRepository.findByUsername(user.getUsername());
            List<UserFavouriteBook> favouritesBooksOfUser = this.userFavouriteBookRepository.findByUsername(user.getUsername(), Pageable.unpaged()).getContent();

            List<Book> favouritesBooks = favouritesBooksOfUser.stream().map(x -> this.bookRepository.findById(x.getBookId()).get()).collect(Collectors.toList());
            booksOfUser.addAll(favouritesBooks);

            List<FunctionScoreQueryBuilder.FilterFunctionBuilder> filterFunctionBuilders = new ArrayList<>();
            
            if(!booksOfUser.isEmpty()) {
                for(Book b : booksOfUser) {
                    filterFunctionBuilders.add(new FunctionScoreQueryBuilder.FilterFunctionBuilder(QueryBuilders.matchQuery("publisher", b.getPublisher()),
                        ScoreFunctionBuilders.weightFactorFunction(12)));
                    filterFunctionBuilders.add(new FunctionScoreQueryBuilder.FilterFunctionBuilder(QueryBuilders.matchQuery("author", b.getAuthor()),
                        ScoreFunctionBuilders.weightFactorFunction(10)));
                    for(String genre : b.getGenres().split(",")) {
                        filterFunctionBuilders.add(new FunctionScoreQueryBuilder.FilterFunctionBuilder(QueryBuilders.matchQuery("genres", genre),
                            ScoreFunctionBuilders.weightFactorFunction(10)));
                    }
                }
            }

            filterFunctionBuilders.add(new FunctionScoreQueryBuilder.FilterFunctionBuilder(QueryBuilders.matchQuery("genres", user.getGenres()),
                ScoreFunctionBuilders.weightFactorFunction(8)));

            FunctionScoreQueryBuilder.FilterFunctionBuilder[] builders = new FunctionScoreQueryBuilder.FilterFunctionBuilder[filterFunctionBuilders.size()];
            filterFunctionBuilders.toArray(builders);
            FunctionScoreQueryBuilder functionScoreQueryBuilder = QueryBuilders.functionScoreQuery(builders)
                    .scoreMode(FunctionScoreQuery.ScoreMode.MAX)
                    .setMinScore(8);

            BoolQueryBuilder queryFilter = new BoolQueryBuilder();
            
            for(Book book : booksOfUser) {
                BoolQueryBuilder boolQueryBuilder1 = new BoolQueryBuilder();
                boolQueryBuilder1.mustNot(QueryBuilders.matchQuery("id",book.getId()));
                queryFilter.must(boolQueryBuilder1);
            }
            BoolQueryBuilder boolQueryBuilder2 = new BoolQueryBuilder();
            boolQueryBuilder2.mustNot(QueryBuilders.matchQuery("username",user.getUsername()));

            queryFilter.must(boolQueryBuilder2);
            
            NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
            builder.withQuery(functionScoreQueryBuilder);
            builder.withFilter(queryFilter);
            builder.withPageable(pageable);
            NativeSearchQuery searchQuery = builder.build();

            SearchHits<Book> searchHits = elasticsearchTemplate.search(searchQuery, Book.class);

            if(searchHits.getTotalHits()<=0){
                List<Book> bk = this.bookRepository.findByGenresLike(user.getGenres(), PageRequest.of(0, 21)).getContent();
                if(bk.isEmpty()) {
                    bk = this.bookRepository.findByUsernameNot(user.getUsername(), PageRequest.of(0, 21)).getContent();
                }
                res.put(1, bk);
                return res;
            }

            List<Book> searchBookList = searchHits.stream().map(x -> x.getContent()).collect(Collectors.toList());
            Integer numPages = 5;

            if((int) Math.ceil(searchHits.getTotalHits() / pageable.getPageSize()) < 5) {
                numPages = (int) Math.ceil(searchHits.getTotalHits() / pageable.getPageSize());
            }

            res.put(numPages, searchBookList);
        } else {
            res.put(5, new ArrayList<>());
        }

        return res;

    }
    
}
