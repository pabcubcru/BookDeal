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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class BookController {

    @Autowired
    private BookService bookService;

    @Autowired
    private UserService userService;

    @GetMapping(value = "/books/new")
	public ModelAndView main() {
		ModelAndView model = new ModelAndView();
		model.setViewName("Main");
		return model;
	}

    @PostMapping("/books/new")
    public Map<String, Object> saveBook(@RequestBody @Valid Book book, Principal principal) {
        Map<String, Object> res = new HashMap<>();

        User user = this.userService.findByUsername(principal.getName());
        book.setUser(user);

        this.bookService.save(book);
        res.put("success", true);

        return res;
    }
    
}
