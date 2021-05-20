package com.pabcubcru.infobooks.controller;

import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.pabcubcru.infobooks.models.User;
import com.pabcubcru.infobooks.services.AuthoritiesService;
import com.pabcubcru.infobooks.services.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @MockBean
    private UserService userService;

    @MockBean
    private AuthoritiesService authoritiesService;

    @Autowired
    private MockMvc mockMvc;

    private User user1;

    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

    @BeforeEach
    public void setup() {
        user1 = new User();

        user1.setName("Test");
        user1.setEmail("email@email.com");
        user1.setPhone("+34654987321");
        user1.setBirthDate(LocalDate.of(1998, 11, 23));
        user1.setProvince("Province");
        user1.setPostCode("41012");
        user1.setUsername("username");
        user1.setPassword(new BCryptPasswordEncoder().encode("password123"));
        user1.setEnabled(true);
        user1.setGenres("Religión,Gastronomía,Cocina");

        BDDMockito.given(this.userService.findByUsername("Test")).willReturn(user1);
        BDDMockito.given(this.userService.existUserWithSameEmail("email@email.com")).willReturn(false);
        BDDMockito.given(this.userService.existUserWithSameUsername("username")).willReturn(false);
    }

    /*@WithAnonymousUser
    @Test
    public void testProcessRegisterFormHasError() throws Exception {
        //String userJson = "{\n'id\n':null,\n'name\n':\n'Test\n',\n'email\n':\n'email@email.com\n',\n'phone\n':\n'+34654987321\n',\n'birthDate\n':\n'1998-01-01\n',\n'username\n':\n'username\n',\n'password\n':\n'pablo123\n',\n'province\n':\n'Badajoz\n',\n'postCode\n':\n'41012\n',\n'genres\n':\n'Autoayuda,Diseño,Esoterismo\n',\n'accept\n':true,\n'confirmPassword\n':\n'pablo123\n'}";

        User u = new User();
        u.setName("Test");
        u.setEmail("email@email.com");
        u.setPhone("+34654987321");
        u.setProvince("Province");
        //u.setBirthDate(LocalDate.of(1998, 01, 01));
        u.setPostCode("41012");
        u.setUsername("username");
        u.setPassword("password123");
        u.setEnabled(true);
        u.setAccept(true);
        u.setConfirmPassword("password123");
        u.setGenres("Religión,Gastronomía,Cocina");

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson=ow.writeValueAsString(u);

        mockMvc.perform(post("/register")
        .contentType(APPLICATION_JSON_UTF8)
        .content(requestJson))
        .andExpect(status().is2xxSuccessful());
        
    }

    @WithAnonymousUser
    @Test
    public void testProcessRegisterSuccess() throws Exception {
        String userJson = "{\'name\':\'Test\',\'email\':\'email@email.com\',\'phone\':\'+34654987321\',\'birthDate\':\'1998-01-01\',\'username\':\'username\',\'password\':\'pablo123\',\'province\':\'Badajoz\',\'postCode\':\'41012\',\'genres\':\'Autoayuda,Diseño,Esoterismo\',\'accept\':true,\'confirmPassword\':\'pablo123\'}";

        RequestBuilder requestBuilder = post("/register")
        .accept(MediaType.APPLICATION_JSON).content(userJson)
        .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();
        System.out.println(response);
        
        Assertions.assertThat(response.getStatus()).isEqualTo(200);
    }*/
}
