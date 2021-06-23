package com.pabcubcru.bookdeal.service;

import com.pabcubcru.bookdeal.models.UserFavouriteBook;
import com.pabcubcru.bookdeal.services.UserFavouriteBookService;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class UserFavouriteBookServiceTests {

    @Autowired
    private UserFavouriteBookService userFavouriteBookService;

    @Test
    public void shouldFindAllByUsername() throws Exception {
        PageRequest pageRequest = PageRequest.of(1, 21);
        Page<UserFavouriteBook> ufbooks = this.userFavouriteBookService.findAllByUsername("juan1234", pageRequest);

        Assertions.assertThat(ufbooks.getTotalElements()).isEqualTo(1L);

        ufbooks = this.userFavouriteBookService.findAllByUsername("test001", pageRequest);
        Assertions.assertThat(ufbooks.getTotalElements()).isEqualTo(1L);
    }

    @Test
    public void shouldFindByUsernameAndBookId() throws Exception {
        UserFavouriteBook ufbook = this.userFavouriteBookService.findByUsernameAndBookId("test002", "book-001");
        Assertions.assertThat(ufbook.getId()).isEqualTo("ufb-001");

        ufbook = this.userFavouriteBookService.findByUsernameAndBookId("juan1234", "book-001");
        Assertions.assertThat(ufbook.getId()).isEqualTo("ufb-003");
    }

    @Test
    public void shouldDelete() throws Exception {
        UserFavouriteBook ufbook = this.userFavouriteBookService.findByUsernameAndBookId("test001", "booktest");
        Assertions.assertThat(ufbook).isNotNull();

        this.userFavouriteBookService.delete(ufbook);
        ufbook = this.userFavouriteBookService.findByUsernameAndBookId("test001", "booktest");
        Assertions.assertThat(ufbook).isNull();
    }

    @ParameterizedTest
    @CsvFileSource(resources = "../csv/services/UserFavouritesBooks.csv", encoding = "utf-8", numLinesToSkip = 1, delimiterString = ";")
    public void shouldSave(String username, String bookId) throws Exception {
        PageRequest pageRequest = PageRequest.of(1, 21);
        Long numFavouritesBefore = this.userFavouriteBookService.findAllByUsername(username, pageRequest).getTotalElements();
        UserFavouriteBook ufb = new UserFavouriteBook(username, bookId);
        this.userFavouriteBookService.save(ufb);

        Long numFavouritesAfter = this.userFavouriteBookService.findAllByUsername(username, pageRequest).getTotalElements();
        Assertions.assertThat(numFavouritesAfter).isEqualTo(numFavouritesBefore + 1L);
    }

}
