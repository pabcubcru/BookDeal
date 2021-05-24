package com.pabcubcru.infobooks.controller;

import java.nio.charset.Charset;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.pabcubcru.infobooks.models.Book;
import com.pabcubcru.infobooks.models.Image;
import com.pabcubcru.infobooks.models.Request;
import com.pabcubcru.infobooks.models.RequestStatus;
import com.pabcubcru.infobooks.models.User;
import com.pabcubcru.infobooks.services.BookService;
import com.pabcubcru.infobooks.services.RequestService;
import com.pabcubcru.infobooks.services.SearchService;
import com.pabcubcru.infobooks.services.UserFavouriteBookService;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class BookControllerTests {

        @Autowired
        private MockMvc mockMvc;

        private final String ID_BOOK_1 = "book001";
        private final String ID_BOOK_2 = "book002";

        private final String ID_IMAGE_1 = "image001";

        @MockBean
        private BookService bookService;

        @MockBean
        private RequestService requestService;

        @MockBean
        private UserService userService;

        @MockBean
        private UserFavouriteBookService userFavouriteBookService;

        @MockBean
        private SearchService searchService;

        public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
                        MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));
        public static final Charset ISO_8859_1 = Charset.forName("ISO-8859-1");
        public static final Charset UTF_8 = Charset.forName("UTF-8");

        @BeforeEach
        void setup() {

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

                Book book2 = new Book();
                book2.setUsername("test002");
                book2.setTitle("Title");
                book2.setIsbn("0-7645-2641-3");
                book2.setPublicationYear(2014);
                book2.setPublisher("Publisher");
                book2.setGenres("Comedia,Aventuras");
                book2.setAuthor("Author Test");
                book2.setDescription("Description Test");
                book2.setStatus("COMO NUEVO");
                book2.setPrice(10.);
                book2.setId(ID_BOOK_2);

                User user = new User();

                user.setName("Test");
                user.setEmail("email@email.com");
                user.setPhone("+34654987321");
                user.setBirthDate(LocalDate.of(1998, 11, 23));
                user.setProvince("Province");
                user.setPostCode("41012");
                user.setUsername("test002");
                user.setPassword(new BCryptPasswordEncoder().encode("password123"));
                user.setEnabled(true);
                user.setGenres("Religión,Gastronomía,Cocina");

                Image image = new Image();
                image.setId(ID_IMAGE_1);
                image.setUrlImage("https://i.ibb.co/YRy9kHC/paper.jpg");
                image.setIdBook(ID_BOOK_2);
                image.setPrincipal(true);

                List<Request> requests = new ArrayList<Request>();
                List<Image> images = new ArrayList<Image>();

                List<Book> listTest001 = new ArrayList<>();
                listTest001.add(new Book());
                Page<Book> booksTest001 = new PageImpl<>(listTest001);

                given(this.bookService.findBookById(ID_BOOK_1)).willReturn(book);
                given(this.bookService.findBookById(ID_BOOK_2)).willReturn(book2);
                given(this.bookService.findAllExceptMine("test001", PageRequest.of(1, 21))).willReturn(booksTest001);

                given(this.requestService.findByIdBook1OrIdBook2(ID_BOOK_1, ID_BOOK_1)).willReturn(requests);
                given(this.requestService.findFirstByIdBook1AndStatus(ID_BOOK_1, RequestStatus.ACEPTADA.toString()))
                                .willReturn(null);
                given(this.requestService.findFirstByIdBook2AndStatus(ID_BOOK_1, RequestStatus.ACEPTADA.toString()))
                                .willReturn(null);

                given(this.requestService.findByIdBook1OrIdBook2(ID_BOOK_2, ID_BOOK_2)).willReturn(requests);
                given(this.requestService.findFirstByIdBook1AndStatus(ID_BOOK_2, RequestStatus.ACEPTADA.toString()))
                                .willReturn(null);
                given(this.requestService.findFirstByIdBook2AndStatus(ID_BOOK_2, RequestStatus.ACEPTADA.toString()))
                                .willReturn(null);
                given(this.bookService.findImagesByIdBook(ID_BOOK_2)).willReturn(images);

                given(this.userFavouriteBookService.findByUsernameAndBookId("test001", ID_BOOK_1)).willReturn(null);

                List<Book> books = new ArrayList<Book>();
                books.add(book2);
                given(this.bookService.findByUsername("test002")).willReturn(books);
                given(this.bookService.findByIdBookAndPrincipalTrue(ID_BOOK_2)).willReturn(image);

                Page<Book> page = new PageImpl<>(books);
                given(this.bookService.findMyBooks("test002", PageRequest.of(0, 21))).willReturn(page);

                given(this.userService.findByUsername("test002")).willReturn(user);
                given(this.bookService.findNearBooks(user, PageRequest.of(0, 21), "undefined")).willReturn(page);

                given(this.bookService.findImageById(ID_IMAGE_1)).willReturn(image);
                given(this.bookService.findByIdBookAndPrincipalTrue(ID_BOOK_1)).willReturn(new Image());

                Map<Integer, List<Book>> map = new HashMap<>();
                map.put(1, books);
                given(this.searchService.recommendBooks(user, PageRequest.of(0, 21))).willReturn(map);
        }

        @Test
        @WithMockUser(value = "user1", authorities = "user")
        public void testMain() throws Exception {
                this.mockMvc.perform(get("/books/new")).andExpect(status().isOk()).andExpect(view().name("Main"));
        }

        @Test
        @WithMockUser(value = "user1", authorities = "user")
        public void testMainForShowMode() throws Exception {
                this.mockMvc.perform(get("/books/all/0/postCode")).andExpect(status().isOk())
                                .andExpect(view().name("Main"));
        }

        @Test
        @WithMockUser(value = "user1", authorities = "user")
        public void testMainWithSecurity() throws Exception {
                this.mockMvc.perform(get("/books/{id}", ID_BOOK_1)).andExpect(status().isOk())
                                .andExpect(view().name("Main"));
        }

        @Test
        @WithMockUser(value = "user1", authorities = "user")
        public void testMainWithSecurityError404() throws Exception {
                this.mockMvc.perform(get("/books/booktest")).andExpect(status().isOk())
                                .andExpect(view().name("errors/Error404"));
        }

        @Test
        @WithMockUser(value = "user1", authorities = "user")
        public void testMainWithUserSecurityError404() throws Exception {
                this.mockMvc.perform(get("/books/booktest/edit")).andExpect(status().isOk())
                                .andExpect(view().name("errors/Error404"));
        }

        @Test
        @WithMockUser(value = "user1", authorities = "user")
        public void testMainWithUserSecurityError403() throws Exception {
                this.mockMvc.perform(get("/books/{id}/edit", ID_BOOK_1)).andExpect(status().isOk())
                                .andExpect(view().name("errors/Error403"));
        }

        @Test
        @WithMockUser(value = "test001", authorities = { "user" })
        public void testCreateBookSuccess() throws Exception {
                Book book = new Book();
                book.setTitle("Titletest");
                book.setIsbn("0-7645-2641-3");
                book.setPublicationYear(2010);
                book.setPublisher("PublisherTest");
                book.setGenres("Gastronomía,Cocina");
                book.setAuthor("Author Test");
                book.setDescription("Description Test");
                book.setImage("2");
                book.setStatus("COMO NUEVO");
                book.setUsername("test001");
                book.setPrice(10.);

                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
                ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
                String requestJson = ow.writeValueAsString(book);

                mockMvc.perform(post("/books/new").contentType(APPLICATION_JSON_UTF8).content(requestJson))
                                .andExpect(status().isOk()).andExpect(jsonPath("$.success").value(true));
        }

        @Test
        @WithMockUser(value = "test001", authorities = { "user" })
        public void testCreateBookHasErrors() throws Exception {
                Book book = new Book();
                book.setTitle(""); // BLANK
                book.setIsbn("0-7645-2641-3");
                book.setPublicationYear(null); // NULL
                book.setPublisher("PublisherTest");
                book.setGenres("Gastronomía,Cocina");
                book.setAuthor("Author Test");
                book.setDescription("Description Test");
                book.setImage("0"); // 0 IMAGES
                book.setStatus("COMO NUEVO");
                book.setUsername("test001");
                book.setPrice(10.);

                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
                ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
                String requestJson = ow.writeValueAsString(book);

                mockMvc.perform(post("/books/new").contentType(APPLICATION_JSON_UTF8).content(requestJson))
                                .andExpect(status().isOk()).andExpect(jsonPath("$.success").value(false));
        }

        @Test
        @WithMockUser(value = "test002", authorities = { "user" })
        public void testPutEditBookSuccess() throws Exception {
                Book book = new Book();
                book.setId(ID_BOOK_2);
                book.setTitle("Title");
                book.setIsbn("0-7645-2641-3");
                book.setPublicationYear(2019);
                book.setPublisher("PublisherTest");
                book.setGenres("Gastronomía,Cocina");
                book.setAuthor("Author Test");
                book.setDescription("Description Test");
                book.setImage("2");
                book.setStatus("COMO NUEVO");
                book.setUsername("test002");
                book.setPrice(10.);

                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
                ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
                String requestJson = ow.writeValueAsString(book);

                mockMvc.perform(put("/books/" + ID_BOOK_1 + "/edit").contentType(APPLICATION_JSON_UTF8)
                                .content(requestJson)).andExpect(status().isOk())
                                .andExpect(jsonPath("$.success").value(true));
        }

        @Test
        @WithMockUser(value = "test002", authorities = { "user" })
        public void testPutEditBookHasErrors() throws Exception {
                Book book = new Book();
                book.setId(ID_BOOK_2);
                book.setTitle("Title");
                book.setIsbn("0-7645-2641-3");
                book.setPublicationYear(-10);
                book.setPublisher("PublisherTest");
                book.setGenres("Gastronomía,Cocina");
                book.setAuthor("Author Test");
                book.setDescription("Description Test");
                book.setImage("0");
                book.setStatus("COMO NUEVO");
                book.setUsername("test002");
                book.setPrice(10.);

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
                MockHttpServletResponse response = this.mockMvc
                                .perform(get("/books/list/all-me?page=0&showMode=undefined")).andExpect(status().isOk())
                                .andReturn().getResponse();

                assertThat(response.getContentAsString()).contains(ID_BOOK_2);
        }

        @Test
        @WithMockUser(value = "test002", authorities = "user")
        public void testListMyBooksExceptWithAcceptedRequest() throws Exception {
                MockHttpServletResponse response = this.mockMvc.perform(get("/books/list/me-change"))
                                .andExpect(status().isOk()).andReturn().getResponse();

                assertThat(response.getContentAsString()).contains(ID_BOOK_2);
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
                                "\"Arte\",\"Diseño\",\"Autoayuda\",\"Esoterismo\",\"Ciencia\",\"Naturaleza\",\"Ciencias_Sociales\","
                                                + "\"Deportes\",\"Derecho\",\"Política\",\"Economía\",\"Negocios\",\"Ensayos\",\"Biografías\",\"Espectáculos\","
                                                + "\"Cine\",\"Música\",\"Filosofía\",\"Religión\",\"Gastronomía\",\"Cocina\",\"Historia\",\"Hogar\","
                                                + "\"Familia\",\"Ingeniería\",\"Tecnología\",\"Libros_Infantiles\",\"Libros_Juveniles\",\"Literatura\",\"Novela\","
                                                + "\"Medicina\",\"Salud\",\"Obras_de_Consulta\",\"Idiomas\",\"Ocio_y_tiempo_libre\",\"Poesía\",\"Viajes\","
                                                + "\"Geografía\",\"Otros\"");
        }

        @Test
        @WithMockUser(value = "test002", authorities = "user")
        public void testGetBookById() throws Exception {
                MockHttpServletResponse response = this.mockMvc.perform(get("/books/get/" + ID_BOOK_2))
                                .andExpect(status().isOk()).andReturn().getResponse();

                assertThat(response.getContentAsString()).contains(ID_BOOK_2);
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
        @WithMockUser(value = "test002", authorities = "user")
        public void testRecommendBooks() throws Exception {
                MockHttpServletResponse response = this.mockMvc.perform(get("/books/recommend"))
                                .andExpect(status().isOk()).andReturn().getResponse();

                assertThat(response.getContentAsString()).contains(ID_BOOK_2);
        }
}
