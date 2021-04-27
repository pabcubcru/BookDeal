package com.pabcubcru.infobooks.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.pabcubcru.infobooks.models.Book;
import com.pabcubcru.infobooks.models.User;
import com.pabcubcru.infobooks.repository.BookRepository;
import com.pabcubcru.infobooks.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
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
    public List<Book> findByUsernameAndAction(String username, String action) {
        return this.bookRepository.findByUsernameAndAction(username, action);
    }

    @Transactional
    public Page<Book> findNearBooks(User user, Pageable pageable, String showMode) {
        List<User> usersWithSameAddress = new ArrayList<>();
        if(showMode.equals("postalCode")) {
            usersWithSameAddress = this.userRepository.findByPostCode(user.getPostCode());
        } else if(showMode.equals("city")) {
            usersWithSameAddress = this.userRepository.findByCity(user.getCity());
        } else if(showMode.equals("province")) {
            usersWithSameAddress = this.userRepository.findByProvince(user.getProvince());
        } else {
            usersWithSameAddress = this.userRepository.findByPostCodeOrCityOrProvince(user.getPostCode(), user.getCity(), user.getProvince());
        }
        List<Book> listBooks = new ArrayList<>();

        for(User u : usersWithSameAddress) {
            if(!u.getUsername().equals(user.getUsername())) {
                int num = (int) Math.floor(21/(usersWithSameAddress.size()-1 <= 0 ? 1 : usersWithSameAddress.size()-1));
                int numItems = num < 1 ? 1 : num;
                Page<Book> books = this.bookRepository.findByUsername(u.getUsername(), PageRequest.of(pageable.getPageNumber(), numItems));
                listBooks.addAll(books.getContent());
            }
        }
        return new PageImpl<>(listBooks, pageable, listBooks.size());
    }
    
}
