package com.pabcubcru.bookdeal.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.pabcubcru.bookdeal.models.Book;
import com.pabcubcru.bookdeal.models.Search;
import com.pabcubcru.bookdeal.models.User;
import com.pabcubcru.bookdeal.services.BookService;
import com.pabcubcru.bookdeal.services.SearchService;
import com.pabcubcru.bookdeal.services.UserService;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
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

        List<String> books = map.values().stream().findFirst().orElse(new ArrayList<>()).stream().map(x -> x.getId())
                .collect(Collectors.toList());

        Assertions.assertThat(books).isNotEmpty();
    }

    @Test
    public void shouldSearchBookPublicationYearType() throws Exception {
        Map<Integer, List<Book>> map = this.searchService.searchBook("2014", PageRequest.of(0, 21), "pablo123",
                "publicationYear");

        List<String> books = map.values().stream().findFirst().orElse(new ArrayList<>()).stream().map(x -> x.getId())
                .collect(Collectors.toList());

        Assertions.assertThat(books).isNotEmpty();
    }

    @Test
    public void shouldSearchBookRangeYearsType() throws Exception {
        Map<Integer, List<Book>> map = this.searchService.searchBook("2013-2014", PageRequest.of(0, 21), "pablo123",
                "rangeYears");

        List<String> books = map.values().stream().findFirst().orElse(new ArrayList<>()).stream().map(x -> x.getId())
                .collect(Collectors.toList());

        Assertions.assertThat(books).isNotEmpty();
    }

    @Test
    public void shouldSearchBookPostalCodeType() throws Exception {
        Map<Integer, List<Book>> map = this.searchService.searchBook("41012", PageRequest.of(0, 21), "pablo123",
                "postalCode");

        List<String> books = map.values().stream().findFirst().orElse(new ArrayList<>()).stream().map(x -> x.getId())
                .collect(Collectors.toList());

        Assertions.assertThat(books).isNotEmpty();
    }

    @Test
    public void shouldSearchBookProvinceType() throws Exception {
        Map<Integer, List<Book>> map = this.searchService.searchBook("Sevilla", PageRequest.of(0, 21), "pablo123",
                "province");

        List<String> books = map.values().stream().findFirst().orElse(new ArrayList<>()).stream().map(x -> x.getId())
                .collect(Collectors.toList());

        Assertions.assertThat(books).isNotEmpty();
    }

    @Test
    public void shouldSearchBookRangePricesType() throws Exception {
        Map<Integer, List<Book>> map = this.searchService.searchBook("9€-11€", PageRequest.of(0, 21), "pablo123",
                "rangePrices");

        List<String> books = map.values().stream().findFirst().orElse(new ArrayList<>()).stream().map(x -> x.getId())
                .collect(Collectors.toList());

        Assertions.assertThat(books).isNotEmpty();
    }

    @Test
    public void shouldRecommendBooks() throws Exception {
        User user = this.userService.findByUsername("pablo123");
        Map<Integer, List<Book>> map = this.searchService.recommendBooks(user, PageRequest.of(0, 21));

        List<String> books = map.values().stream().findFirst().orElse(new ArrayList<>()).stream().map(x -> x.getId())
                .collect(Collectors.toList());

        Assertions.assertThat(books).isNotEmpty();
    }

    @ParameterizedTest
    @CsvFileSource(resources = "../csv/services/Searchs.csv", encoding = "utf-8", numLinesToSkip = 1, delimiterString = ";")
    public void shouldSaveSearchAndFindByUsername(String text, Integer number1, Integer number2, String type,
            String username) throws Exception {

        Search search = new Search(text, number1, number2, type, username);
        this.searchService.saveSearch(search, username);

        Search lastSearch = this.searchService.findByUsername(username);

        Assertions.assertThat(lastSearch.getUsername()).isEqualTo(username);
        Assertions.assertThat(lastSearch.getType()).isEqualTo(search.getType());
    }

}
