package com.pabcubcru.bookdeal.controller;

import java.nio.charset.Charset;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.pabcubcru.bookdeal.models.Book;

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
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class BookControllerTests {

        @Autowired
        private MockMvc mockMvc;

        private final String ID_BOOK_1 = "book-001";
        private final String ID_BOOK_2 = "book-002";

        private final String ID_IMAGE_1 = "image001";

        public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
                        MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));
        public static final Charset ISO_8859_1 = Charset.forName("ISO-8859-1");
        public static final Charset UTF_8 = Charset.forName("UTF-8");

        @Test
        @WithMockUser(value = "test001", authorities = "user")
        public void testMain() throws Exception {
                this.mockMvc.perform(get("/books/new")).andExpect(status().isOk()).andExpect(view().name("Main"));
        }

        @Test
        @WithMockUser(value = "test001", authorities = "user")
        public void testMainForShowMode() throws Exception {
                this.mockMvc.perform(get("/books/all/0/postCode")).andExpect(status().isOk())
                                .andExpect(view().name("Main"));
        }

        @Test
        @WithMockUser(value = "test001", authorities = "user")
        public void testMainWithSecurity() throws Exception {
                this.mockMvc.perform(get("/books/{id}", ID_BOOK_1)).andExpect(status().isOk())
                                .andExpect(view().name("Main"));
        }

        @Test
        @WithMockUser(value = "test001", authorities = "user")
        public void testMainWithSecurityError404() throws Exception {
                this.mockMvc.perform(get("/books/booktest")).andExpect(status().isOk())
                                .andExpect(view().name("errors/Error404"));
        }

        @Test
        @WithMockUser(value = "test001", authorities = "user")
        public void testMainWithUserSecurityError404() throws Exception {
                this.mockMvc.perform(get("/books/booktest/edit")).andExpect(status().isOk())
                                .andExpect(view().name("errors/Error404"));
        }

        @Test
        @WithMockUser(value = "test001", authorities = "user")
        public void testMainWithUserSecurityError403() throws Exception {
                this.mockMvc.perform(get("/books/{id}/edit", ID_BOOK_1)).andExpect(status().isOk())
                                .andExpect(view().name("errors/Error403"));
        }

        @ParameterizedTest
        @CsvFileSource(resources = "../csv/controllers/Books-success.csv", encoding = "utf-8", numLinesToSkip = 1, delimiterString = ";")
        @Order(1)
        @WithMockUser(value = "test001", authorities = { "user" })
        public void testCreateBookSuccess(String title, String originalTitle, String isbn, Integer publicationYear,
                        String publisher, String genres, String author, String description, String status, Double price,
                        String username, String image) throws Exception {
                Book book = new Book(title, originalTitle, isbn, publicationYear, publisher, genres, author,
                                description, status, price, username, image);

                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
                ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
                String requestJson = ow.writeValueAsString(book);

                mockMvc.perform(post("/books/new").contentType(APPLICATION_JSON_UTF8).content(requestJson))
                                .andExpect(status().isOk()).andExpect(jsonPath("$.success").value(true));
        }

        @ParameterizedTest
        @CsvFileSource(resources = "../csv/controllers/Books-error.csv", encoding = "utf-8", numLinesToSkip = 1, delimiterString = ";")
        @Order(2)
        @WithMockUser(value = "test001", authorities = { "user" })
        public void testCreateBookHasErrors(String title, String originalTitle, String isbn, Integer publicationYear,
                        String publisher, String genres, String author, String description, String status, Double price,
                        String username, String image) throws Exception {
                Book book = new Book(title, originalTitle, isbn, publicationYear, publisher, genres, author,
                                description, status, price, username, image);

                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
                ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
                String requestJson = ow.writeValueAsString(book);

                mockMvc.perform(post("/books/new").contentType(APPLICATION_JSON_UTF8).content(requestJson))
                                .andExpect(status().isOk()).andExpect(jsonPath("$.success").value(false));
        }

        @ParameterizedTest
        @CsvFileSource(resources = "../csv/controllers/Books-success.csv", encoding = "utf-8", numLinesToSkip = 1, delimiterString = ";")
        @Order(3)
        @WithMockUser(value = "test002", authorities = { "user" })
        public void testPutEditBookSuccess(String title, String originalTitle, String isbn, Integer publicationYear,
                        String publisher, String genres, String author, String description, String status, Double price,
                        String username, String image) throws Exception {
                Book book = new Book(title, originalTitle, isbn, publicationYear, publisher, genres, author,
                                description, status, price, username, image);
                book.setId(ID_BOOK_2);

                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
                ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
                String requestJson = ow.writeValueAsString(book);

                mockMvc.perform(put("/books/" + ID_BOOK_1 + "/edit").contentType(APPLICATION_JSON_UTF8)
                                .content(requestJson)).andExpect(status().isOk())
                                .andExpect(jsonPath("$.success").value(true));
        }

        @ParameterizedTest
        @CsvFileSource(resources = "../csv/controllers/Books-error.csv", encoding = "utf-8", numLinesToSkip = 1, delimiterString = ";")
        @Order(4)
        @WithMockUser(value = "test002", authorities = { "user" })
        public void testPutEditBookHasErrors(String title, String originalTitle, String isbn, Integer publicationYear,
                        String publisher, String genres, String author, String description, String status, Double price,
                        String username, String image) throws Exception {
                Book book = new Book(title, originalTitle, isbn, publicationYear, publisher, genres, author,
                                description, status, price, username, image);
                book.setId(ID_BOOK_2);

                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
                ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
                String requestJson = ow.writeValueAsString(book);

                mockMvc.perform(put("/books/" + ID_BOOK_1 + "/edit").contentType(APPLICATION_JSON_UTF8)
                                .content(requestJson)).andExpect(status().isOk())
                                .andExpect(jsonPath("$.success").value(false));
        }

        @Test
        @WithMockUser(value = "test001", authorities = "user")
        public void testDeleteBook() throws Exception {
                this.mockMvc.perform(delete("/books/" + ID_BOOK_1 + "/delete")).andExpect(status().isOk());
        }

        @Test
        @WithMockUser(value = "test002", authorities = "user")
        public void testListAllExceptMyBooks() throws Exception {
                this.mockMvc.perform(get("/books/list/all-me?page=0&showMode=undefined")).andExpect(status().isOk())
                                .andExpect(jsonPath("$.books").isNotEmpty());
        }

        @Test
        @WithMockUser(value = "test002", authorities = "user")
        public void testListMyBooksExceptWithAcceptedRequest() throws Exception {
                this.mockMvc.perform(get("/books/list/me-change"))
                                .andExpect(status().isOk()).andExpect(jsonPath("$.books").isEmpty());
        }

        @Test
        @WithMockUser(value = "test002", authorities = "user")
        public void testListMyBooks() throws Exception {
                MockHttpServletResponse response = this.mockMvc.perform(get("/books/list/me?page=0"))
                                .andExpect(status().isOk()).andReturn().getResponse();

                assertThat(response.getContentAsString()).contains(ID_BOOK_2);
        }

        @Test
        @WithMockUser(value = "test002", authorities = "user")
        public void testGenres() throws Exception {
                MockHttpServletResponse response = this.mockMvc.perform(get("/books/genres")).andExpect(status().isOk())
                                .andReturn().getResponse();

                String content = response.getContentAsString();

                byte[] ptext = content.getBytes(ISO_8859_1);
                String res = new String(ptext, UTF_8);

                assertThat(res).contains(
                                "\"Arte\",\"Diseño\",\"Autoayuda\",\"Esoterismo\",\"Ciencia\",\"Naturaleza\",\"Ciencias_Sociales\",\"Ciencia_Ficción\","
                                                + "\"Deportes\",\"Derecho\",\"Política\",\"Economía\",\"Negocios\",\"Ensayos\",\"Biografías\",\"Espectáculos\","
                                                + "\"Cine\",\"Música\",\"Fantasía\",\"Filosofía\",\"Religión\",\"Gastronomía\",\"Cocina\",\"Historia\",\"Hogar\","
                                                + "\"Familia\",\"Ingeniería\",\"Tecnología\",\"Libros_Infantiles\",\"Libros_Juveniles\",\"Literatura\",\"Novela\","
                                                + "\"Medicina\",\"Salud\",\"Obras_de_Consulta\",\"Idiomas\",\"Ocio_y_tiempo_libre\",\"Poesía\",\"Viajes\","
                                                + "\"Geografía\",\"Otros\"");
        }

        @Test
        @WithMockUser(value = "test002", authorities = "user")
        public void testGetBookById() throws Exception {
                this.mockMvc.perform(get("/books/get/" + ID_BOOK_2)).andExpect(status().isOk())
                                .andExpect(jsonPath("$.success").value(true));

        }

        @Test
        @WithMockUser(value = "test002", authorities = "user")
        public void testGetBookByIdToEdit() throws Exception {
                MockHttpServletResponse response = this.mockMvc.perform(get("/books/edit/" + ID_BOOK_2))
                                .andExpect(status().isOk()).andReturn().getResponse();

                assertThat(response.getContentAsString()).contains(ID_BOOK_2);
        }

        @Test
        @WithMockUser(value = "test001", authorities = "user")
        public void testChangeImagePrincipal() throws Exception {
                this.mockMvc.perform(get("/books/" + ID_BOOK_1 + "/images/" + ID_IMAGE_1 + "/principal"))
                                .andExpect(status().isOk());
        }

        @Test
        @WithMockUser(value = "test001", authorities = "user")
        public void testDeleteImage() throws Exception {
                this.mockMvc.perform(delete("/books/images/" + ID_IMAGE_1 + "/delete")).andExpect(status().isOk());
        }

        @Test
        @Order(Ordered.HIGHEST_PRECEDENCE)
        @WithMockUser(value = "test003", authorities = "user")
        public void testRecommendBooks() throws Exception {
                this.mockMvc.perform(get("/books/recommend"))
                                .andExpect(status().isOk()).andExpect(jsonPath("$.books").isNotEmpty());
        }
}
