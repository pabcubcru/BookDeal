package com.pabcubcru.bookdeal.controllers;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.pabcubcru.bookdeal.models.Book;
import com.pabcubcru.bookdeal.models.Image;
import com.pabcubcru.bookdeal.models.UserFavouriteBook;
import com.pabcubcru.bookdeal.services.BookService;
import com.pabcubcru.bookdeal.services.UserFavouriteBookService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping(value = "/favourites")
public class UserFavouriteBookController {

    @Autowired
    private UserFavouriteBookService userFavouriteBookService;

    @Autowired
    private BookService bookService;

    @GetMapping(value = { "/{page}" })
    public ModelAndView main() {
        ModelAndView model = new ModelAndView();
        model.setViewName("Main");
        return model;
    }

    public List<String> getUrlsImagesFromBooks(List<Book> books) {
        List<String> allBookImages = new ArrayList<>();
        for (Book b : books) {
            Image image = this.bookService.findByIdBookAndPrincipalTrue(b.getId());
            allBookImages.add(image.getUrlImage());
        }
        return allBookImages;
    }

    @GetMapping(value = "/all")
    public Map<String, Object> findAllByUsername(Principal principal,
            @RequestParam(name = "page", defaultValue = "0") Integer page) {
        Map<String, Object> res = new HashMap<>();
        List<String> idBooks = new ArrayList<>();
        PageRequest pageRequest = PageRequest.of(page, 12);

        Page<UserFavouriteBook> pageOfBooks = this.userFavouriteBookService.findAllByUsername(principal.getName(),
                pageRequest);
        List<UserFavouriteBook> ufbooks = pageOfBooks.getContent();
        ufbooks.stream().forEach(x -> idBooks.add(x.getBookId()));

        List<Book> books = this.bookService.findByIds(idBooks);

        res.put("books", books);

        List<String> allBookImages = this.getUrlsImagesFromBooks(books);
        res.put("urlImages", allBookImages);

        Integer numberOfPages = pageOfBooks.getTotalPages();
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

    @GetMapping(value = "/{id}/add")
    public Map<String, Object> addFavouriteBook(@PathVariable("id") String id, Principal principal) {
        Map<String, Object> res = new HashMap<>();

        try {
            if (this.userFavouriteBookService.findByUsernameAndBookId(principal.getName(), id) == null) {
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
        if (ufbook != null) {
            this.userFavouriteBookService.delete(ufbook);
        }
    }

}
