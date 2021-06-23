package com.pabcubcru.bookdeal.controller;

import java.nio.charset.Charset;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.pabcubcru.bookdeal.models.Search;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class SearchControllerTests {

    @Autowired
    private MockMvc mockMvc;

    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));
    public static final Charset ISO_8859_1 = Charset.forName("ISO-8859-1");
    public static final Charset UTF_8 = Charset.forName("UTF-8");

    @Test
    @Order(1)
    @WithMockUser(value = "test001", authorities = "user")
    public void testMain() throws Exception {
        this.mockMvc.perform(get("/search")).andExpect(status().isOk()).andExpect(view().name("Main"));
    }

    @Test
    @Order(2)
    @WithMockUser(value = "test001", authorities = "user")
    public void testMainWithSecurity() throws Exception {
        this.mockMvc.perform(get("/search/province/0/Sevilla")).andExpect(status().isOk())
                .andExpect(view().name("Main"));
    }

    @ParameterizedTest
    @CsvFileSource(resources = "../csv/controllers/Searchs-success.csv", encoding = "utf-8", numLinesToSkip = 1, delimiterString = ";")
    @Order(3)
    @WithMockUser(value = "test001", authorities = "user")
    private void testPostSearchSuccess(String text, Integer number1, Integer number2, String type, String username)
            throws Exception {
        Search search = new Search(text, number1, number2, type, username);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(search);

        this.mockMvc.perform(post("/search").contentType(APPLICATION_JSON_UTF8).content(requestJson))
                .andExpect(status().isOk()).andExpect(jsonPath("$.success").value(true));
    }

    @ParameterizedTest
    @CsvFileSource(resources = "../csv/controllers/Searchs-error.csv", encoding = "utf-8", numLinesToSkip = 1, delimiterString = ";")
    @Order(4)
    @WithMockUser(value = "test001", authorities = "user")
    private void testPostSearchHasErrors(String text, Integer number1, Integer number2, String type, String username)
            throws Exception {
        Search search = new Search(text, number1, number2, type, username);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(search);

        this.mockMvc.perform(post("/search").contentType(APPLICATION_JSON_UTF8).content(requestJson))
                .andExpect(status().isOk()).andExpect(jsonPath("$.success").value(false));
    }

    @Test
    @Order(5)
    @WithMockUser(value = "test001", authorities = "user")
    public void testGetSuggestions() throws Exception {
        this.mockMvc.perform(get("/search/titles/Sapiens")).andExpect(status().isOk())
        .andExpect(jsonPath("$.titles").isNotEmpty());
    }

    @Test
    @Order(6)
    @WithMockUser(value = "test001", authorities = "user")
    public void testSearchBooks() throws Exception {
        this.mockMvc.perform(get("/search/q/book/Sapiens?page=0")).andExpect(status().isOk())
                .andExpect(jsonPath("$.books").isNotEmpty());

    }

    @Test
    @Order(Ordered.LOWEST_PRECEDENCE)
    @WithMockUser(value = "test001", authorities = "user")
    public void testGetLastSearch() throws Exception {
        this.mockMvc.perform(get("/search/last")).andExpect(status().isOk())
                .andExpect(jsonPath("$.search.type").value("book"))
                .andExpect(jsonPath("$.search.text").value("Sapiens"));

    }

}
