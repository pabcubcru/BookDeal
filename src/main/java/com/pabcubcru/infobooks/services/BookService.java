package com.pabcubcru.infobooks.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

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
    public Map<Integer, Page<Book>> findNearBooks(User user, Pageable pageable, String showMode) {
        Map<Integer, Page<Book>> res = new HashMap<>();
        Integer numBooks = 0;

        Page<User> usersWithSameAddress = null;
        List<Book> books = new ArrayList<>();

        if(showMode.equals("postalCode")) {
            usersWithSameAddress = this.userRepository.findByPostCode(user.getPostCode(), Pageable.unpaged());
            usersWithSameAddress.getContent().stream().filter(x -> !x.getUsername().equals(user.getUsername())).forEach(x -> books.addAll(this.bookRepository.findByUsername(x.getUsername(), Pageable.unpaged()).getContent()));
            numBooks = books.size();
        } else if(showMode.equals("province")) {
            usersWithSameAddress = this.userRepository.findByProvince(user.getProvince(), Pageable.unpaged());
            usersWithSameAddress.getContent().stream().filter(x -> !x.getUsername().equals(user.getUsername())).forEach(x -> books.addAll(this.bookRepository.findByUsername(x.getUsername(), Pageable.unpaged()).getContent()));
            numBooks = books.size();
        } else {
            usersWithSameAddress = this.userRepository.findByPostCodeOrProvince(user.getPostCode(), user.getProvince(), Pageable.unpaged());
            usersWithSameAddress.getContent().stream().filter(x -> !x.getUsername().equals(user.getUsername())).forEach(x -> books.addAll(this.bookRepository.findByUsername(x.getUsername(), Pageable.unpaged()).getContent()));
            numBooks = books.size();
        }

        List<Book> listBooks = new ArrayList<>();

        if(numBooks > 0) {
            for(int i = 21*pageable.getPageNumber(); i<21*pageable.getPageNumber()+21; i++) {
                if(books.size()-1 >= i) {
                    listBooks.add(books.get(i));
                }
            }
        }
        res.put((int) Math.floor((numBooks/21))+1, new PageImpl<>(listBooks, pageable, listBooks.size()));
        return res;
    }
    
}
