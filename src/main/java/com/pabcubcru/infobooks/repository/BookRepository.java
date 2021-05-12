package com.pabcubcru.infobooks.repository;

import java.util.List;
import java.util.Optional;

import com.pabcubcru.infobooks.models.Book;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends ElasticsearchRepository<Book, String> {

    public Page<Book> findAll(Pageable pageable);

    public Page<Book> findByUsername(String username, Pageable pageable);

    public Optional<Book> findById(String id);

    public Page<Book> findByUsernameNot(String username, Pageable pageable);

    public List<Book> findByUsername(String username);

    public Long countByUsername(String username);

    public Page<Book> findByUsernameIn(List<String> usernames, Pageable pageable);

    public Page<Book> findByGenresLike(String genre, Pageable pageable);
}
