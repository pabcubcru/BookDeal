package com.pabcubcru.infobooks.service;

import java.util.List;

import com.pabcubcru.infobooks.models.Authorities;
import com.pabcubcru.infobooks.services.AuthoritiesService;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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

    @Test
    public void shouldSave() throws Exception {
        List<Authorities> authorities = this.authoritiesService.findByUsername("pablo123");
        Integer numberOfAuthoritiesBefore = authorities.size();

        Authorities authority = new Authorities();
        authority.setUsername("pablo123");
        authority.setAuthority("test");
        this.authoritiesService.save(authority);

        authorities = this.authoritiesService.findByUsername("pablo123");
        Integer numberOfAuthoritiesAfter = authorities.size();
        Assertions.assertThat(numberOfAuthoritiesAfter).isEqualTo(numberOfAuthoritiesBefore+1);
    }
    
}
