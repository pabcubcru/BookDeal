package com.pabcubcru.bookdeal.controllers;

import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.validation.Valid;

import com.pabcubcru.bookdeal.models.Book;
import com.pabcubcru.bookdeal.models.Image;
import com.pabcubcru.bookdeal.models.Request;
import com.pabcubcru.bookdeal.models.RequestStatus;
import com.pabcubcru.bookdeal.models.Search;
import com.pabcubcru.bookdeal.services.BookService;
import com.pabcubcru.bookdeal.services.RequestService;
import com.pabcubcru.bookdeal.services.SearchService;
import com.pabcubcru.bookdeal.services.UserFavouriteBookService;

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

    @GetMapping(value = "")
    public ModelAndView main() {
        ModelAndView model = new ModelAndView();
        model.setViewName("Main");
        return model;
    }

    @GetMapping(value = "/{type}/{page}/{query}")
    public ModelAndView mainWithSecurity(@PathVariable("type") String type, Principal principal) {
        ModelAndView model = new ModelAndView();
        model.setViewName("errors/Error404");
        if (type.equals("province") || type.equals("rangePrices") || type.equals("rangeYears")
                || type.equals("postalCode") || type.equals("book") || type.equals("publicationYear")) {
            model.setViewName("Main");
            if (principal == null && !type.equals("book")) {
                model.setViewName("errors/Error403");
            }
        }
        return model;
    }

    private void validateSearch(Search search, BindingResult result) {
        if (search.getType().equals("rangeYears") || search.getType().equals("publicationYear")) {
            if (search.getNumber1() == null) {
                result.rejectValue("number1", "Campo requerido", "Campo requerido");
            } else if (search.getNumber1() > LocalDate.now().getYear()) {
                result.rejectValue("number1", "Debe ser anterior o igual al año actual",
                        "Debe ser anterior o igual al año actual");
            } else if (search.getNumber1() < 0) {
                result.rejectValue("number1", "Debe ser positivo", "Debe ser positivo");
            }
        }
        if (search.getType().equals("rangeYears")) {
            if (search.getNumber2() == null) {
                result.rejectValue("number2", "Campo requerido", "Campo requerido");
            } else if (search.getNumber2() < 0) {
                result.rejectValue("number2", "Debe ser positivo", "Debe ser positivo");
            } else if (search.getNumber2() > LocalDate.now().getYear()) {
                result.rejectValue("number2", "Debe ser anterior o igual al año actual",
                        "Debe ser anterior o igual al año actual");
            } else if (search.getNumber2() < search.getNumber1()) {
                result.rejectValue("number2", "Debe ser posterior al año inicial", "Debe ser posterior al año inicial");
            }
        } else if (search.getType().equals("postalCode")) {
            if (search.getText().isEmpty()) {
                result.rejectValue("text", "Campo requerido", "Campo requerido");
            } else if (Integer.parseInt(search.getText()) < 0) {
                result.rejectValue("text", "Debe ser positivo", "Debe ser positivo");
            } else if (!search.getText().matches("0[1-9][0-9]{3}|[1-4][0-9]{4}|5[0-2][0-9]{3}")) {
                result.rejectValue("text", "Código postal no válido", "Código postal no válido");
            }
        } else if (search.getType().equals("rangePrices")) {
            if (search.getNumber1() == null) {
                result.rejectValue("number1", "Campo requerido", "Campo requerido");
            } else if (search.getNumber1() < 0) {
                result.rejectValue("number1", "Debe ser positivo", "Debe ser positivo");
            }
            if (search.getNumber2() == null) {
                result.rejectValue("number2", "Campo requerido", "Campo requerido");
            } else if (search.getNumber2() < 0) {
                result.rejectValue("number2", "Debe ser positivo", "Debe ser positivo");
            }
        } else if (search.getType().equals("book")) {
            if (search.getText().isEmpty()) {
                result.rejectValue("text", "Campo requerido", "Campo requerido");
            } else if (search.getText().length() < 3) {
                result.rejectValue("text", "Introduce al menos 3 carácteres", "Introduce al menos 3 carácteres");
            } else if (search.getText().length() > 80) {
                result.rejectValue("text", "No puede superar los 80 carácteres", "No puede superar los 80 carácteres");
            }
        }
    }

    @PostMapping(value = "")
    public Map<String, Object> postSearch(@RequestBody @Valid Search search, BindingResult result,
            Principal principal) {
        Map<String, Object> res = new HashMap<>();

        this.validateSearch(search, result);

        if (result.hasErrors()) {
            res.put("errors", result.getAllErrors());
            res.put("success", false);
        } else {
            String query = "";
            if (search.getType().equals("rangeYears")) {
                query = search.getNumber1() + "-" + search.getNumber2();
            } else if (search.getType().equals("publicationYear")) {
                query = "" + search.getNumber1();
            } else if (search.getType().equals("postalCode")) {
                query = search.getText();
            } else if (search.getType().equals("rangePrices")) {
                query = search.getNumber1() + "€-" + search.getNumber2() + "€";
            } else {
                query = search.getText();
            }
            res.put("query", query);
            res.put("success", true);
            this.searchService.saveSearch(search, principal != null ? principal.getName() : null);
        }
        return res;
    }

    @GetMapping(value = "/titles/{query}")
    public Map<String, Object> getSuggestions(@PathVariable("query") String query) {
        Map<String, Object> res = new HashMap<>();
        res.put("titles", this.searchService.findTitlesOfBooks(query));

        return res;
    }

    @GetMapping(value = "/q/{type}/{query}")
    public Map<String, Object> searchBooks(@PathVariable("query") String query, @PathVariable("type") String type,
            Principal principal, @RequestParam("page") Integer page) {
        Map<String, Object> res = new HashMap<>();
        PageRequest pageRequest = PageRequest.of(page, 21);
        List<List<String>> allBookImages = new ArrayList<>();

        String username = principal != null ? principal.getName() : null;

        Map<Integer, List<Book>> map = this.searchService.searchBook(query, pageRequest, username, type);
        List<Book> pageOfBooks = map.values().stream().findFirst().orElse(new ArrayList<>());
        Integer numberOfPages = map.keySet().stream().findFirst().orElse(0);
        res.put("searchResult", true);
        if (numberOfPages == 0) {
            res.put("searchResult", false);
        }
        List<Book> books = new ArrayList<>();
        for (Book b : pageOfBooks) {
            Request requestAcceptedToBook1 = this.requestService.findFirstByIdBook1AndStatus(b.getId(),
                    RequestStatus.ACEPTADA.toString());
            Request requestAcceptedToBook2 = this.requestService.findFirstByIdBook2AndStatus(b.getId(),
                    RequestStatus.ACEPTADA.toString());
            if (requestAcceptedToBook1 == null && requestAcceptedToBook2 == null) {
                books.add(b);
            }
        }
        if (principal != null) {
            List<Boolean> isAdded = new ArrayList<>();
            for (Book book : books) {
                if (this.userFavouriteBookService.findByUsernameAndBookId(principal.getName(), book.getId()) == null) {
                    isAdded.add(false);
                } else {
                    isAdded.add(true);
                }
            }
            res.put("isAdded", isAdded);
            res.put("page", page);
            res.put("search", this.searchService.findByUsername(principal.getName()));
        }

        for (Book b : books) {
            List<String> urlImages = new ArrayList<>();
            List<Image> images = this.bookService.findImagesByIdBook(b.getId());
            for (Image image : images) {
                urlImages.add(image.getUrlImage());
            }
            allBookImages.add(urlImages);
        }
        res.put("urlImages", allBookImages);

        res.put("books", books);

        res.put("numTotalPages", numberOfPages);
        res.put("pages", new ArrayList<Integer>());
        if (numberOfPages > 0) {
            List<Integer> pages = IntStream
                    .rangeClosed(page - 5 <= 0 ? 0 : page - 5,
                            page + 5 >= numberOfPages - 1 ? numberOfPages - 1 : page + 5)
                    .boxed().collect(Collectors.toList());
            res.put("pages", pages);
        }

        return res;
    }

    @GetMapping(value = "/last")
    public Map<String, Object> findLastSearch(Principal principal) {
        Map<String, Object> res = new HashMap<>();
        if (principal != null) {
            res.put("search", this.searchService.findByUsername(principal.getName()));
        }

        return res;
    }

}
