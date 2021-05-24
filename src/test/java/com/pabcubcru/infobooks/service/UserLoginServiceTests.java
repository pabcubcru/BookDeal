package com.pabcubcru.infobooks.service;

import java.util.List;
import java.util.stream.Collectors;

import com.pabcubcru.infobooks.services.UserLoginService;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class UserLoginServiceTests {

    @Autowired
    private UserLoginService userLoginService;

    @Test
    public void shouldLoadByUsername() throws Exception {
        String username = "pablo123";
        UserDetails userDetails = this.userLoginService.loadUserByUsername(username);

        Assertions.assertThat(userDetails).isNotNull();
        Assertions.assertThat(userDetails.getUsername()).isEqualTo(username);

        List<String> authorities = userDetails.getAuthorities().stream().map(x -> x.getAuthority())
                .collect(Collectors.toList());
        Assertions.assertThat(authorities).contains("user");
    }

}
