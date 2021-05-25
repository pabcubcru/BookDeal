package com.pabcubcru.infobooks.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.pabcubcru.infobooks.models.Book;
import com.pabcubcru.infobooks.models.Image;
import com.pabcubcru.infobooks.models.Request;
import com.pabcubcru.infobooks.models.RequestStatus;
import com.pabcubcru.infobooks.services.BookService;
import com.pabcubcru.infobooks.services.RequestService;
import com.pabcubcru.infobooks.services.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
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
import java.util.ArrayList;
import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class RequestControllerTests {

    private final String ID_BOOK_1 = "book001";
    private final String ID_REQUEST_1 = "request001";
    private final String ID_REQUEST_2 = "request002";
    private final String ID_IMAGE_1 = "image001";

    @MockBean
    private RequestService requestService;

    @MockBean
    private BookService bookService;

    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));
    public static final Charset ISO_8859_1 = Charset.forName("ISO-8859-1");
    public static final Charset UTF_8 = Charset.forName("UTF-8");

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

        Request request = new Request();
        request.setId(ID_REQUEST_1);
        request.setAction("VENTA");
        request.setIdBook2(ID_BOOK_1);
        request.setPay(10.);
        request.setComment("Comment");
        request.setStatus(RequestStatus.PENDIENTE.toString());
        request.setUsername2("test001");

        Image image = new Image();
        image.setId(ID_IMAGE_1);
        image.setUrlImage("https://i.ibb.co/YRy9kHC/paper.jpg");
        image.setIdBook(ID_BOOK_1);
        image.setPrincipal(true);

        List<Request> requests = new ArrayList<>();
        requests.add(request);

        given(this.bookService.findBookById("book001")).willReturn(book);
        given(this.bookService.findBookById("book002")).willReturn(null);
        book.setId("book003");
        given(this.bookService.findBookById("book003")).willReturn(book);

        given(this.requestService.findByUsername1AndIdBook2("test001", "book001")).willReturn(null);
        given(this.requestService.findByUsername1AndIdBook2("test001", "book002")).willReturn(null);
        given(this.requestService.findByUsername1AndIdBook2("test001", "book003")).willReturn(new Request());

        given(this.requestService.findByUsername1AndIdBook2("test002", "book001")).willReturn(null);

        Page<Request> page = new PageImpl<>(requests);
        given(this.requestService.listMyRequests("test001", PageRequest.of(0, 10))).willReturn(page);

        given(this.requestService.listReceivedRequests("test001", PageRequest.of(0, 10))).willReturn(page);

        given(this.bookService.findByIdBookAndPrincipalTrue("book003")).willReturn(image);

        given(this.requestService.findById("request001")).willReturn(request);

        request.setStatus(RequestStatus.CANCELADA.toString());
        given(this.requestService.findById("request002")).willReturn(request);

        given(this.requestService.findByIdBook2AndStatusNotAndStatusNotAndAction(ID_BOOK_1, "VENTA"))
                .willReturn(requests);

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
    public void testAddRequestSuccess() throws Exception {
        Request request = new Request();
        request.setAction("COMPRA");
        request.setIdBook2(ID_BOOK_1);
        request.setPay(10.);
        request.setComment("Comment");

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(request);

        this.mockMvc.perform(post("/requests/book001/new").contentType(APPLICATION_JSON_UTF8).content(requestJson))
                .andExpect(status().isOk()).andExpect(jsonPath("$.success").value(true));
    }

    private String convertToUTF8(String content) {
        byte[] ptext = content.getBytes(ISO_8859_1);
        String res = new String(ptext, UTF_8);

        return res;
    }

    @Test
    @WithMockUser(value = "test002", authorities = "user")
    public void testAddRequestHasErrors() throws Exception {
        Request request = new Request();
        request.setAction("COMPRA");
        request.setIdBook2(ID_BOOK_1);
        request.setPay(null);
        request.setComment("Comment Comment Comment Comment Comment Comment Comment Comment Comment"
                + " Comment Comment Comment Comment Comment Comment Comment Comment Comment Comment ");

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(request);

        MockHttpServletResponse response = this.mockMvc
                .perform(post("/requests/book001/new").contentType(APPLICATION_JSON_UTF8).content(requestJson))
                .andExpect(status().isOk()).andReturn().getResponse();

        String content = response.getContentAsString();

        String res = this.convertToUTF8(content);

        assertThat(res).contains("El precio es un campo requerido.");
        assertThat(res).contains("El comentario adicional no debe superar los 80 carácteres.");
    }

    @Test
    @WithMockUser(value = "test001", authorities = "user")
    public void testListMyRequests() throws Exception {
        this.mockMvc.perform(get("/requests/my-requests?page=0")).andExpect(status().isOk())
                .andExpect(jsonPath("$.requests[0].id").value(ID_REQUEST_1))
                .andExpect(jsonPath("$.books2[0].id").value("book003"));
    }

    @Test
    @WithMockUser(value = "test001", authorities = "user")
    public void testListReceivedRequests() throws Exception {
        this.mockMvc.perform(get("/requests/received-requests?page=0")).andExpect(status().isOk())
                .andExpect(jsonPath("$.requests[0].id").value(ID_REQUEST_1))
                .andExpect(jsonPath("$.books2[0].id").value("book003"));
    }

    @Test
    @WithMockUser(value = "test001", authorities = "user")
    public void testCancelRequestSuccess() throws Exception {
        this.mockMvc.perform(get("/requests/" + ID_REQUEST_1 + "/cancel")).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(value = "test001", authorities = "user")
    public void testCancelRequestError404() throws Exception {
        this.mockMvc.perform(get("/requests/" + "id_not_exist" + "/cancel")).andExpect(status().isOk())
                .andExpect(view().name("errors/Error404"));
    }

    @Test
    @WithMockUser(value = "test002", authorities = "user")
    public void testCancelRequestError403() throws Exception {
        this.mockMvc.perform(get("/requests/" + ID_REQUEST_1 + "/cancel")).andExpect(status().isOk())
                .andExpect(view().name("errors/Error403"));
    }

    @Test
    @WithMockUser(value = "test001", authorities = "user")
    public void testDeleteRequestSuccess() throws Exception {
        this.mockMvc.perform(delete("/requests/" + ID_REQUEST_2 + "/delete")).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(value = "test001", authorities = "user")
    public void testDeleteRequestError404() throws Exception {
        this.mockMvc.perform(delete("/requests/" + "id_not_exist" + "/delete")).andExpect(status().isOk())
                .andExpect(view().name("errors/Error404"));
    }

    @Test
    @WithMockUser(value = "test002", authorities = "user")
    public void testDeleteRequestError403() throws Exception {
        this.mockMvc.perform(delete("/requests/" + ID_REQUEST_2 + "/delete")).andExpect(status().isOk())
                .andExpect(view().name("errors/Error403"));
    }

    @Test
    @WithMockUser(value = "test001", authorities = "user")
    public void testAcceptRequestSuccess() throws Exception {
        this.mockMvc.perform(get("/requests/" + ID_REQUEST_2 + "/accept")).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(value = "test001", authorities = "user")
    public void testAcceptRequestError404() throws Exception {
        this.mockMvc.perform(get("/requests/" + "not_exist" + "/accept")).andExpect(status().isOk())
                .andExpect(view().name("errors/Error404"));
    }

    @Test
    @WithMockUser(value = "test002", authorities = "user")
    public void testAcceptRequestError403() throws Exception {
        this.mockMvc.perform(get("/requests/" + ID_REQUEST_2 + "/accept")).andExpect(status().isOk())
                .andExpect(view().name("errors/Error403"));
    }

    @Test
    @WithMockUser(value = "test001", authorities = "user")
    public void testRejectRequestSuccess() throws Exception {
        this.mockMvc.perform(get("/requests/" + ID_REQUEST_2 + "/reject")).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(value = "test001", authorities = "user")
    public void testRejectRequestError404() throws Exception {
        this.mockMvc.perform(get("/requests/" + "not_exists" + "/reject")).andExpect(status().isOk())
                .andExpect(view().name("errors/Error404"));
    }

    @Test
    @WithMockUser(value = "test002", authorities = "user")
    public void testRejectRequestError403() throws Exception {
        this.mockMvc.perform(get("/requests/" + ID_REQUEST_2 + "/reject")).andExpect(status().isOk())
                .andExpect(view().name("errors/Error403"));
    }

}