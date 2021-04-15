package com.pabcubcru.infobooks.service;

import java.util.List;

import com.pabcubcru.infobooks.models.Book;
import com.pabcubcru.infobooks.services.BookService;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class BookServiceTests {

    @Autowired
    private BookService bookService;

    @Test
    public void shouldFindAll() throws Exception {
        PageRequest pageRequest = PageRequest.of(1, 20);
        Page<Book> books = this.bookService.findAll(pageRequest);
        Long numberOfBooks = books.getTotalElements();
        Assertions.assertThat(numberOfBooks).isEqualTo(863L); //863 = 861 de dataset y 2 para tests
    }

    @Test
    public void shouldFindMyBooks() throws Exception {
        PageRequest pageRequest = PageRequest.of(1, 20);
        Page<Book> books = this.bookService.findMyBooks("pablo123", pageRequest);
        String username = books.getContent().get(10).getUsername();
        Assertions.assertThat(username).isEqualTo("pablo123");
    }

    @Test
    public void shouldFindById() throws Exception {
        Book book = this.bookService.findBookById("book-001");
        Assertions.assertThat(book.getAuthor()).isEqualTo("Author Test");
        Assertions.assertThat(book.getUsername()).isEqualTo("test001");
    }

    @Test
    public void shouldDeleteById() throws Exception {
        this.bookService.deleteBookById("book-001");
        Book book = this.bookService.findBookById("book-001");
        Assertions.assertThat(book).isNull();
    }

    @Test
    public void shouldFindByIds() throws Exception {
        List<Book> books = this.bookService.findByIds(List.of("book-001", "book-002"));
        Assertions.assertThat(books.size()).isEqualTo(2);
        Assertions.assertThat(books.get(0).getTitle()).isEqualTo("Title test 1");
        Assertions.assertThat(books.get(1).getTitle()).isEqualTo("Title test 2");
    }

    @Test
    public void shouldSaveValidateBook() throws Exception {
        PageRequest pageRequest = PageRequest.of(1, 20);
        Page<Book> books = this.bookService.findAll(pageRequest);
        Long numberOfBooksBefore = books.getTotalElements();
        
        Book book = new Book();

		book.setId("book-003");
        book.setTitle("Title test 1");
        book.setIsbn("0-7645-2641-3");
        book.setPublicationYear(2014);
        book.setPublisher("Publisher Test");
        book.setGenres("Comedia");
        book.setAuthor("Author Test"); 
        book.setDescription("Description test"); 
        book.setImage("https://i.pinimg.com/originals/8d/23/06/8d2306b98839234e49ce96a8b76e20ae.jpg");
        book.setStatus("COMO NUEVO"); 
        book.setAction("VENTA"); 
		book.setPrice(10.);
        book.setUsername("test003");
		this.bookService.save(book);

        books = this.bookService.findAll(pageRequest);
        Long numberOfBooksAfter = books.getTotalElements();
        Assertions.assertThat(numberOfBooksAfter).isEqualTo(numberOfBooksBefore+1);
    }

    @Test
    public void shouldEditValidateBook() throws Exception {
        PageRequest pageRequest = PageRequest.of(1, 20);
        Page<Book> books = this.bookService.findAll(pageRequest);
        Long numberOfBooksBefore = books.getTotalElements();

        Book book = this.bookService.findBookById("book-002");
        book.setDescription("New description");
        this.bookService.save(book);

        book = this.bookService.findBookById("book-002");
        Assertions.assertThat(book.getDescription()).isEqualTo("New description");

        books = this.bookService.findAll(pageRequest);
        Long numberOfBooksAfter = books.getTotalElements();
        Assertions.assertThat(numberOfBooksAfter).isEqualTo(numberOfBooksBefore);
    }
    
}
