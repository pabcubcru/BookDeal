package com.pabcubcru.infobooks.services;

import java.util.ArrayList;
import java.util.List;

import com.pabcubcru.infobooks.models.Book;
import com.pabcubcru.infobooks.repository.BookRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookService {

    private BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
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
    
}
