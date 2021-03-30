package com.pabcubcru.infobooks.repository;

import com.pabcubcru.infobooks.models.Book;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends ElasticsearchRepository<Book, String> {
    
}
