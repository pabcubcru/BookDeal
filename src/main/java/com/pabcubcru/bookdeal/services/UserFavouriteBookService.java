package com.pabcubcru.bookdeal.services;

import com.pabcubcru.bookdeal.models.UserFavouriteBook;
import com.pabcubcru.bookdeal.repository.UserFavouriteBookRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserFavouriteBookService {

    private UserFavouriteBookRepository userFavoruriteBookRepository;

    @Autowired
    public UserFavouriteBookService(UserFavouriteBookRepository userFavoruriteBookRepository) {
        this.userFavoruriteBookRepository = userFavoruriteBookRepository;
    }

    @Transactional
    public void save(UserFavouriteBook userFavouriteBook) {
        this.userFavoruriteBookRepository.save(userFavouriteBook);
    }

    @Transactional
    public void delete(UserFavouriteBook userFavouriteBook) {
        this.userFavoruriteBookRepository.delete(userFavouriteBook);
    }

    @Transactional(readOnly = true)
    public Page<UserFavouriteBook> findAllByUsername(String username, Pageable pageable) {
        return this.userFavoruriteBookRepository.findByUsername(username, pageable);
    }

    @Transactional
    public UserFavouriteBook findByUsernameAndBookId(String username, String bookId) {
        return this.userFavoruriteBookRepository.findByUsernameAndBookId(username, bookId);
    }
}
