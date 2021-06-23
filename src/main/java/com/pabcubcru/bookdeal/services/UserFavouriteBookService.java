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

    private UserFavouriteBookRepository userFavouriteBookRepository;

    @Autowired
    public UserFavouriteBookService(UserFavouriteBookRepository userFavouriteBookRepository) {
        this.userFavouriteBookRepository = userFavouriteBookRepository;
    }

    @Transactional
    public void save(UserFavouriteBook userFavouriteBook) {
        this.userFavouriteBookRepository.save(userFavouriteBook);
    }

    @Transactional
    public void delete(UserFavouriteBook userFavouriteBook) {
        this.userFavouriteBookRepository.delete(userFavouriteBook);
    }

    @Transactional(readOnly = true)
    public Page<UserFavouriteBook> findAllByUsername(String username, Pageable pageable) {
        return this.userFavouriteBookRepository.findByUsername(username, pageable);
    }

    @Transactional
    public UserFavouriteBook findByUsernameAndBookId(String username, String bookId) {
        return this.userFavouriteBookRepository.findByUsernameAndBookId(username, bookId);
    }

    @Transactional
    public Integer countFavouritesBooks() {
        return Integer.parseInt("" + this.userFavouriteBookRepository.count());
    }
}
