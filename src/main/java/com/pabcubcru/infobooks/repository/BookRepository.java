package com.pabcubcru.infobooks.repository;

import java.util.List;
import java.util.Optional;

import com.pabcubcru.infobooks.models.Book;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends ElasticsearchRepository<Book, String> {
    
    public List<Book> findByUsername(String username);

    public Optional<Book> findById(String id);
}
