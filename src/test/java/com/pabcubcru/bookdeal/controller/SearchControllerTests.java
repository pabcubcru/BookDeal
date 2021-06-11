package com.pabcubcru.bookdeal.controller;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.pabcubcru.bookdeal.models.Book;
import com.pabcubcru.bookdeal.models.Image;
import com.pabcubcru.bookdeal.models.RequestStatus;
import com.pabcubcru.bookdeal.models.Search;
import com.pabcubcru.bookdeal.services.BookService;
import com.pabcubcru.bookdeal.services.RequestService;
import com.pabcubcru.bookdeal.services.SearchService;
import com.pabcubcru.bookdeal.services.UserFavouriteBookService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class SearchControllerTests {

    private final String ID_BOOK_1 = "book001";

    @MockBean
    private SearchService searchService;

    @MockBean
    private RequestService requestService;

    @MockBean
    private UserFavouriteBookService userFavouriteBookService;

    @MockBean
    private BookService bookService;

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
        book.setUsername("test001");
        book.setPrice(10.);

        List<Image> images = new ArrayList<Image>();

        Search search = new Search();
        search.setType("book");
        search.setUsername("test001");
        search.setText("Prueba");

        List<Book> books = new ArrayList<>();
        books.add(book);
        Map<Integer, List<Book>> map = new HashMap<>();
        map.put(1, books);
        given(this.searchService.searchBook(search.getText(), PageRequest.of(0, 21), search.getUsername(),
                search.getType())).willReturn(map);

        given(this.requestService.findFirstByIdBook1AndStatus(ID_BOOK_1, RequestStatus.ACEPTADA.toString()))
                .willReturn(null);
        given(this.requestService.findFirstByIdBook2AndStatus(ID_BOOK_1, RequestStatus.ACEPTADA.toString()))
                .willReturn(null);

        given(this.userFavouriteBookService.findByUsernameAndBookId("test001", ID_BOOK_1)).willReturn(null);

        given(this.searchService.findByUsername("test001")).willReturn(search);

        given(this.bookService.findImagesByIdBook(ID_BOOK_1)).willReturn(images);

        List<String> suggestions = new ArrayList<>();
        suggestions.add("Prueba para los tests");
        given(this.searchService.findTitlesOfBooks(search.getText())).willReturn(suggestions);
    }

    @Test
    @WithMockUser(value = "test001", authorities = "user")
    public void testMain() throws Exception {
        this.mockMvc.perform(get("/search")).andExpect(status().isOk()).andExpect(view().name("Main"));
    }

    @Test
    @WithMockUser(value = "test001", authorities = "user")
    public void testMainWithSecurity() throws Exception {
        this.mockMvc.perform(get("/search/province/0/Sevilla")).andExpect(status().isOk())
                .andExpect(view().name("Main"));
    }

    private String postSearch(Search search) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(search);

        MockHttpServletResponse response = this.mockMvc
                .perform(post("/search").contentType(APPLICATION_JSON_UTF8).content(requestJson))
                .andExpect(status().isOk()).andReturn().getResponse();

        String content = response.getContentAsString();

        byte[] ptext = content.getBytes(ISO_8859_1);
        String res = new String(ptext, UTF_8);

        return res;
    }

    @Test
    @WithMockUser(value = "test001", authorities = "user")
    public void testPostSearchTextSuccess() throws Exception {

        Search search = new Search();
        search.setType("book");
        search.setUsername("test001");
        search.setText("Prueba test");

        String content = this.postSearch(search);

        assertThat(content).contains("\"success\":true");
        assertThat(content).contains("\"query\":\"" + search.getText() + "\"");
    }

    @Test
    @WithMockUser(value = "test001", authorities = "user")
    public void testPostSearchTextIsBlank() throws Exception {

        Search search = new Search();
        search.setType("book");
        search.setUsername("test001");
        search.setText("");

        String content = this.postSearch(search);

        assertThat(content).contains("\"success\":false");
        assertThat(content).contains("Campo requerido");
    }

    @Test
    @WithMockUser(value = "test001", authorities = "user")
    public void testPostSearchTextHasLessThan3Characters() throws Exception {

        Search search = new Search();
        search.setType("book");
        search.setUsername("test001");
        search.setText("Pr");

        String content = this.postSearch(search);

        assertThat(content).contains("\"success\":false");
        assertThat(content).contains("Introduce al menos 3 carácteres");
    }

    @Test
    @WithMockUser(value = "test001", authorities = "user")
    public void testPostSearchTextHasMoreThan80Characters() throws Exception {

        Search search = new Search();
        search.setType("book");
        search.setUsername("test001");
        search.setText("Prueba test Prueba test Prueba test Prueba test Prueba test Prueba test Prueba test Prueba test"
                + " Prueba test Prueba test Prueba test Prueba test Prueba test Prueba test Prueba test Prueba test Prueba test ");

        String content = this.postSearch(search);

        assertThat(content).contains("\"success\":false");
        assertThat(content).contains("No puede superar los 80 carácteres");
    }

    @Test
    @WithMockUser(value = "test001", authorities = "user")
    public void testPostSearchRangeYearsSuccess() throws Exception {

        Search search = new Search();
        search.setType("rangeYears");
        search.setUsername("test001");
        search.setNumber1(2000);
        search.setNumber2(2010);

        String content = this.postSearch(search);

        assertThat(content).contains("\"success\":true");
    }

    @Test
    @WithMockUser(value = "test001", authorities = "user")
    public void testPostSearchRangeYearsAreNull() throws Exception {

        Search search = new Search();
        search.setType("rangeYears");
        search.setUsername("test001");
        search.setNumber1(null);
        search.setNumber2(null);

        String content = this.postSearch(search);

        assertThat(content).contains("\"success\":false");
        assertThat(content).contains("Campo requerido");
    }

    @Test
    @WithMockUser(value = "test001", authorities = "user")
    public void testPostSearchRangeYearsAreNegatives() throws Exception {

        Search search = new Search();
        search.setType("rangeYears");
        search.setUsername("test001");
        search.setNumber1(-10);
        search.setNumber2(-60);

        String content = this.postSearch(search);

        assertThat(content).contains("\"success\":false");
        assertThat(content).contains("Debe ser positivo");
    }

    @Test
    @WithMockUser(value = "test001", authorities = "user")
    public void testPostSearchRangeYearsAreInFuture() throws Exception {

        Search search = new Search();
        search.setType("rangeYears");
        search.setUsername("test001");
        search.setNumber1(2030);
        search.setNumber2(2040);

        String content = this.postSearch(search);

        assertThat(content).contains("\"success\":false");
        assertThat(content).contains("Debe ser anterior o igual al año actual");
    }

    @Test
    @WithMockUser(value = "test001", authorities = "user")
    public void testPostSearchRangeYearsNumber2IsBeforeThanNumber1() throws Exception {

        Search search = new Search();
        search.setType("rangeYears");
        search.setUsername("test001");
        search.setNumber1(2015);
        search.setNumber2(2010);

        String content = this.postSearch(search);

        assertThat(content).contains("\"success\":false");
        assertThat(content).contains("Debe ser posterior al año inicial");
    }

    @Test
    @WithMockUser(value = "test001", authorities = "user")
    public void testPostSearchPublicationYearSuccess() throws Exception {

        Search search = new Search();
        search.setType("publicationYear");
        search.setUsername("test001");
        search.setNumber1(2000);

        String content = this.postSearch(search);

        assertThat(content).contains("\"success\":true");
    }

    @Test
    @WithMockUser(value = "test001", authorities = "user")
    public void testPostSearchPublicationYearIsNull() throws Exception {

        Search search = new Search();
        search.setType("publicationYear");
        search.setUsername("test001");
        search.setNumber1(null);

        String content = this.postSearch(search);

        assertThat(content).contains("\"success\":false");
        assertThat(content).contains("Campo requerido");
    }

    @Test
    @WithMockUser(value = "test001", authorities = "user")
    public void testPostSearchPublicationYearIsNegative() throws Exception {

        Search search = new Search();
        search.setType("publicationYear");
        search.setUsername("test001");
        search.setNumber1(-1000);

        String content = this.postSearch(search);

        assertThat(content).contains("\"success\":false");
        assertThat(content).contains("Debe ser positivo");
    }

    @Test
    @WithMockUser(value = "test001", authorities = "user")
    public void testPostSearchPublicationYearIsInFuture() throws Exception {

        Search search = new Search();
        search.setType("publicationYear");
        search.setUsername("test001");
        search.setNumber1(2030);

        String content = this.postSearch(search);

        assertThat(content).contains("\"success\":false");
        assertThat(content).contains("Debe ser anterior o igual al año actual");
    }

    @Test
    @WithMockUser(value = "test001", authorities = "user")
    public void testPostSearchPostalCodeSuccess() throws Exception {

        Search search = new Search();
        search.setType("postalCode");
        search.setUsername("test001");
        search.setText("41012");

        String content = this.postSearch(search);

        assertThat(content).contains("\"success\":true");
    }

    @Test
    @WithMockUser(value = "test001", authorities = "user")
    public void testPostSearchPostalCodeIsBlank() throws Exception {

        Search search = new Search();
        search.setType("postalCode");
        search.setUsername("test001");
        search.setText("");

        String content = this.postSearch(search);

        assertThat(content).contains("\"success\":false");
        assertThat(content).contains("Campo requerido");
    }

    @Test
    @WithMockUser(value = "test001", authorities = "user")
    public void testPostSearchPostalCodeIsNegative() throws Exception {

        Search search = new Search();
        search.setType("postalCode");
        search.setUsername("test001");
        search.setText("-41012");

        String content = this.postSearch(search);

        assertThat(content).contains("\"success\":false");
        assertThat(content).contains("Debe ser positivo");
    }

    @Test
    @WithMockUser(value = "test001", authorities = "user")
    public void testPostSearchPostalCodeIsNotValid() throws Exception {

        Search search = new Search();
        search.setType("postalCode");
        search.setUsername("test001");
        search.setText("125");

        String content = this.postSearch(search);

        assertThat(content).contains("\"success\":false");
        assertThat(content).contains("Código postal no válido");
    }

    @Test
    @WithMockUser(value = "test001", authorities = "user")
    public void testPostSearchRangePricesSuccess() throws Exception {

        Search search = new Search();
        search.setType("rangePrices");
        search.setUsername("test001");
        search.setNumber1(10);
        search.setNumber2(50);

        String content = this.postSearch(search);

        assertThat(content).contains("\"success\":true");
    }

    @Test
    @WithMockUser(value = "test001", authorities = "user")
    public void testPostSearchRangePricesAreNull() throws Exception {

        Search search = new Search();
        search.setType("rangePrices");
        search.setUsername("test001");
        search.setNumber1(null);
        search.setNumber2(null);

        String content = this.postSearch(search);

        assertThat(content).contains("\"success\":false");
        assertThat(content).contains("Campo requerido");
    }

    @Test
    @WithMockUser(value = "test001", authorities = "user")
    public void testPostSearchRangePricesAreNegatives() throws Exception {

        Search search = new Search();
        search.setType("rangePrices");
        search.setUsername("test001");
        search.setNumber1(-10);
        search.setNumber2(-20);

        String content = this.postSearch(search);

        assertThat(content).contains("\"success\":false");
        assertThat(content).contains("Debe ser positivo");
    }

    @Test
    @WithMockUser(value = "test001", authorities = "user")
    public void testGetSuggestions() throws Exception {
        MockHttpServletResponse response = this.mockMvc.perform(get("/search/titles/Prueba")).andExpect(status().isOk())
                .andReturn().getResponse();

        assertThat(response.getContentAsString()).contains("Prueba para los tests");
    }

    @Test
    @WithMockUser(value = "test001", authorities = "user")
    public void testSearchBooks() throws Exception {
        MockHttpServletResponse response = this.mockMvc.perform(get("/search/q/book/Prueba?page=0")).andExpect(status().isOk())
                .andReturn().getResponse();

        assertThat(response.getContentAsString()).contains(ID_BOOK_1);
    }

    @Test
    @WithMockUser(value = "test001", authorities = "user")
    public void testGetLastSearch() throws Exception {
        MockHttpServletResponse response = this.mockMvc.perform(get("/search/last")).andExpect(status().isOk())
                .andReturn().getResponse();

        assertThat(response.getContentAsString()).contains("book");
        assertThat(response.getContentAsString()).contains("Prueba");
    }

}
