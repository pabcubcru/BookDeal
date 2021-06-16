package com.pabcubcru.bookdeal.controller;

import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.pabcubcru.bookdeal.models.User;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final String USER_TEST_001 = "test001";

    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));
    public static final Charset ISO_8859_1 = Charset.forName("ISO-8859-1");
    public static final Charset UTF_8 = Charset.forName("UTF-8");

    @ParameterizedTest
    @CsvFileSource(resources = "../csv/controllers/Users-success.csv", encoding = "utf-8", numLinesToSkip = 1, delimiterString = ";")
    @Order(1)
    @WithAnonymousUser
    public void testProcessRegisterFormSuccess(String name, String email, String phone, LocalDate birthDate,
            String province, String postCode, String genres, String username, String password, Boolean enabled,
            Boolean accept, String confirmPassword) throws Exception {
        User user = new User(name, email, phone, birthDate, province, postCode, genres, username, password, enabled,
                accept, confirmPassword);
        user.setBirthDate(null);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(user);
        requestJson = requestJson.replace("null", "\"" + birthDate.format(DateTimeFormatter.ISO_LOCAL_DATE) + "\"");

        mockMvc.perform(post("/register").contentType(APPLICATION_JSON_UTF8).content(requestJson))
                .andExpect(status().isOk()).andExpect(jsonPath("$.success").value(true));

    }

    @ParameterizedTest
    @CsvFileSource(resources = "../csv/controllers/Users-success.csv", encoding = "utf-8", numLinesToSkip = 1, delimiterString = ";")
    @Order(2)
    @WithMockUser(value = USER_TEST_001, authorities = "user")
    public void testProcessEditFormSuccess(String name, String email, String phone, LocalDate birthDate,
            String province, String postCode, String genres, String username, String password, Boolean enabled,
            Boolean accept, String confirmPassword) throws Exception {

        User user = new User(name, email, phone, birthDate, province, postCode, genres, username, password, enabled,
                accept, confirmPassword);
        user.setId("test" + username);
        user.setUsername(USER_TEST_001);
        user.setBirthDate(null);
        user.setEmail("e" + email);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(user);
        requestJson = requestJson.replace("null", "\"" + birthDate.format(DateTimeFormatter.ISO_LOCAL_DATE) + "\"");

        mockMvc.perform(put("/user/test001/edit").contentType(APPLICATION_JSON_UTF8).content(requestJson))
                .andExpect(status().isOk()).andExpect(jsonPath("$.success").value(true));
    }

    @ParameterizedTest
    @CsvFileSource(resources = "../csv/controllers/Users-error.csv", encoding = "utf-8", numLinesToSkip = 1, delimiterString = ";")
    @Order(3)
    @WithAnonymousUser
    public void testProcessRegisterFormHasErrors(String name, String email, String phone, LocalDate birthDate,
            String province, String postCode, String genres, String username, String password, Boolean enabled,
            Boolean accept, String confirmPassword) throws Exception {

        User user = new User(name, email, phone, birthDate, province, postCode, genres, username, password, enabled,
                accept, confirmPassword);
        user.setBirthDate(null);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(user);
        requestJson = requestJson.replace("null", "\"" + birthDate.format(DateTimeFormatter.ISO_LOCAL_DATE) + "\"");

        this.mockMvc.perform(post("/register").contentType(APPLICATION_JSON_UTF8).content(requestJson))
                .andExpect(status().isOk()).andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errors").isNotEmpty());
    }

    @ParameterizedTest
    @CsvFileSource(resources = "../csv/controllers/Users-error.csv", encoding = "utf-8", numLinesToSkip = 1, delimiterString = ";")
    @Order(4)
    @WithMockUser(value = USER_TEST_001, authorities = "user")
    public void testProcessEditFormHasErrors(String name, String email, String phone, LocalDate birthDate,
            String province, String postCode, String genres, String username, String password, Boolean enabled,
            Boolean accept, String confirmPassword) throws Exception {

        User user = new User(name, email, phone, birthDate, province, postCode, genres, username, password, enabled,
                accept, confirmPassword);
        user.setId("test" + username);
        user.setUsername(USER_TEST_001);
        user.setBirthDate(null);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(user);
        requestJson = requestJson.replace("null", "\"" + birthDate.format(DateTimeFormatter.ISO_LOCAL_DATE) + "\"");

        this.mockMvc.perform(put("/user/test001/edit").contentType(APPLICATION_JSON_UTF8).content(requestJson))
                .andExpect(status().isOk()).andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errors").isNotEmpty());
    }

    @Test
    @Order(5)
    @WithMockUser(value = USER_TEST_001, authorities = "user")
    public void testMain() throws Exception {
        this.mockMvc.perform(get("/")).andExpect(status().isOk()).andExpect(view().name("Main"));
    }

    @Test
    @Order(6)
    @WithMockUser(value = "administrador", authorities = {"user", "admin"})
    public void testGetPrincipalAdmin() throws Exception {
        this.mockMvc.perform(get("/user/principal")).andExpect(status().isOk())
                .andExpect(jsonPath("$.isLogged").value(true)).andExpect(jsonPath("$.isAdmin").value(true));
    }

    @Test
    @Order(7)
    @WithMockUser(value = USER_TEST_001, authorities = "user")
    public void testGetPrincipalUser() throws Exception {
        this.mockMvc.perform(get("/user/principal")).andExpect(status().isOk())
                .andExpect(jsonPath("$.isLogged").value(true)).andExpect(jsonPath("$.isAdmin").value(false));
    }

    @Test
    @Order(8)
    @WithMockUser(value = USER_TEST_001, authorities = "user")
    public void testGetProvinces() throws Exception {
        MockHttpServletResponse response = this.mockMvc.perform(get("/provinces")).andExpect(status().isOk())
                .andReturn().getResponse();

        String content = response.getContentAsString();

        byte[] ptext = content.getBytes(ISO_8859_1);
        String res = new String(ptext, UTF_8);

        assertThat(res).contains("\"Álava\",\"Albacete\",\"Alicante\",\"Almería\",\"Asturias\",\"Ávila\",\"Badajoz\","
                + "\"Barcelona\",\"Burgos\",\"Cáceres\",\"Cádiz\",\"Cantabria\",\"Castellón\",\"CiudadReal\",\"Córdoba\",\"La_Coruña\",\"Cuenca\",\"Gerona\","
                + "\"Granada\",\"Guadalajara\",\"Guipúzcoa\",\"Huelva\",\"Huesca\",\"Islas_Baleares\",\"Jaén\",\"León\",\"Lérida\",\"Lugo\",\"Madrid\",\"Málaga\","
                + "\"Murcia\",\"Navarra\",\"Orense\",\"Palencia\",\"Las_Palmas\",\"Pontevedra\",\"LaRioja\",\"Salamanca\",\"Segovia\",\"Sevilla\",\"Soria\",\"Tarragona\","
                + "\"Tenerife\",\"Teruel\",\"Toledo\",\"Valencia\",\"Valladolid\",\"Vizcaya\",\"Zamora\",\"Zaragoza\"");
    }

    @Test
    @Order(9)
    @WithMockUser(value = USER_TEST_001, authorities = "user")
    public void testGetUsername() throws Exception {
        this.mockMvc.perform(get("/user/get-username")).andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(USER_TEST_001));
    }

    @Test
    @Order(10)
    @WithMockUser(value = "test002", authorities = "user")
    public void testGetUser() throws Exception {
        this.mockMvc.perform(get("/user/test002")).andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.user.username").value("test002"))
                .andExpect(jsonPath("$.user.email").value("test002@us.es"));
    }
}
