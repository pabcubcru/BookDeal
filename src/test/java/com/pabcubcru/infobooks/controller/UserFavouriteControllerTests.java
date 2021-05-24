package com.pabcubcru.infobooks.controller;

import com.pabcubcru.infobooks.models.Book;
import com.pabcubcru.infobooks.models.Image;
import com.pabcubcru.infobooks.models.UserFavouriteBook;
import com.pabcubcru.infobooks.services.BookService;
import com.pabcubcru.infobooks.services.UserFavouriteBookService;

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
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserFavouriteControllerTests {

    private final String ID_BOOK_1 = "book001";
    private final String ID_BOOK_2 = "book002";
    private final String ID_USER_1 = "test001";
    private final String ID_IMAGE_1 = "image001";

    @MockBean
    private UserFavouriteBookService userFavouriteBookService;

    @MockBean
    private BookService bookService;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        UserFavouriteBook ufb = new UserFavouriteBook();
        ufb.setBookId(ID_BOOK_1);
        ufb.setUsername(ID_USER_1);

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

        Image image = new Image();
        image.setId(ID_IMAGE_1);
        image.setUrlImage("https://i.ibb.co/YRy9kHC/paper.jpg");
        image.setIdBook(ID_BOOK_1);
        image.setPrincipal(true);

        List<UserFavouriteBook> listUfb = new ArrayList<>();
        listUfb.add(ufb);
        Page<UserFavouriteBook> page = new PageImpl<>(listUfb);
        given(this.userFavouriteBookService.findAllByUsername("test001", PageRequest.of(0, 12))).willReturn(page);

        List<String> idBooks = new ArrayList<>();
        idBooks.add(ID_BOOK_1);
        List<Book> books = new ArrayList<>();
        books.add(book);
        given(this.bookService.findByIds(idBooks)).willReturn(books);

        given(this.bookService.findByIdBookAndPrincipalTrue(ID_BOOK_1)).willReturn(image);

        given(this.userFavouriteBookService.findByUsernameAndBookId(ID_USER_1, ID_BOOK_2)).willReturn(null);

        given(this.userFavouriteBookService.findByUsernameAndBookId(ID_USER_1, ID_BOOK_1)).willReturn(ufb);
    }

    @Test
    @WithMockUser(value = "test001", authorities = "user")
    public void testMain() throws Exception {
        this.mockMvc.perform(get("/favourites/0")).andExpect(status().isOk()).andExpect(view().name("Main"));
    }

    @Test
    @WithMockUser(value = "test001", authorities = "user")
    public void testFindAllByUsername() throws Exception {
        MockHttpServletResponse response = this.mockMvc.perform(get("/favourites/all?page=0"))
                .andExpect(status().isOk()).andReturn().getResponse();

        String content = response.getContentAsString();

        assertThat(content).contains(ID_BOOK_1);
        assertThat(content).contains("https://i.ibb.co/YRy9kHC/paper.jpg");
    }

    @Test
    @WithMockUser(value = "test001", authorities = "user")
    public void testAddFavouriteBook() throws Exception {
        this.mockMvc.perform(get("/favourites/" + ID_BOOK_2 + "/add")).andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    @WithMockUser(value = "test001", authorities = "user")
    public void testIsAlreadyFavouriteBook() throws Exception {
        this.mockMvc.perform(get("/favourites/" + ID_BOOK_1 + "/add")).andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(false)).andExpect(jsonPath("$.alreadyAdded").value(true));
    }

    @Test
    @WithMockUser(value = "test001", authorities = "user")
    public void testDeleteFavouriteBook() throws Exception {
        this.mockMvc.perform(delete("/favourites/" + ID_BOOK_1 + "/delete")).andExpect(status().isOk());
    }

}
