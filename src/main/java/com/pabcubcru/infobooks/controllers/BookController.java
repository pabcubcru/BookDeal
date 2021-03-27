package com.pabcubcru.infobooks.controllers;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import com.pabcubcru.infobooks.models.Book;
import com.pabcubcru.infobooks.models.User;
import com.pabcubcru.infobooks.services.BookService;
import com.pabcubcru.infobooks.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/books")
public class BookController {

    @Autowired
    private BookService bookService;

    @Autowired
    private UserService userService;

    @PostMapping("/new")
    public Map<String, Object> saveBook(@RequestBody @Valid Book book, Principal principal) {
        Map<String, Object> res = new HashMap<>();

        User user = this.userService.findByUsername(principal.getName());
        book.setUser(user);

        this.bookService.save(book);
        res.put("success", true);

        return res;
    }
    
}
