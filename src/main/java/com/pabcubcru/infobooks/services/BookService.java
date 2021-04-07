package com.pabcubcru.infobooks.services;

import java.util.ArrayList;
import java.util.List;

import com.pabcubcru.infobooks.models.Book;
import com.pabcubcru.infobooks.repository.BookRepository;

import org.springframework.beans.factory.annotation.Autowired;
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
    public List<Book> findAll() {
        List<Book> res = new ArrayList<Book>();
        this.bookRepository.findAll().iterator().forEachRemaining(res::add);
        return res;
    }

    @Transactional(readOnly = true)
    public List<Book> findAllExceptMine(String username) {
        List<Book> res = new ArrayList<Book>();
        this.bookRepository.findByUsernameNot(username).iterator().forEachRemaining(res::add);
        return res;
    }

    @Transactional(readOnly = true)
    public List<Book> findMyBooks(String username) {
        List<Book> res = new ArrayList<>();
        this.bookRepository.findByUsername(username).iterator().forEachRemaining(res::add);
        return res;
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
    
}
