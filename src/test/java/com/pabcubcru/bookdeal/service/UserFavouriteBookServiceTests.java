package com.pabcubcru.bookdeal.service;

import com.pabcubcru.bookdeal.models.UserFavouriteBook;
import com.pabcubcru.bookdeal.services.UserFavouriteBookService;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
        PageRequest pageRequest = PageRequest.of(1, 20);
        Page<UserFavouriteBook> ufbooks = this.userFavouriteBookService.findAllByUsername("juan1234", pageRequest);

        Assertions.assertThat(ufbooks.getTotalElements()).isEqualTo(1L);

        ufbooks = this.userFavouriteBookService.findAllByUsername("pablo123", pageRequest);
        Assertions.assertThat(ufbooks.getTotalElements()).isEqualTo(2L);
    }

    @Test
    public void shouldFindByUsernameAndBookId() throws Exception {
        UserFavouriteBook ufbook = this.userFavouriteBookService.findByUsernameAndBookId("pablo123", "book-001");
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

    @Test
    public void shouldSave() throws Exception {
        UserFavouriteBook ufb = new UserFavouriteBook();
        ufb.setBookId("booktest1");
        ufb.setId("ufbtest");
        ufb.setUsername("test");
        this.userFavouriteBookService.save(ufb);

        UserFavouriteBook ufbook = this.userFavouriteBookService.findByUsernameAndBookId("test", "booktest1");
        Assertions.assertThat(ufbook).isNotNull();
        Assertions.assertThat(ufbook.getId()).isEqualTo("ufbtest");
    }

}
