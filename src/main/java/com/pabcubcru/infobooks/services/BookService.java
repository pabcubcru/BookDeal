package com.pabcubcru.infobooks.services;

import java.util.ArrayList;
import java.util.List;

import com.pabcubcru.infobooks.models.Book;
import com.pabcubcru.infobooks.models.User;
import com.pabcubcru.infobooks.repository.BookRepository;
import com.pabcubcru.infobooks.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookService {

    private BookRepository bookRepository;

    private UserRepository userRepository;

    @Autowired
    public BookService(BookRepository bookRepository, UserRepository userRepository) {
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public void save(Book book) {
        this.bookRepository.save(book);
    }

    @Transactional(readOnly = true)
    public Page<Book> findAll(Pageable pageable) {
        return this.bookRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Page<Book> findAllExceptMine(String username, Pageable pageable) {
        return this.bookRepository.findByUsernameNot(username, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Book> findMyBooks(String username, Pageable pageable) {
        return this.bookRepository.findByUsername(username, pageable);
    }

    @Transactional
    public Book findBookById(String id) {
        return this.bookRepository.findById(id).orElse(null);
    }

    @Transactional
    public void deleteBookById(String id) {
        this.bookRepository.deleteById(id);
    }

    @Transactional
    public List<Book> findByIds(List<String> bookIds) {
        List<Book> res = new ArrayList<>();
        this.bookRepository.findAllById(bookIds).iterator().forEachRemaining(res::add);
        
        return res;
    }

    @Transactional
    public List<Book> findByUsername(String username) {
        return this.bookRepository.findByUsername(username);
    }

    @Transactional
    public Page<Book> findNearBooks(User user, Pageable pageable, String showMode) {

        List<User> usersWithSameAddress = null;
        List<String> usernames = new ArrayList<>();

        if(showMode.equals("postCode")) {
            usersWithSameAddress = this.userRepository.findByPostCode(user.getPostCode());
            usersWithSameAddress.stream().filter(x -> !x.getUsername().equals(user.getUsername())).forEach(x -> usernames.add(x.getUsername()));
        } else if(showMode.equals("province")) {
            usersWithSameAddress = this.userRepository.findByProvince(user.getProvince());
            usersWithSameAddress.stream().filter(x -> !x.getUsername().equals(user.getUsername())).forEach(x -> usernames.add(x.getUsername()));
        }else if(showMode.equals("genres")) {
            return this.bookRepository.findByGenresLike(user.getGenres(), pageable);
        } else {
            usersWithSameAddress = this.userRepository.findByPostCodeOrProvince(user.getPostCode(), user.getProvince());
            usersWithSameAddress.stream().filter(x -> !x.getUsername().equals(user.getUsername())).forEach(x -> usernames.add(x.getUsername()));
        }

        Page<Book> books = this.bookRepository.findByUsernameIn(usernames, pageable);

        return books;
    }
    
}
