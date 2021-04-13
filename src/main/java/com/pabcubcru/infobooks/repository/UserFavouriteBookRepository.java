package com.pabcubcru.infobooks.repository;

import com.pabcubcru.infobooks.models.UserFavouriteBook;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserFavouriteBookRepository extends ElasticsearchRepository<UserFavouriteBook, String> {
    
    Page<UserFavouriteBook> findByUsername(String username, Pageable pageable);

    UserFavouriteBook findByUsernameAndBookId(String username, String bookId);
}
