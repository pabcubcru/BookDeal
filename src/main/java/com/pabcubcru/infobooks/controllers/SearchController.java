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
import com.pabcubcru.infobooks.models.Image;
import com.pabcubcru.infobooks.models.Request;
import com.pabcubcru.infobooks.models.RequestStatus;
import com.pabcubcru.infobooks.models.Search;
import com.pabcubcru.infobooks.services.BookService;
import com.pabcubcru.infobooks.services.RequestService;
import com.pabcubcru.infobooks.services.SearchService;
import com.pabcubcru.infobooks.services.UserFavouriteBookService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @Autowired
    private BookService bookService;

    @GetMapping(value = {"/{page}/{query}", ""})
    public ModelAndView main() {
        ModelAndView model = new ModelAndView();
		model.setViewName("Main");
		return model;
    }

    private void validateSearch(Search search, BindingResult result) {
        if(search.getType().equals("rangeYears") || search.getType().equals("publicationYear")) {
            if(search.getStartYear() == null) {
                result.rejectValue("startYear", "Campo requerido", "Campo requerido");
            } else if(search.getStartYear() > LocalDate.now().getYear()) {
                result.rejectValue("startYear", "Debe ser anterior o igual al año actual", "Debe ser anterior o igual al año actual");
            } else if(search.getStartYear() < 0) {
                result.rejectValue("startYear", "Debe ser positivo", "Debe ser positivo");
            }
        }
        if(search.getType().equals("rangeYears")) {
            if(search.getFinishYear() == null) {
                result.rejectValue("finishYear", "Campo requerido", "Campo requerido");
            } else if(search.getFinishYear() < 0) {
                result.rejectValue("finishYear", "Debe ser positivo", "Debe ser positivo");
            } else if(search.getFinishYear() > LocalDate.now().getYear()) {
                result.rejectValue("finishYear", "Debe ser anterior o igual al año actual", "Debe ser anterior o igual al año actual");
            } else if(search.getFinishYear() < search.getStartYear()) {
                result.rejectValue("finishYear", "Debe ser posterior al año inicial", "Debe ser posterior al año inicial");
            }
        } else if(search.getType().equals("postalCode")) {
            if(search.getPostalCode() == null) {
                result.rejectValue("postalCode", "Campo requerido", "Campo requerido");
            } else if(search.getPostalCode() < 0) {
                result.rejectValue("postalCode", "Debe ser positivo", "Debe ser positivo");
            } else if(!search.getPostalCode().toString().matches("0[1-9][0-9]{3}|[1-4][0-9]{4}|5[0-2][0-9]{3}")) {
                result.rejectValue("postalCode", "Código postal no válido", "Código postal no válido");
            }
        } else {
            if(search.getText().isEmpty()) {
                result.rejectValue("text", "Campo requerido", "Campo requerido");
            }
        }
    }

    @PostMapping(value = "")
    public Map<String, Object> postSearch(@RequestBody @Valid Search search, BindingResult result, Principal principal) {
        Map<String, Object> res = new HashMap<>();

        this.validateSearch(search, result);

        if(result.hasErrors()) {
            res.put("errors", result.getAllErrors());
            res.put("success", false);
        } else {
            String query = "";
            if(search.getType().equals("rangeYears")) {
                query = search.getStartYear() + "-" + search.getFinishYear();
            } else if(search.getType().equals("publicationYear")) {
                query = ""+search.getStartYear();
            } else if(search.getType().equals("postalCode")) {
                query = ""+search.getPostalCode();
            } else {
                query = search.getText();
            }
            //res = this.searchBooks(query, principal, 0);
            res.put("query", query);
            res.put("success", true);
        }

        return res;
    }

    @GetMapping(value = "/titles/{query}")
    public Map<String, Object> searchBooks(@PathVariable("query") String query) {
        Map<String, Object> res = new HashMap<>();
        res.put("titles", this.searchService.findTitlesOfBooks(query));

        return res;
    }

    @GetMapping(value = "/q/{query}")
    public Map<String, Object> searchBooks(@PathVariable("query") String query, Principal principal, @RequestParam("page") Integer page) {
        Map<String, Object> res = new HashMap<>();
        PageRequest pageRequest = PageRequest.of(page, 21);
        List<List<String>> allBookImages = new ArrayList<>();

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

        for(Book b : books) {
            List<String> urlImages = new ArrayList<>();
            List<Image> images = this.bookService.findImagesByIdBook(b.getId());
            for(Image image : images) {
                urlImages.add(image.getUrlImage());
            }
            allBookImages.add(urlImages);
        }
        res.put("urlImages", allBookImages);

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
