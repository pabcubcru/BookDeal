package com.pabcubcru.bookdeal.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;

import com.pabcubcru.bookdeal.models.Request;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.nio.charset.Charset;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class RequestControllerTests {

    private final String ID_BOOK_1 = "book-001";
    private final String ID_BOOK_2 = "book-002";
    private final String ID_BOOK_3 = "book-003";
    private final String ID_REQUEST_1 = "request-001";
    private final String ID_REQUEST_2 = "request-002";

    @Autowired
    private MockMvc mockMvc;

    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));
    public static final Charset ISO_8859_1 = Charset.forName("ISO-8859-1");
    public static final Charset UTF_8 = Charset.forName("UTF-8");

    @Test
    @WithMockUser(value = "test001", authorities = "user")
    public void testMain() throws Exception {
        this.mockMvc.perform(get("/requests/me/0")).andExpect(status().isOk()).andExpect(view().name("Main"));
    }

    @Test
    @WithMockUser(value = "test002", authorities = "user")
    public void testMainWithSecurity() throws Exception {
        this.mockMvc.perform(get("/requests/" + ID_BOOK_3 + "/add")).andExpect(status().isOk())
                .andExpect(view().name("Main"));
    }

    @ParameterizedTest
    @CsvFileSource(resources = "../csv/controllers/Requests-success.csv", encoding = "utf-8", numLinesToSkip = 1, delimiterString = ";")
    @WithMockUser(value = "test003", authorities = "user")
    public void testAddRequestSuccess(String username1, String username2, String idBook1, String idBook2, String status,
            String action, Double pay, String comment) throws Exception {
        Request request = new Request(username1, username2, idBook1, idBook2, status, action, pay, comment);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(request);

        this.mockMvc.perform(
                    post("/requests/" + ID_BOOK_3 + "/new").contentType(APPLICATION_JSON_UTF8).content(requestJson))
                    .andExpect(status().isOk()).andExpect(jsonPath("$.success").value(true));
    }

    @ParameterizedTest
    @CsvFileSource(resources = "../csv/controllers/Requests-error.csv", encoding = "utf-8", numLinesToSkip = 1, delimiterString = ";")
    @WithMockUser(value = "test003", authorities = "user")
    public void testAddRequestHasErrors(String username1, String username2, String idBook1, String idBook2,
            String status, String action, Double pay, String comment) throws Exception {
        Request request = new Request(username1, username2, idBook1, idBook2, status, action, pay, comment);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(request);

        this.mockMvc.perform(post("/requests/" + ID_BOOK_1 + "/new")
                    .contentType(APPLICATION_JSON_UTF8).content(requestJson))
                    .andExpect(status().isOk()).andExpect(jsonPath("$.success").value(false));
    }

    @Test
    @WithMockUser(value = "test001", authorities = "user")
    public void testListMyRequests() throws Exception {
        this.mockMvc.perform(get("/requests/my-requests?page=0")).andExpect(status().isOk())
                .andExpect(jsonPath("$.requests[0].id").value(ID_REQUEST_2))
                .andExpect(jsonPath("$.books2[0].id").value(ID_BOOK_2));
    }

    @Test
    @WithMockUser(value = "test001", authorities = "user")
    public void testListReceivedRequests() throws Exception {
        this.mockMvc.perform(get("/requests/received-requests?page=0")).andExpect(status().isOk())
                .andExpect(jsonPath("$.requests").isEmpty());
    }

    @Test
    @WithMockUser(value = "test001", authorities = "user")
    public void testCancelRequestSuccess() throws Exception {
        this.mockMvc.perform(get("/requests/" + ID_REQUEST_1 + "/cancel")).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(value = "test001", authorities = "user")
    public void testDeleteRequestSuccess() throws Exception {
        this.mockMvc.perform(delete("/requests/" + ID_REQUEST_2 + "/delete")).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(value = "test010", authorities = "user")
    public void testAcceptRequestSuccess() throws Exception {
        this.mockMvc.perform(get("/requests/" + ID_REQUEST_2 + "/accept")).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(value = "test010", authorities = "user")
    public void testRejectRequestSuccess() throws Exception {
        this.mockMvc.perform(get("/requests/" + ID_REQUEST_2 + "/reject")).andExpect(status().isOk());
    }
}
