package com.pabcubcru.infobooks.controller;

import java.util.ArrayList;
import java.util.List;

import com.pabcubcru.infobooks.models.Book;
import com.pabcubcru.infobooks.models.Request;
import com.pabcubcru.infobooks.models.RequestStatus;
import com.pabcubcru.infobooks.services.BookService;
import com.pabcubcru.infobooks.services.RequestService;
import com.pabcubcru.infobooks.services.UserFavouriteBookService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class BookControllerTests {
    
    @Autowired
    private MockMvc mockMvc;

    private final String ID_BOOK_1 = "book001";
    private final String ID_BOOK_2 = "book002";

    @MockBean
    private BookService bookService;

    @MockBean
    private RequestService requestService;

    @MockBean
    private UserFavouriteBookService userFavouriteBookService;

    @BeforeEach
    void setup() {
        
        Book book = new Book();
        //book.setId(ID_BOOK_1);
        book.setTitle("Title");
        book.setIsbn("0-7645-2641-3");
        book.setPublicationYear(2014);
        book.setPublisher("Publisher");
        book.setGenres("Comedia,Aventuras");
        book.setAuthor("Author Test");
        book.setDescription("Description Test");
        book.setImage("https://i.pinimg.com/originals/8d/23/06/8d2306b98839234e49ce96a8b76e20ae.jpg");
        book.setStatus("COMO NUEVO");
        book.setUsername("test001");

        Book book2 = new Book();
        book.setUsername("test002");
        //book.setId(ID_BOOK_2);

        List<Request> requests = new ArrayList<Request>();

        List<Book> listTest001 = new ArrayList<>();
        listTest001.add(new Book());
        Page<Book> booksTest001 = new PageImpl<>(listTest001);
        

        BDDMockito.given(this.bookService.findBookById(ID_BOOK_1)).willReturn(book);
        BDDMockito.given(this.bookService.findBookById(ID_BOOK_2)).willReturn(book2);
        BDDMockito.given(this.bookService.findAllExceptMine("test001", PageRequest.of(1, 21))).willReturn(booksTest001);
        //BDDMockito.given(this.bookService.findAllExceptMine("test001", PageRequest.of(1, 21)).getContent()).willReturn(listTest001);
        //BDDMockito.given(this.bookService.findAllExceptMine("test001", PageRequest.of(1, 21)).getTotalPages()).willReturn(10);
        BDDMockito.given(this.requestService.findByIdBook1OrIdBook2(ID_BOOK_1, ID_BOOK_1)).willReturn(requests);
        BDDMockito.given(this.requestService.findFirstByIdBook1AndStatus(ID_BOOK_1, RequestStatus.ACEPTADA.toString())).willReturn(null);
        BDDMockito.given(this.requestService.findFirstByIdBook2AndStatus(ID_BOOK_1, RequestStatus.ACEPTADA.toString())).willReturn(null);
        BDDMockito.given(this.userFavouriteBookService.findByUsernameAndBookId("test001", ID_BOOK_1)).willReturn(null);
    }

    @Test
    @WithMockUser(value = "user1", authorities="usuario")
    public void testMain() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/books/new"))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.view().name("Main"));
    }

    @Test
    @WithMockUser(value = "user1", authorities="usuario")
    public void testMainWithSecurity() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/books/{id}", ID_BOOK_1))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.view().name("Main"));
    }

    @Test
    @WithMockUser(value = "user1", authorities="usuario")
    public void testMainWithSecurityError404() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/books/booktest"))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.view().name("errors/Error404"));
    }

    /*@Test
    @WithMockUser(value = "test001", authorities="usuario")
    public void testMainWithUserSecurity() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/books/book001/edit"))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.view().name("Main"));
    }*/

    @Test
    @WithMockUser(value = "user1", authorities="usuario")
    public void testMainWithUserSecurityError404() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/books/booktest/edit"))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.view().name("errors/Error404"));
    }

    @Test
    @WithMockUser(value = "user1", authorities="usuario")
    public void testMainWithUserSecurityError403() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/books/{id}/edit", ID_BOOK_1))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.view().name("errors/Error403"));
    }

    /*@Test
    @WithMockUser(value = "test001", authorities = "usuario")
    public void testPostEditBook() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.put("/books/{id}/edit", ID_BOOK_1).with(SecurityMockMvcRequestPostProcessors.csrf())
        .param("id", ID_BOOK_1).param("title", "Title").param("isbn", "0-7645-2641-3").param("originalTitle", "")
        .param("publicationYear", "2015").param("publisher", "Publisher").param("genres", "Comedia,Aventuras")
        .param("author", "Author").param("description", "Description").param("image", "https://i.pinimg.com/originals/8d/23/06/8d2306b98839234e49ce96a8b76e20ae.jpg")
        .param("status", "POCO USADO").param("action", "INTERCAMBIO").param("price", "").param("username", ""))
        .andExpect(MockMvcResultMatchers.status().isOk());
    }*/

    /*@Test
    @WithMockUser(value = "user1", authorities = "usuario")
    public void testPostCreateBook() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post("/books/new").with(SecurityMockMvcRequestPostProcessors.csrf())
        .param("title", "Title").param("isbn", "0-7645-2641-3").param("originalTitle", "")
        .param("publicationYear", "2015").param("publisher", "Publisher").param("genres", "Comedia,Aventuras")
        .param("author", "Author").param("description", "Description").param("image", "https://i.pinimg.com/originals/8d/23/06/8d2306b98839234e49ce96a8b76e20ae.jpg")
        .param("status", "POCO USADO").param("action", "INTERCAMBIO").param("price", ""))
        .andExpect(MockMvcResultMatchers.status().isOk());
    }*/

    @Test
    @WithMockUser(value = "user1", authorities = "usuario")
    public void testPostCreateBookError() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post("/books/new").with(SecurityMockMvcRequestPostProcessors.csrf())
        .param("title", "Title").param("isbn", "0-7645-2641-3")
        .param("publicationYear", "2015").param("publisher", "Publisher").param("genres", "Comedia,Aventuras")
        .param("author", "Author").param("description", "Description").param("image", "https://i.pinimg.com/originals/8d/23/06/8d2306b98839234e49ce96a8b76e20ae.jpg")
        .param("status", "POCO USADO").param("action", ""))
        .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    /*@Test
    @WithMockUser(value = "test002", authorities = "usuario")
    public void testDeleteBook() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/books/{id}/delete", ID_BOOK_2))
        .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    }*/

    /*@Test
    @WithMockUser(value = "test001", authorities = "usuario")
    public void testListAllExceptMyBooks() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/books/list/all-me"))
        .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    }*/


    
}
