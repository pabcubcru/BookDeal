package com.pabcubcru.infobooks.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.pabcubcru.infobooks.models.Book;
import com.pabcubcru.infobooks.repository.BookRepository;

import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilders;
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

    @Autowired
    public SearchService(ElasticsearchRestTemplate elasticsearchTemplate) {
        this.elasticsearchTemplate = elasticsearchTemplate;
    }

    @Transactional(readOnly = true)
    public Map<Integer, List<Book>> searchBook(String query, Pageable pageable) {
        Map<Integer, List<Book>> map = new HashMap<>();

        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
        .withQuery(QueryBuilders.multiMatchQuery(query)
        .field("title")
        .field("description")
        .field("originalTitle")
        .field("isbn")
        .field("publisher")
        .field("genres")
        .field("author")
        .field("status")
        .field("action")
        //.field("price")
        //.field("publicationYear")
        .operator(Operator.OR)
        .type(MultiMatchQueryBuilder.Type.BEST_FIELDS)
        .fuzziness(Fuzziness.ONE)
        .prefixLength(2))
        .withPageable(pageable)
        .build();

        SearchHits<Book> books = elasticsearchTemplate.search(searchQuery, Book.class, IndexCoordinates.of("books"));

        List<Book> res = books.stream().map(x -> x.getContent()).collect(Collectors.toList());

        Integer numPages = (int) Math.ceil(books.getTotalHits() / pageable.getPageSize())+1;

        map.put(numPages, res);
        return map;

    }
    
}
