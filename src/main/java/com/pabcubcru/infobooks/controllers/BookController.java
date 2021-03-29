package com.pabcubcru.infobooks.controllers;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import com.pabcubcru.infobooks.models.Book;
import com.pabcubcru.infobooks.services.BookService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping(value = "/books")
public class BookController {

    @Autowired
    private BookService bookService;

    @GetMapping(value = "/new")
	public ModelAndView main() {
		ModelAndView model = new ModelAndView();
		model.setViewName("Main");
		return model;
	}

    @PostMapping(value = "/new")
    public Map<String, Object> saveBook(@RequestBody @Valid Book book, Principal principal) {
        Map<String, Object> res = new HashMap<>();

        book.setUsername(principal.getName());

        this.bookService.save(book);
        res.put("success", true);

        return res;
    }

    @GetMapping(value="/all")
    public Map<String, Object> findAll() {
        Map<String, Object> res = new HashMap<>();

        res.put("books", this.bookService.findAll());

        return res;
    }
    
}
