package com.pabcubcru.infobooks.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pabcubcru.infobooks.models.Book;
import com.pabcubcru.infobooks.services.SearchService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping(value = "/search")
public class SearchController {

    @Autowired
    private SearchService searchService;

    @GetMapping(value = {"/{query}", "/"})
    public ModelAndView main() {
        ModelAndView model = new ModelAndView();
		model.setViewName("Main");
		return model;
    }

    @GetMapping(value = "/q/{query}")
    public Map<String, Object> searchBooks(@PathVariable("query") String query) {
        Map<String, Object> res = new HashMap<>();

        List<Book> books = this.searchService.searchBook(query);

        res.put("books", books);

        return res;
    }
    
}
