package com.pabcubcru.infobooks.repository;

import java.util.List;

import com.pabcubcru.infobooks.models.UserFavouriteBook;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserFavouriteBookRepository extends ElasticsearchRepository<UserFavouriteBook, String> {
    
    List<UserFavouriteBook> findByUsername(String username);

    UserFavouriteBook findByUsernameAndBookId(String username, String bookId);
}
