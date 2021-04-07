package com.pabcubcru.infobooks.controllers;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pabcubcru.infobooks.models.UserFavouriteBook;
import com.pabcubcru.infobooks.services.BookService;
import com.pabcubcru.infobooks.services.UserFavouriteBookService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping(value = "/favourites")
public class UserFavouriteBookController {

    @Autowired
    private UserFavouriteBookService userFavouriteBookService;

    @Autowired
    private BookService bookService;

    @GetMapping(value = {""})
    public ModelAndView main() {
        ModelAndView model = new ModelAndView();
        model.setViewName("Main");
        return model;
    }

    @GetMapping(value = "/all")
    public Map<String, Object> findAllByUsername(Principal principal) {
        Map<String, Object> res = new HashMap<>();
        List<String> idBooks = new ArrayList<>();

        List<UserFavouriteBook> ufbooks = this.userFavouriteBookService.findAllByUsername(principal.getName());
        ufbooks.stream().forEach(x -> idBooks.add(x.getBookId()));
        
        res.put("books", this.bookService.findByIds(idBooks));

        return res;
    }

    @GetMapping(value = "/{id}/add")
    public Map<String, Object> addFavouriteBook(@PathVariable("id") String id, Principal principal) {
        Map<String, Object> res = new HashMap<>();

        try {
            if(this.userFavouriteBookService.findByUsernameAndBookId(principal.getName(), id) == null) {
                UserFavouriteBook ufbook = new UserFavouriteBook();
                ufbook.setBookId(id);
                ufbook.setUsername(principal.getName());
                this.userFavouriteBookService.save(ufbook);
                res.put("success", true);
            } else {
                res.put("alreadyAdded", true);
                res.put("success", false);
            }
        } catch (Exception e) {
            res.put("success", false);
        }

        return res;
    }

    @DeleteMapping(value = "/{id}/delete")
    public void deleteFavouriteBook(@PathVariable("id") String id, Principal principal) {
        UserFavouriteBook ufbook = this.userFavouriteBookService.findByUsernameAndBookId(principal.getName(), id);
        if(ufbook != null) {
            this.userFavouriteBookService.delete(ufbook);
        }
    }
    
}
