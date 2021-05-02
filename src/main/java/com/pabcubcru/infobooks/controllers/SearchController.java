package com.pabcubcru.infobooks.controllers;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.pabcubcru.infobooks.models.Book;
import com.pabcubcru.infobooks.models.Request;
import com.pabcubcru.infobooks.models.RequestStatus;
import com.pabcubcru.infobooks.services.RequestService;
import com.pabcubcru.infobooks.services.SearchService;
import com.pabcubcru.infobooks.services.UserFavouriteBookService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping(value = "/search")
public class SearchController {

    @Autowired
    private SearchService searchService;

    @Autowired
    private RequestService requestService;

    @Autowired
    private UserFavouriteBookService userFavouriteBookService;

    @GetMapping(value = {"/{page}/{query}", "/*"})
    public ModelAndView main() {
        ModelAndView model = new ModelAndView();
		model.setViewName("Main");
		return model;
    }

    @GetMapping(value = "/q/{query}")
    public Map<String, Object> searchBooks(@PathVariable("query") String query, Principal principal, @RequestParam("page") Integer page) {
        Map<String, Object> res = new HashMap<>();
        PageRequest pageRequest = PageRequest.of(page, 21);

        String username = principal != null ? principal.getName() : null;

        Map<Integer, List<Book>> map = this.searchService.searchBook(query, pageRequest, username);
        List<Book> pageOfBooks = map.values().stream().findFirst().orElse(new ArrayList<>());
        Integer numberOfPages = map.keySet().stream().findFirst().orElse(0);
        res.put("searchResult", true);
        if(numberOfPages == 0) {
            res.put("searchResult", false);
        }
        List<Book> books = new ArrayList<>();
        for(Book b : pageOfBooks) {
            Request requestAcceptedToBook1 = this.requestService.findFirstByIdBook1AndStatus(b.getId(), RequestStatus.ACEPTADA.toString());
            Request requestAcceptedToBook2 = this.requestService.findFirstByIdBook2AndStatus(b.getId(), RequestStatus.ACEPTADA.toString());
            if(requestAcceptedToBook1 == null && requestAcceptedToBook2 == null) {
                books.add(b);
            }
        }
        if(principal != null) {
            List<Boolean> isAdded = new ArrayList<>();
            for(Book book : books) {
                if(this.userFavouriteBookService.findByUsernameAndBookId(principal.getName(), book.getId()) == null) {
                    isAdded.add(false);
                } else {
                    isAdded.add(true);
                }
            }
            res.put("isAdded", isAdded);
            res.put("page", page);
        }

        res.put("books", books);

        res.put("numTotalPages", numberOfPages);
        res.put("pages", new ArrayList<Integer>());
        if(numberOfPages > 0) {
            List<Integer> pages = IntStream.rangeClosed(page-5 <= 0 ? 0 : page-5, page+5 >= numberOfPages-1 ? numberOfPages-1 : page+5).boxed().collect(Collectors.toList());
            res.put("pages", pages);
        }
        
        return res;
    }
    
}
