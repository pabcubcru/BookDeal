package com.pabcubcru.bookdeal.service;

import java.util.List;

import com.pabcubcru.bookdeal.models.Authorities;
import com.pabcubcru.bookdeal.services.AuthoritiesService;

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
public class AuthoritiesServiceTests {

    @Autowired
    private AuthoritiesService authoritiesService;

    @Test
    public void shouldFindByUsername() throws Exception {
        List<Authorities> authorities = this.authoritiesService.findByUsername("juan1234");
        Assertions.assertThat(authorities.get(0).getAuthority()).isEqualTo("user");
    }

    @Test
    public void shouldNotFindByUsername() throws Exception {
        List<Authorities> authorities = this.authoritiesService.findByUsername("authorities-userFake");
        Assertions.assertThat(authorities.size()).isEqualTo(0);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "../csv/services/Authorities.csv", encoding = "utf-8", numLinesToSkip = 1, delimiterString = ";")
    public void shouldSave(String username, String authority) throws Exception {
        List<Authorities> authorities = this.authoritiesService.findByUsername(username);
        Integer numberOfAuthoritiesBefore = authorities.size();

        Authorities auth = new Authorities(username, authority);
        this.authoritiesService.save(auth);

        authorities = this.authoritiesService.findByUsername(username);
        Integer numberOfAuthoritiesAfter = authorities.size();
        Assertions.assertThat(numberOfAuthoritiesAfter).isEqualTo(numberOfAuthoritiesBefore + 1);
    }

}
