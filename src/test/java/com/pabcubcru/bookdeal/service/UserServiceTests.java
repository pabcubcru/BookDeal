package com.pabcubcru.bookdeal.service;

import java.time.LocalDate;

import com.pabcubcru.bookdeal.models.User;
import com.pabcubcru.bookdeal.services.UserService;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class UserServiceTests {

    @Autowired
    private UserService userService;

    @Test
    public void shouldFindUserById() throws Exception {
        User user = this.userService.findUserById("userTest-pablo123");
        Assertions.assertThat(user.getUsername()).isEqualTo("pablo123");
    }

    @Test
    public void shouldFindUserByUsername() throws Exception {
        User user = this.userService.findByUsername("juan1234");
        Assertions.assertThat(user.getId()).isEqualTo("userTest-juan1234");
        Assertions.assertThat(user.getEmail()).isEqualTo("juan1234@us.es");
    }

    @Test
    public void shouldExistsUserWithSameEmail() throws Exception {
        Boolean exists = this.userService.existUserWithSameEmail("pablo123@us.es");
        Assertions.assertThat(exists).isEqualTo(true);
    }

    @Test
    public void shouldNotExistsUserWithSameEmail() throws Exception {
        Boolean exists = this.userService.existUserWithSameEmail("email@us.es");
        Assertions.assertThat(exists).isEqualTo(false);
    }

    @Test
    public void shouldExistsUserWithSameUsername() throws Exception {
        Boolean exists = this.userService.existUserWithSameUsername("pablo123");
        Assertions.assertThat(exists).isEqualTo(true);
    }

    @Test
    public void shouldNotExistsUserWithSameUsername() throws Exception {
        Boolean exists = this.userService.existUserWithSameUsername("userFake");
        Assertions.assertThat(exists).isEqualTo(false);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "../csv/services/Users.csv", encoding = "utf-8", numLinesToSkip = 1, delimiterString = ";")
    public void shouldSaveUser(String name, String email, String phone, LocalDate birthDate, String province,
            String postCode, String genres, String username, String password, Boolean enabled, Boolean accept,
            String confirmPassword) throws Exception {
        User user = new User(name, email, phone, birthDate, province, postCode, genres, username, password, enabled,
                accept, confirmPassword);
        this.userService.save(user, true);

        user = this.userService.findByUsername(username);
        Assertions.assertThat(user.getUsername()).isEqualTo(username);
        Assertions.assertThat(user.getEmail()).isEqualTo(email);
    }

    @Test
    public void shouldDeleteUser() throws Exception {
        User user = this.userService.findByUsername("test001");

        Assertions.assertThat(user).isNotNull();

        this.userService.delete(user);

        user = this.userService.findByUsername("test001");

        Assertions.assertThat(user).isNull();
    }

}
