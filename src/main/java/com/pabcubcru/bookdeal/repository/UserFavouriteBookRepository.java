package com.pabcubcru.bookdeal.repository;

import com.pabcubcru.bookdeal.models.UserFavouriteBook;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserFavouriteBookRepository extends ElasticsearchRepository<UserFavouriteBook, String> {

    Page<UserFavouriteBook> findByUsername(String username, Pageable pageable);

    UserFavouriteBook findByUsernameAndBookId(String username, String bookId);
}
