package com.pabcubcru.infobooks.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.pabcubcru.infobooks.models.Book;
import com.pabcubcru.infobooks.models.Search;
import com.pabcubcru.infobooks.models.User;
import com.pabcubcru.infobooks.services.BookService;
import com.pabcubcru.infobooks.services.SearchService;
import com.pabcubcru.infobooks.services.UserService;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class SearchServiceTests {

    @Autowired
    private SearchService searchService;

    @Autowired
    private BookService bookService;

    @Autowired
    private UserService userService;

    @Test
    public void shouldFindTitlesOfBooks() throws Exception {
        List<String> suggestions = this.searchService.findTitlesOfBooks("Sapiens");

        Assertions.assertThat(suggestions).isNotEmpty();
    }

    @Test
    public void shouldSearchBookTextType() throws Exception {
        Map<Integer, List<Book>> map = this.searchService.searchBook("Sapiens", PageRequest.of(0, 21), "pablo123",
                "book");
        
        List<String> books = map.values().stream().findFirst().orElse(new ArrayList<>()).stream().map(x -> x.getId()).collect(Collectors.toList());

        Book sapiens = this.bookService.findBookById("book-003");

        Assertions.assertThat(books).isNotEmpty();
        Assertions.assertThat(books).contains(sapiens.getId());
    }

    @Test
    public void shouldSearchBookPublicationYearType() throws Exception {
        Map<Integer, List<Book>> map = this.searchService.searchBook("2014", PageRequest.of(0, 21), "pablo123",
                "publicationYear");
        
        List<String> books = map.values().stream().findFirst().orElse(new ArrayList<>()).stream().map(x -> x.getId()).collect(Collectors.toList());

        Book sapiens = this.bookService.findBookById("book-003");

        Assertions.assertThat(books).isNotEmpty();
        Assertions.assertThat(books).contains(sapiens.getId());
    }

    @Test
    public void shouldSearchBookRangeYearsType() throws Exception {
        Map<Integer, List<Book>> map = this.searchService.searchBook("2010-2014", PageRequest.of(0, 21), "pablo123",
                "rangeYears");
        
        List<String> books = map.values().stream().findFirst().orElse(new ArrayList<>()).stream().map(x -> x.getId()).collect(Collectors.toList());

        Book sapiens = this.bookService.findBookById("book-003");

        Assertions.assertThat(books).isNotEmpty();
        Assertions.assertThat(books).contains(sapiens.getId());
    }

    @Test
    public void shouldSearchBookPostalCodeType() throws Exception {
        Map<Integer, List<Book>> map = this.searchService.searchBook("41012", PageRequest.of(0, 21), "pablo123",
                "postalCode");
        
        List<String> books = map.values().stream().findFirst().orElse(new ArrayList<>()).stream().map(x -> x.getId()).collect(Collectors.toList());

        Book sapiens = this.bookService.findBookById("book-003");

        Assertions.assertThat(books).isNotEmpty();
        Assertions.assertThat(books).contains(sapiens.getId());
    }

    @Test
    public void shouldSearchBookProvinceType() throws Exception {
        Map<Integer, List<Book>> map = this.searchService.searchBook("Sevilla", PageRequest.of(0, 21), "pablo123",
                "province");
        
        List<String> books = map.values().stream().findFirst().orElse(new ArrayList<>()).stream().map(x -> x.getId()).collect(Collectors.toList());

        Book sapiens = this.bookService.findBookById("book-003");

        Assertions.assertThat(books).isNotEmpty();
        Assertions.assertThat(books).contains(sapiens.getId());
    }

    @Test
    public void shouldSearchBookRangePricesType() throws Exception {
        Map<Integer, List<Book>> map = this.searchService.searchBook("5€-20€", PageRequest.of(0, 21), "pablo123",
                "rangePrices");
        
        List<String> books = map.values().stream().findFirst().orElse(new ArrayList<>()).stream().map(x -> x.getId()).collect(Collectors.toList());

        Book sapiens = this.bookService.findBookById("book-003");

        Assertions.assertThat(books).isNotEmpty();
        Assertions.assertThat(books).contains(sapiens.getId());
    }

    @Test
    public void shouldRecommendBooks() throws Exception {
        User user = this.userService.findByUsername("pablo123");
        Map<Integer, List<Book>> map = this.searchService.recommendBooks(user, PageRequest.of(0, 21));

        List<String> books = map.values().stream().findFirst().orElse(new ArrayList<>()).stream().map(x -> x.getId()).collect(Collectors.toList());

        Book sapiens = this.bookService.findBookById("book-003");

        Assertions.assertThat(books).isNotEmpty();
        Assertions.assertThat(books).contains(sapiens.getId()); 
    }

    @Test
    public void shouldSaveSearchAndFindByUsername() throws Exception {
        String username = "pablo123";

        Search search = new Search();
        search.setUsername(username);
        search.setType("book");
        search.setText("search");

        this.searchService.saveSearch(search, username);

        Search lastSearch = this.searchService.findByUsername(username);

        Assertions.assertThat(lastSearch.getUsername()).isEqualTo(username);
        Assertions.assertThat(lastSearch.getText()).isEqualTo(search.getText());
        Assertions.assertThat(lastSearch.getType()).isEqualTo(search.getType());        
    }

}
