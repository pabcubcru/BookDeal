package com.pabcubcru.infobooks.controllers;

import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.validation.Valid;

import com.pabcubcru.infobooks.models.Book;
import com.pabcubcru.infobooks.models.GenreEnum;
import com.pabcubcru.infobooks.models.Request;
import com.pabcubcru.infobooks.models.RequestStatus;
import com.pabcubcru.infobooks.models.User;
import com.pabcubcru.infobooks.services.BookService;
import com.pabcubcru.infobooks.services.RequestService;
import com.pabcubcru.infobooks.services.SearchService;
import com.pabcubcru.infobooks.services.UserFavouriteBookService;
import com.pabcubcru.infobooks.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @Autowired
    private UserService userService;

    @Autowired
    private SearchService searchService;

    @GetMapping(value = {"/new", "/me/{page}", "/all/{page}", "recommend/{page}"})
	public ModelAndView main() {
		ModelAndView model = new ModelAndView();
		model.setViewName("Main");
		return model;
	}

    @GetMapping(value = "/all/{page}/{showMode}")
    public ModelAndView mainForShowMode(@PathVariable("showMode") String showMode) {
		ModelAndView model = new ModelAndView();
		model.setViewName("Main");
        if(!showMode.equals("postCode") && !showMode.equals("province") && !showMode.equals("genres")) {
            model.setViewName("errors/Error404");
        }
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
            } else {
                Request requestAcceptedToBook1 = this.requestService.findFirstByIdBook1AndStatus(id, RequestStatus.ACEPTADA.toString());
                Request requestAcceptedToBook2 = this.requestService.findFirstByIdBook2AndStatus(id, RequestStatus.ACEPTADA.toString());
                if(requestAcceptedToBook1 != null || requestAcceptedToBook2 != null || !book.getUsername().equals(principal.getName())) {
                    model.setViewName("errors/Error403");
                }
            }
        } else {
            model.setViewName("errors/Error404");
        }
        
        return model;
    }

    public BindingResult validateBook(Book book, BindingResult result) {

        if(book.getPublicationYear() != null) {
            if(book.getPublicationYear() > LocalDate.now().getYear()) {
                result.rejectValue("publicationYear", "El año de publicación debe ser anterior o igual al presente año.", 
                "El año de publicación debe ser anterior o igual al presente año.");
            }
        }

        return result;
    }

    @PostMapping(value = "/new")
    public Map<String, Object> create(@RequestBody @Valid Book book, BindingResult result, Principal principal) {
        Map<String, Object> res = new HashMap<>();

        result = this.validateBook(book, result);
        if(!result.hasErrors()) {
            try{
                book.setUsername(principal.getName());
                this.bookService.save(book);
                res.put("success", true);
            } catch(Exception e) {
                res.put("success", false);
            }
        } else {
            res.put("errors", result.getAllErrors());
            res.put("success", false);
        }
        
        return res;
    }

    @PutMapping(value = "/{id}/edit")
    public Map<String, Object> edit(@RequestBody @Valid Book book, BindingResult result, Principal principal) {
        Map<String, Object> res = new HashMap<>();

        result = this.validateBook(book, result);
        if(!result.hasErrors()) {
            try{
                book.setUsername(principal.getName());
                this.bookService.save(book);
                res.put("success", true);
            } catch(Exception e) {
                res.put("success", false);
            }
        } else {
            res.put("errors", result.getAllErrors());
            res.put("success", false);
        }

        return res;
    }

    @DeleteMapping("/{id}/delete")
    public void delete(@PathVariable("id") String id, Principal principal) {
        Book book = this.bookService.findBookById(id);
        if(book.getUsername().equals(principal.getName())) {
            Request requestAcceptedToBook1 = this.requestService.findFirstByIdBook1AndStatus(id, RequestStatus.ACEPTADA.toString());
            Request requestAcceptedToBook2 = this.requestService.findFirstByIdBook2AndStatus(id, RequestStatus.ACEPTADA.toString());
            if(requestAcceptedToBook1 == null && requestAcceptedToBook2 == null) {
                List<Request> requests = this.requestService.findByIdBook1OrIdBook2(id, id);
                this.requestService.deleteAll(requests);
                this.bookService.deleteBookById(id);
            }
        }
    }

    @GetMapping(value="/list/all-me")
    public Map<String, Object> findAllExceptMine(Principal principal, Pageable pageable, 
    @RequestParam(name = "page", defaultValue = "0") Integer page, @RequestParam("showMode") String showMode) {
        Map<String, Object> res = new HashMap<>();
        Page<Book> pageOfBooks = null;
        PageRequest pageRequest = PageRequest.of(page, 21);
        Integer numberOfPages = 0;

        if(principal == null) {
            pageOfBooks = this.bookService.findAll(pageRequest);
            numberOfPages = pageOfBooks.getTotalPages();
            res.put("books", pageOfBooks.getContent());
        } else {
            List<Book> books = new ArrayList<>();
            List<Book> findBooks = new ArrayList<>();
            User user = this.userService.findByUsername(principal.getName());
            pageOfBooks = this.bookService.findNearBooks(user, pageRequest, showMode);
            res.put("nearBooks", true);
            if(showMode.equals("province")) {
                res.put("title", "Libros en su provincia");
            } else if(showMode.equals("postCode")) {
                res.put("title", "Libros en su código postal");
            } else if(showMode.equals("genres")) {
                res.put("title", "Libros por sus géneros preferidos");
            } else {
                res.put("title", "Libros cercanos a usted");
            }
            numberOfPages = pageOfBooks.getTotalPages();
            if(pageOfBooks.getNumberOfElements() <= 0) {
                Map<Integer, List<Book>> map = this.searchService.recommendBooks(user, PageRequest.of(0, 21));
                findBooks = map.values().stream().findFirst().orElse(new ArrayList<>());
                res.put("nearBooks", false);
                numberOfPages = 0;
            } else {
                findBooks = pageOfBooks.getContent();
            }
            for(Book b : findBooks) {
                Request requestAcceptedToBook1 = this.requestService.findFirstByIdBook1AndStatus(b.getId(), RequestStatus.ACEPTADA.toString());
                Request requestAcceptedToBook2 = this.requestService.findFirstByIdBook2AndStatus(b.getId(), RequestStatus.ACEPTADA.toString());
                if(requestAcceptedToBook1 == null && requestAcceptedToBook2 == null) {
                    books.add(b);
                }
            }
            res.put("books", books);
            List<Boolean> isAdded = new ArrayList<>();
            for(Book book : books) {
                if(this.userFavouriteBookService.findByUsernameAndBookId(principal.getName(), book.getId()) == null) {
                    isAdded.add(false);
                } else {
                    isAdded.add(true);
                }
            }
            res.put("page", page);
            res.put("isAdded", isAdded);
        }

        res.put("numTotalPages", numberOfPages);
        res.put("pages", new ArrayList<Integer>());
        if(numberOfPages > 0) {
            List<Integer> pages = IntStream.rangeClosed(page-5 <= 0 ? 0 : page-5, page+5 >= numberOfPages-1 ? numberOfPages-1 : page+5).boxed().collect(Collectors.toList());
            res.put("pages", pages);
        }

        return res;
    }

    @GetMapping(value="/list/me")
    public Map<String, Object> findMyBooks(Principal principal, Pageable pageable, @RequestParam(name = "page", defaultValue = "0") Integer page){
        Map<String, Object> res = new HashMap<>();
        PageRequest pageRequest = PageRequest.of(page, 21);
        Page<Book> pageOfBooks = this.bookService.findMyBooks(principal.getName(), pageRequest);
        
        res.put("books", pageOfBooks.getContent());

        Integer numberOfPages = pageOfBooks.getTotalPages();
        res.put("numTotalPages", numberOfPages);
        res.put("pages", new ArrayList<Integer>());
        if(numberOfPages > 0) {
            List<Integer> pages = IntStream.rangeClosed(page-5 <= 0 ? 0 : page-5, page+5 >= numberOfPages-1 ? numberOfPages-1 : page+5).boxed().collect(Collectors.toList());
            res.put("pages", pages);
        }

        return res;
    }

    @GetMapping(value="/list/me-change")
    public Map<String, Object> findMyBooksForChange(Principal principal) {
        Map<String, Object> res = new HashMap<>();
        List<Book> books = new ArrayList<>();

        List<Book> booksForChange = this.bookService.findByUsername(principal.getName());

        for(Book b : booksForChange) {
            Request requestAcceptedToBook1 = this.requestService.findFirstByIdBook1AndStatus(b.getId(), RequestStatus.ACEPTADA.toString());
            Request requestAcceptedToBook2 = this.requestService.findFirstByIdBook2AndStatus(b.getId(), RequestStatus.ACEPTADA.toString());
            if(requestAcceptedToBook1 == null && requestAcceptedToBook2 == null) {
                books.add(b);
            }
        }

        res.put("books", books);

        return res;
    } 

    @GetMapping(value="/genres")
    public Map<String, Object> getGenres() {
        Map<String, Object> res = new HashMap<>();

        res.put("genres", GenreEnum.values());

        return res;
    }

    @GetMapping(value = "/get/{id}")
    public Map<String, Object> getBookById(@PathVariable("id") String id, Principal principal) {
        Map<String, Object> res = new HashMap<>();
        try {
            Book book = this.bookService.findBookById(id);
            if(principal != null) {
                Request requestAcceptedToBook1 = this.requestService.findFirstByIdBook1AndStatus(id, RequestStatus.ACEPTADA.toString());
                Request requestAcceptedToBook2 = this.requestService.findFirstByIdBook2AndStatus(id, RequestStatus.ACEPTADA.toString());
                if(requestAcceptedToBook1 != null || requestAcceptedToBook2 != null) {
                    res.put("hasRequestAccepted", true);
                } else {
                    res.put("hasRequestAccepted", false);
                }
                
                if(!book.getUsername().equals(principal.getName())) {
                    if(this.userFavouriteBookService.findByUsernameAndBookId(principal.getName(), book.getId()) == null) {
                        res.put("isAdded", false);
                    } else {
                        res.put("isAdded", true);
                    }
                    Request request = this.requestService.findByUsername1AndIdBook2(principal.getName(), id);
                    if(request != null) {
                        res.put("alreadyRequest", true);
                    } else {
                        res.put("alreadyRequest", false);
                    }
                }
            }
            res.put("book", book);
            res.put("success", true);
        } catch (Exception e) {
            res.put("success", false);
        }

        return res;
    }

    @GetMapping(value = "/edit/{id}")
    public Map<String, Object> getBookByIdToEdit(@PathVariable("id") String id, Principal principal) {
        Map<String, Object> res = new HashMap<>();
        try {
            Book book = this.bookService.findBookById(id);
            res.put("book", book);
            res.put("success", true);
        } catch (Exception e) {
            res.put("success", false);
        }

        return res;
    }

    @GetMapping(value="/recommend")
    public Map<String, Object> findRecommendatedBooks(Principal principal, @RequestParam(name = "page", defaultValue = "0") Integer page){
        Map<String, Object> res = new HashMap<>();
        PageRequest pageRequest = PageRequest.of(page, 21);
        User user = this.userService.findByUsername(principal.getName());
        Map<Integer, List<Book>> map = this.searchService.recommendBooks(user, pageRequest);

        List<Book> books = map.values().stream().findFirst().orElse(new ArrayList<>());
        
        res.put("books", books);

        Integer numberOfPages = map.keySet().stream().findFirst().orElse(0);
        res.put("numTotalPages", numberOfPages);

        List<Boolean> isAdded = new ArrayList<>();
        for(Book book : books) {
            if(this.userFavouriteBookService.findByUsernameAndBookId(principal.getName(), book.getId()) == null) {
                isAdded.add(false);
            } else {
                isAdded.add(true);
            }
        }
        res.put("isAdded", isAdded);
        
        if(numberOfPages > 0) {
            List<Integer> pages = IntStream.rangeClosed(page-5 <= 0 ? 0 : page-5, page+5 >= numberOfPages-1 ? numberOfPages-1 : page+5).boxed().collect(Collectors.toList());
            res.put("pages", pages);
        } else {
            res.put("pages", new ArrayList<Integer>());
        }

        return res;
    }

    
}
