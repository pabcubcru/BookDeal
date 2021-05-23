package com.pabcubcru.infobooks.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.pabcubcru.infobooks.models.Book;
import com.pabcubcru.infobooks.models.Request;
import com.pabcubcru.infobooks.services.BookService;
import com.pabcubcru.infobooks.services.RequestService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.nio.charset.Charset;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class RequestControllerTests {

    private final String ID_BOOK_1 = "book001";

    @MockBean
    private RequestService requestService;

    @MockBean
    private BookService bookService;

    @Autowired
    private MockMvc mockMvc;

    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

    @BeforeEach
    public void setup() {
        Book book = new Book();
        book.setId(ID_BOOK_1);
        book.setTitle("Title");
        book.setIsbn("0-7645-2641-3");
        book.setPublicationYear(2014);
        book.setPublisher("Publisher");
        book.setGenres("Comedia,Aventuras");
        book.setAuthor("Author Test");
        book.setDescription("Description Test");
        book.setStatus("COMO NUEVO");
        book.setUsername("test010");
        book.setPrice(10.);

        given(this.bookService.findBookById("book001")).willReturn(book);
        given(this.bookService.findBookById("book002")).willReturn(null);
        book.setId("book003");
        given(this.bookService.findBookById("book003")).willReturn(book);

        given(this.requestService.findByUsername1AndIdBook2("test001", "book001")).willReturn(null);
        given(this.requestService.findByUsername1AndIdBook2("test001", "book002")).willReturn(null);
        given(this.requestService.findByUsername1AndIdBook2("test001", "book003")).willReturn(new Request());

        given(this.requestService.findByUsername1AndIdBook2("test002", "book001")).willReturn(null);
    }

    @Test
    @WithMockUser(value = "test001", authorities = "user")
    public void testMain() throws Exception {
        this.mockMvc.perform(get("/requests/me/0")).andExpect(status().isOk()).andExpect(view().name("Main"));
    }

    @Test
    @WithMockUser(value = "test001", authorities = "user")
    public void testMainWithSecurity() throws Exception {
        this.mockMvc.perform(get("/requests/" + ID_BOOK_1 + "/add")).andExpect(status().isOk())
                .andExpect(view().name("Main"));
    }

    @Test
    @WithMockUser(value = "test001", authorities = "user")
    public void testMainWithSecurityError404() throws Exception {
        this.mockMvc.perform(get("/requests/book002/add")).andExpect(status().isOk())
                .andExpect(view().name("errors/Error404"));
    }

    @Test
    @WithMockUser(value = "test001", authorities = "user")
    public void testMainWithSecurityError403() throws Exception {
        this.mockMvc.perform(get("/requests/book003/add")).andExpect(status().isOk())
                .andExpect(view().name("errors/Error403"));
    }

    @Test
    @WithMockUser(value = "test002", authorities = "user")
    public void testAddRequest() throws Exception {
        Request request = new Request();
        request.setAction("COMPRA");
        request.setIdBook2(ID_BOOK_1);
        request.setPay(10.);
        request.setComment("Comment");

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(request);

        MockHttpServletResponse response = mockMvc
                .perform(post("/requests/book001/new").contentType(APPLICATION_JSON_UTF8).content(requestJson))
                .andExpect(status().isOk()).andReturn().getResponse();

        String content = response.getContentAsString();

        assertThat(content).contains("\"success\":true");
    }

}
