package com.pabcubcru.infobooks.controllers;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import com.pabcubcru.infobooks.models.Book;
import com.pabcubcru.infobooks.models.GenreEnum;
import com.pabcubcru.infobooks.models.Request;
import com.pabcubcru.infobooks.models.RequestStatus;
import com.pabcubcru.infobooks.services.BookService;
import com.pabcubcru.infobooks.services.RequestService;
import com.pabcubcru.infobooks.services.UserFavouriteBookService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping(value = "/books")
public class BookController {

    @Autowired
    private BookService bookService;

    @Autowired
    private UserFavouriteBookService userFavouriteBookService;

    @Autowired
    private RequestService requestService;

    @GetMapping(value = {"/new", "/me", "/all"})
	public ModelAndView main() {
		ModelAndView model = new ModelAndView();
		model.setViewName("Main");
		return model;
	}

    @GetMapping(value = {"/{id}"})
	public ModelAndView mainwithSecurity(@PathVariable("id") String id) {
		ModelAndView model = new ModelAndView();
		model.setViewName("Main");
        if(id != null) {
            Book book = this.bookService.findBookById(id);
            if(book == null) {
                model.setViewName("errors/Error404");
            }
        }
		return model;
	}

    @GetMapping(value = {"/{id}/edit"})
    public ModelAndView mainWithUserSecurity(Principal principal, @PathVariable("id") String id) {
        ModelAndView model = new ModelAndView();
        model.setViewName("Main");
        if(id != null) {
            Book book = this.bookService.findBookById(id);
            if(book == null) {
                model.setViewName("errors/Error404");
            } else if(!book.getUsername().equals(principal.getName())) {
                model.setViewName("errors/Error403");
            }
        }
        
        return model;
    }

    @PostMapping(value = "/new")
    public Map<String, Object> create(@RequestBody @Valid Book book, Principal principal) {
        Map<String, Object> res = new HashMap<>();

        try{
            if(book.getAction().equals("INTERCAMBIO")) {
                book.setPrice(null);
            }
            book.setUsername(principal.getName());
            this.bookService.save(book);
            res.put("success", true);
        } catch(Exception e) {
            res.put("success", false);
        }
        return res;
    }

    @PutMapping(value = "/{id}/edit")
    public Map<String, Object> edit(@RequestBody @Valid Book book, Principal principal) {
        Map<String, Object> res = new HashMap<>();

        try{
            if(book.getAction().equals("INTERCAMBIO")) {
                book.setPrice(null);
            }
            book.setUsername(principal.getName());
            this.bookService.save(book);
            res.put("success", true);
        } catch(Exception e) {
            res.put("success", false);
        }

        return res;
    }

    @DeleteMapping("/{id}/delete")
    public void delete(@PathVariable("id") String id, Principal principal) {
        Book book = this.bookService.findBookById(id);
        if(book.getUsername().equals(principal.getName())) {
            this.bookService.deleteBookById(id);
        }
    }

    @GetMapping(value="/list/all-me")
    public Map<String, Object> findAllExceptMine(Principal principal) {
        Map<String, Object> res = new HashMap<>();

        if(principal == null) {
            res.put("books", this.bookService.findAll());
        } else {
            List<Book> books = this.bookService.findAllExceptMine(principal.getName());
            res.put("books", books);
            List<Boolean> isAdded = new ArrayList<>();
            for(Book book : books) {
                if(this.userFavouriteBookService.findByUsernameAndBookId(principal.getName(), book.getId()) == null) {
                    isAdded.add(false);
                } else {
                    isAdded.add(true);
                }
            }
            res.put("isAdded", isAdded);
        }

        return res;
    }

    @GetMapping(value="/list/me")
    public Map<String, Object> findMyBooks(Principal principal){
        Map<String, Object> res = new HashMap<>();

        res.put("books", this.bookService.findMyBooks(principal.getName()));

        return res;
    }

    @GetMapping(value="/genres")
    public Map<String, Object> getGenres() {
        Map<String, Object> res = new HashMap<>();

        res.put("genres", List.of(GenreEnum.values()));

        return res;
    }

    @GetMapping(value = "/get/{id}")
    public Map<String, Object> getBookById(@PathVariable("id") String id, Principal principal) {
        Map<String, Object> res = new HashMap<>();
        try {
            if(principal != null) {
                Request request = this.requestService.findByUsername1AndIdBook2(principal.getName(), id);
                if(request != null) {
                    res.put("alreadyRequest", true);
                } else {
                    res.put("alreadyRequest", false);
                }
                Request requestAcceptedToBook1 = this.requestService.findFirstByIdBook1AndStatus(id, RequestStatus.ACEPTADA.toString());
                Request requestAcceptedToBook2 = this.requestService.findFirstByIdBook2AndStatus(id, RequestStatus.ACEPTADA.toString());
                if(requestAcceptedToBook1 != null || requestAcceptedToBook2 != null) {
                    res.put("hasRequestAccepted", true);
                } else {
                    res.put("hasRequestAccepted", false);
                }
            }
            Book book = this.bookService.findBookById(id);
            res.put("book", book);
            res.put("success", true);
            if(this.userFavouriteBookService.findByUsernameAndBookId(principal.getName(), book.getId()) == null) {
                res.put("isAdded", false);
            } else {
                res.put("isAdded", true);
            }
        } catch (Exception e) {
            res.put("success", false);
        }

        return res;
    }
    
}
