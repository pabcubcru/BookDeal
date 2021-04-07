package com.pabcubcru.infobooks.services;

import java.util.ArrayList;
import java.util.List;

import com.pabcubcru.infobooks.models.UserFavouriteBook;
import com.pabcubcru.infobooks.repository.UserFavouriteBookRepository;

import org.springframework.beans.factory.annotation.Autowired;
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
    public List<UserFavouriteBook> findAllByUsername(String username) {
        List<UserFavouriteBook> res = new ArrayList<>();
        this.userFavoruriteBookRepository.findByUsername(username).iterator().forEachRemaining(res::add);
        return res;
    }

    @Transactional
    public UserFavouriteBook findByUsernameAndBookId(String username, String bookId) {
        return this.userFavoruriteBookRepository.findByUsernameAndBookId(username, bookId);
    }
}
