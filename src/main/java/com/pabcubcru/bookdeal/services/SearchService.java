package com.pabcubcru.bookdeal.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.pabcubcru.bookdeal.models.Book;
import com.pabcubcru.bookdeal.models.GenreEnum;
import com.pabcubcru.bookdeal.models.Search;
import com.pabcubcru.bookdeal.models.User;
import com.pabcubcru.bookdeal.models.UserFavouriteBook;
import com.pabcubcru.bookdeal.repository.BookRepository;
import com.pabcubcru.bookdeal.repository.SearchRepository;
import com.pabcubcru.bookdeal.repository.UserFavouriteBookRepository;
import com.pabcubcru.bookdeal.repository.UserRepository;

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

    private SearchRepository searchRepository;

    @Autowired
    public SearchService(ElasticsearchRestTemplate elasticsearchTemplate, BookRepository bookRepository,
            UserFavouriteBookRepository userFavouriteBookRepository, UserRepository userRepository,
            SearchRepository searchRepository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.bookRepository = bookRepository;
        this.userFavouriteBookRepository = userFavouriteBookRepository;
        this.userRepository = userRepository;
        this.searchRepository = searchRepository;
    }

    @Transactional
    public List<String> findTitlesOfBooks(String query) {
        NativeSearchQueryBuilder searchQuery = new NativeSearchQueryBuilder();
        searchQuery.withQuery(QueryBuilders.multiMatchQuery(query).field("title").field("originalTitle").field("isbn")
                .field("publisher").field("genres").field("author").field("status").field("username")
                .operator(Operator.OR).type(MultiMatchQueryBuilder.Type.BEST_FIELDS).fuzziness(Fuzziness.TWO)
                .prefixLength(1));

        searchQuery.withPageable(PageRequest.of(0, 20));
        NativeSearchQuery queryPrincipal = searchQuery.build();
        SearchHits<Book> hits = elasticsearchTemplate.search(queryPrincipal, Book.class, IndexCoordinates.of("books"));
        List<Book> books = hits.stream().map(x -> x.getContent()).collect(Collectors.toList());

        Set<String> res = new HashSet<>();
        Boolean a = null;
        String q = query.toLowerCase();
        for (Book b : books) {
            a = b.getTitle().toLowerCase().contains(q) ? res.add(b.getTitle() + "//" + "Título") : null;
            a = b.getAuthor().toLowerCase().contains(q) ? res.add(b.getAuthor() + "//" + "Autor") : null;
            a = b.getOriginalTitle() != null && b.getOriginalTitle().toLowerCase().contains(q)
                    ? res.add(b.getOriginalTitle() + "//" + "Título original")
                    : null;
            a = b.getIsbn().toLowerCase().startsWith(q) ? res.add(b.getIsbn() + "//" + "ISBN") : null;
            a = b.getPublisher().toLowerCase().contains(q) ? res.add(b.getPublisher() + "//" + "Editorial") : null;
            a = b.getStatus().toLowerCase().startsWith(q) ? res.add(b.getStatus() + "//" + "Estado") : null;
            a = b.getUsername().toLowerCase().startsWith(q) ? res.add(b.getUsername() + "//" + "Nombre de usuario")
                    : null;
        }

        Arrays.asList(GenreEnum.values()).stream().map(x -> x.toString().replace("_", " "))
                .filter(x -> x.toLowerCase().startsWith(q)).forEach(x -> res.add(x + "//" + "Género"));

        List<String> titles = new ArrayList<>(res);
        if (res.size() >= 20) {
            return titles.subList(0, 20);
        } else {
            return titles;
        }
    }

    @Transactional(readOnly = true)
    public Map<Integer, List<Book>> searchBook(String query, Pageable pageable, String username, String type) {
        Map<Integer, List<Book>> map = new HashMap<>();

        NativeSearchQueryBuilder searchQuery = new NativeSearchQueryBuilder();

        List<Book> books = new ArrayList<>();
        Boolean isSearchToBook = true;
        Page<Book> pageBooks = null;

        if (type.equals("publicationYear")) {
            searchQuery.withQuery(QueryBuilders.matchQuery("publicationYear", Integer.parseInt(query)));
        } else if (type.equals("rangePrices")) {
            String q = query.replace("€", "");
            String[] splitQuery = q.split("-");
            searchQuery.withQuery(QueryBuilders.matchAllQuery())
                    .withFilter(QueryBuilders.rangeQuery("price").gte(Integer.parseInt(splitQuery[0]))
                            .lte(Integer.parseInt(splitQuery[1])))
                    .withSort(SortBuilders.fieldSort("price").order(SortOrder.ASC));
        } else if (type.equals("rangeYears")) {
            String[] splitQuery = query.split("-");
            searchQuery.withQuery(QueryBuilders.matchAllQuery())
                    .withFilter(QueryBuilders.rangeQuery("publicationYear").gte(Integer.parseInt(splitQuery[0]))
                            .lte(Integer.parseInt(splitQuery[1])))
                    .withSort(SortBuilders.fieldSort("publicationYear").order(SortOrder.ASC));
        } else if (type.equals("postalCode")) {
            List<String> usernames = this.userRepository.findByPostCode(query).stream().map(x -> x.getUsername())
                    .collect(Collectors.toList());
            pageBooks = this.bookRepository.findByUsernameIn(usernames, pageable);
            books = pageBooks.getContent();
            isSearchToBook = false;
        } else if (type.equals("province")) {
            List<String> usernames = this.userRepository.findByProvince(query).stream().map(x -> x.getUsername())
                    .collect(Collectors.toList());
            pageBooks = this.bookRepository.findByUsernameIn(usernames, pageable);
            books = pageBooks.getContent();
            isSearchToBook = false;
        } else {
            searchQuery.withQuery(QueryBuilders.multiMatchQuery(query).field("title").field("description")
                    .field("originalTitle").field("isbn").field("publisher").field("genres").field("author")
                    .field("status").field("username").operator(Operator.OR)
                    .type(MultiMatchQueryBuilder.Type.BEST_FIELDS).fuzziness(Fuzziness.ONE).prefixLength(2));
        }

        searchQuery.withPageable(pageable);
        NativeSearchQuery queryPrincipal = searchQuery.build();
        SearchHits<Book> hits = elasticsearchTemplate.search(queryPrincipal, Book.class, IndexCoordinates.of("books"));

        if (isSearchToBook) {
            books = hits.stream().map(x -> x.getContent()).collect(Collectors.toList());
        }

        List<Book> res = new ArrayList<>();
        Integer numPages = 0;

        if (books.size() > 0) {
            res = books;
            Integer totalBooks = isSearchToBook ? (int) hits.getTotalHits() : (int) pageBooks.getNumberOfElements();

            numPages = (int) Math.ceil(totalBooks / pageable.getPageSize()) + 1;
        } else if (username != null) {
            User user = this.userRepository.findByUsername(username);
            res = this.recommendBooks(user, PageRequest.of(0, 21)).values().stream().findFirst()
                    .orElse(new ArrayList<>());
            if (res.isEmpty()) {
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

        if (pageable.getPageNumber() < 5) {
            List<Book> booksOfUser = this.bookRepository.findByUsername(user.getUsername());
            List<UserFavouriteBook> favouritesBooksOfUser = this.userFavouriteBookRepository
                    .findByUsername(user.getUsername(), Pageable.unpaged()).getContent();

            List<Book> favouritesBooks = favouritesBooksOfUser.stream()
                    .map(x -> this.bookRepository.findById(x.getBookId()).orElse(null)).collect(Collectors.toList());
            booksOfUser.addAll(favouritesBooks);

            List<FunctionScoreQueryBuilder.FilterFunctionBuilder> filterFunctionBuilders = new ArrayList<>();

            if (!booksOfUser.isEmpty()) {
                for (Book b : booksOfUser) {
                    if (b != null) {
                        filterFunctionBuilders.add(new FunctionScoreQueryBuilder.FilterFunctionBuilder(
                                QueryBuilders.matchQuery("publisher", b.getPublisher()),
                                ScoreFunctionBuilders.weightFactorFunction(12)));
                        filterFunctionBuilders.add(new FunctionScoreQueryBuilder.FilterFunctionBuilder(
                                QueryBuilders.matchQuery("author", b.getAuthor()),
                                ScoreFunctionBuilders.weightFactorFunction(10)));
                        for (String genre : b.getGenres().split(",")) {
                            filterFunctionBuilders.add(new FunctionScoreQueryBuilder.FilterFunctionBuilder(
                                    QueryBuilders.matchQuery("genres", genre),
                                    ScoreFunctionBuilders.weightFactorFunction(10)));
                        }
                    }
                }
            }

            filterFunctionBuilders.add(new FunctionScoreQueryBuilder.FilterFunctionBuilder(
                    QueryBuilders.matchQuery("genres", user.getGenres()),
                    ScoreFunctionBuilders.weightFactorFunction(8)));

            FunctionScoreQueryBuilder.FilterFunctionBuilder[] builders = new FunctionScoreQueryBuilder.FilterFunctionBuilder[filterFunctionBuilders
                    .size()];
            filterFunctionBuilders.toArray(builders);
            FunctionScoreQueryBuilder functionScoreQueryBuilder = QueryBuilders.functionScoreQuery(builders)
                    .scoreMode(FunctionScoreQuery.ScoreMode.MAX).setMinScore(8);

            BoolQueryBuilder queryFilter = new BoolQueryBuilder();

            for (Book book : booksOfUser) {
                if (book != null) {
                    BoolQueryBuilder boolQueryBuilder1 = new BoolQueryBuilder();
                    boolQueryBuilder1.mustNot(QueryBuilders.matchQuery("id", book.getId()));
                    queryFilter.must(boolQueryBuilder1);
                }
            }
            BoolQueryBuilder boolQueryBuilder2 = new BoolQueryBuilder();
            boolQueryBuilder2.mustNot(QueryBuilders.matchQuery("username", user.getUsername()));

            queryFilter.must(boolQueryBuilder2);

            NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
            builder.withQuery(functionScoreQueryBuilder);
            builder.withFilter(queryFilter);
            builder.withPageable(pageable);
            NativeSearchQuery searchQuery = builder.build();

            SearchHits<Book> searchHits = elasticsearchTemplate.search(searchQuery, Book.class);

            if (searchHits.getTotalHits() <= 0) {
                List<Book> bk = this.bookRepository.findByGenresLike(user.getGenres(), PageRequest.of(0, 21))
                        .getContent();
                if (bk.isEmpty()) {
                    bk = this.bookRepository.findByUsernameNot(user.getUsername(), PageRequest.of(0, 21)).getContent();
                }
                res.put(1, bk);
                return res;
            }

            List<Book> searchBookList = searchHits.stream().map(x -> x.getContent()).collect(Collectors.toList());
            Integer numPages = 5;

            if ((int) Math.ceil(searchHits.getTotalHits() / pageable.getPageSize()) < 5) {
                numPages = (int) Math.ceil(searchHits.getTotalHits() / pageable.getPageSize());
            }

            res.put(numPages, searchBookList);
        } else {
            res.put(5, new ArrayList<>());
        }

        return res;
    }

    @Transactional
    public void saveSearch(Search search, String username) {
        if (username != null) {
            search.setUsername(username);
            Search s = this.searchRepository.findFirstByUsername(username);
            if (s != null) {
                search.setId(s.getId());
            }
            this.searchRepository.save(search);
        }
    }

    @Transactional
    public Search findByUsername(String username) {
        return this.searchRepository.findFirstByUsername(username);
    }

    @Transactional
    public Integer countSearchs() {
        return Integer.parseInt("" + this.searchRepository.count());
    }

}
