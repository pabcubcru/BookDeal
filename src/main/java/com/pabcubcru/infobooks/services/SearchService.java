package com.pabcubcru.infobooks.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.pabcubcru.infobooks.models.Book;
import com.pabcubcru.infobooks.models.ProvinceEnum;
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
import org.springframework.data.domain.Page;
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

    @Transactional
    public List<String> findTitlesOfBooks(String query) {
        NativeSearchQueryBuilder searchQuery = new NativeSearchQueryBuilder();
        searchQuery.withQuery(QueryBuilders.multiMatchQuery(query)
            .field("title")
            .field("description")
            .field("originalTitle")
            .field("isbn")
            .field("publisher")
            .field("genres")
            .field("author")
            .field("status")
            .field("username")
            .operator(Operator.OR)
            .type(MultiMatchQueryBuilder.Type.BEST_FIELDS)
            .fuzziness(Fuzziness.ONE));

        searchQuery.withPageable(PageRequest.of(0, 10));
        NativeSearchQuery queryPrincipal = searchQuery.build();
        SearchHits<Book> hits = elasticsearchTemplate.search(queryPrincipal, Book.class, IndexCoordinates.of("books"));
        List<Book> books = hits.stream().map(x -> x.getContent()).collect(Collectors.toList());

        return books.stream().map(x -> x.getTitle()).collect(Collectors.toList());   
    }

    @Transactional(readOnly = true)
    public Map<Integer, List<Book>> searchBook(String query, Pageable pageable, String username) {
        Map<Integer, List<Book>> map = new HashMap<>();

        NativeSearchQueryBuilder searchQuery = new NativeSearchQueryBuilder();

        List<Book> books = new ArrayList<>();
        Boolean isSearchToBook = true;
        Page<Book> pageBooks = null;

        String regexPostCode = "0[1-9][0-9]{3}|[1-4][0-9]{4}|5[0-2][0-9]{3}";

        if(this.isNumeric(query) && !query.matches(regexPostCode)) {
            searchQuery.withQuery(QueryBuilders.multiMatchQuery(Integer.parseInt(query))
            .field("price")
            .field("publicationYear")
            .operator(Operator.OR)
            .type(MultiMatchQueryBuilder.Type.BEST_FIELDS));
        } else if(query.contains("-") && this.isCorrectRange(query)) {
            String[] splitQuery = query.split("-");
            searchQuery.withQuery(QueryBuilders.matchAllQuery())
            .withFilter(QueryBuilders.rangeQuery("publicationYear").gte(Integer.parseInt(splitQuery[0])).lte(Integer.parseInt(splitQuery[1])))
            .withSort(SortBuilders.fieldSort("publicationYear").order(SortOrder.ASC));
        } else if(query.matches(regexPostCode)) {
            List<String> usernames = this.userRepository.findByPostCode(query).stream().map(x -> x.getUsername()).collect(Collectors.toList());
            pageBooks = this.bookRepository.findByUsernameIn(usernames, pageable);
            books = pageBooks.getContent();
            isSearchToBook = false;
        } else if(Arrays.asList(ProvinceEnum.values()).stream().anyMatch(x -> x.toString().equals(query))) {
            List<String> usernames = this.userRepository.findByProvinceLike(query).stream().map(x -> x.getUsername()).collect(Collectors.toList());
            pageBooks = this.bookRepository.findByUsernameIn(usernames, pageable);
            books = pageBooks.getContent();
            isSearchToBook = false;
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
            .field("username")
            .operator(Operator.OR)
            .type(MultiMatchQueryBuilder.Type.BEST_FIELDS)
            .fuzziness(Fuzziness.ONE)
            .prefixLength(2));
        }

        searchQuery.withPageable(pageable);
        NativeSearchQuery queryPrincipal = searchQuery.build();
        SearchHits<Book> hits = elasticsearchTemplate.search(queryPrincipal, Book.class, IndexCoordinates.of("books"));

        if(isSearchToBook) {
            books = hits.stream().map(x -> x.getContent()).collect(Collectors.toList());
        }

        List<Book> res = new ArrayList<>();
        Integer numPages = 0;

        if(books.size() > 0) {
            res = books;
            Integer totalBooks = isSearchToBook ? (int) hits.getTotalHits() : (int) pageBooks.getNumberOfElements();

            numPages = (int) Math.ceil(totalBooks / pageable.getPageSize())+1;
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
