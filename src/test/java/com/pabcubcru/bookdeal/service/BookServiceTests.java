package com.pabcubcru.bookdeal.service;

import java.util.ArrayList;
import java.util.List;

import com.pabcubcru.bookdeal.models.Book;
import com.pabcubcru.bookdeal.models.Image;
import com.pabcubcru.bookdeal.models.User;
import com.pabcubcru.bookdeal.services.BookService;
import com.pabcubcru.bookdeal.services.UserService;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
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

    @Autowired
    private UserService userService;

    @Test
    @Order(1)
    public void shouldFindAll() throws Exception {
        PageRequest pageRequest = PageRequest.of(0, 21);
        Page<Book> books = this.bookService.findAll(pageRequest);
        Integer numBooks = this.bookService.countBooks();

        Assertions.assertThat(Integer.parseInt("" + books.getTotalElements())).isEqualTo(numBooks);
    }

    @Test
    @Order(2)
    public void shouldFindMyBooks() throws Exception {
        PageRequest pageRequest = PageRequest.of(0, 21);
        Page<Book> books = this.bookService.findMyBooks("juan1234", pageRequest);

        Assertions.assertThat(books.getContent().size()).isEqualTo(1);
    }

    @Test
    @Order(3)
    public void shouldFindById() throws Exception {
        Book book = this.bookService.findBookById("book-002");
        Assertions.assertThat(book.getAuthor()).isEqualTo("Author Test");
        Assertions.assertThat(book.getUsername()).isEqualTo("test002");
    }

    @Test
    @Order(4)
    public void shouldDeleteById() throws Exception {
        this.bookService.deleteBookById("book-001");
        Book book = this.bookService.findBookById("book-001");
        Assertions.assertThat(book).isNull();
    }

    @Test
    @Order(5)
    public void shouldFindByIds() throws Exception {
        List<String> ids = new ArrayList<>();
        ids.add("book-001");
        ids.add("book-002");
        List<Book> books = this.bookService.findByIds(ids);
        Assertions.assertThat(books.size()).isEqualTo(2);
        Assertions.assertThat(books.get(0).getTitle()).isEqualTo("Title test 1");
        Assertions.assertThat(books.get(1).getTitle()).isEqualTo("Title test 2");
    }

    @ParameterizedTest
    @CsvFileSource(resources = "../csv/services/Books.csv", encoding = "utf-8", numLinesToSkip = 1, delimiterString = ";")
    @Order(6)
    public void shouldSaveValidateBook(String title, String originalTitle, String isbn, Integer publicationYear,
            String publisher, String genres, String author, String description, String status, Double price,
            String username, String image) throws Exception {
        PageRequest pageRequest = PageRequest.of(0, 21);
        Page<Book> books = this.bookService.findAll(pageRequest);
        Long numberOfBooksBefore = books.getTotalElements();

        Book book = new Book(title, originalTitle, isbn, publicationYear, publisher, genres, author, description,
                status, price, username, image);
        this.bookService.save(book);

        books = this.bookService.findAll(pageRequest);
        Long numberOfBooksAfter = books.getTotalElements();
        Assertions.assertThat(numberOfBooksAfter).isEqualTo(numberOfBooksBefore + 1);
    }

    @Test
    @Order(7)
    public void shouldEditValidateBook() throws Exception {
        PageRequest pageRequest = PageRequest.of(0, 21);
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

    private void findNearBooks(String showMode) {
        PageRequest pageRequest = PageRequest.of(0, 21);
        User user = this.userService.findByUsername("pablo123");

        Page<Book> books = this.bookService.findNearBooks(user, pageRequest, showMode);

        Assertions.assertThat(books).isNotEmpty();
    }

    @Test
    @Order(8)
    public void shouldFindNearBooksProvince() throws Exception {
        String showMode = "province";
        this.findNearBooks(showMode);
    }

    @Test
    @Order(9)
    public void shouldFindNearBooksPostCode() throws Exception {
        String showMode = "postCode";
        this.findNearBooks(showMode);
    }

    @Test
    @Order(10)
    public void shouldFindNearBooksGenres() throws Exception {
        String showMode = "genres";
        this.findNearBooks(showMode);
    }

    @Test
    @Order(11)
    public void shouldFindNearBooksProvinceOrPostCode() throws Exception {
        String showMode = "undefined";
        this.findNearBooks(showMode);
    }

    @Test
    @Order(12)
    public void shouldFindBooksByUsername() throws Exception {
        List<Book> books = this.bookService.findByUsername("test002");

        Assertions.assertThat(books).isNotEmpty();
    }

    @Test
    @Order(13)
    public void shouldFindImagesByIdBook() throws Exception {
        List<Image> images = this.bookService.findImagesByIdBook("book-002");

        Assertions.assertThat(images).isNotEmpty();
    }

    @Test
    @Order(14)
    public void shouldFindPrincipalBookImage() throws Exception {
        Image image = this.bookService.findByIdBookAndPrincipalTrue("book-002");

        Assertions.assertThat(image).isNotNull();
    }

    @ParameterizedTest
    @CsvFileSource(resources = "../csv/services/Images.csv", encoding = "utf-8", numLinesToSkip = 1, delimiterString = ";")
    @Order(15)
    public void shouldSaveImage(String fileName, String idBook, String urlImage, Boolean principal) throws Exception {
        List<Image> images = this.bookService.findImagesByIdBook(idBook);
        Integer numImagesBefore = images.size();
        Image image = new Image(fileName, idBook, urlImage, principal);
        this.bookService.saveImage(image);

        images = this.bookService.findImagesByIdBook(idBook);
        Integer numImagesAfter = images.size();
        Assertions.assertThat(images).isNotEmpty();
        Assertions.assertThat(numImagesAfter).isEqualTo(numImagesBefore + 1);
    }

    @Test
    @Order(16)
    public void shouldDeleteImage() throws Exception {
        String idBook = "book001";
        List<Image> images = this.bookService.findImagesByIdBook(idBook);
        Image img = images.get(0);
        this.bookService.deleteImage(img);

        Image i = this.bookService.findImageById(img.getId());
        Assertions.assertThat(i).isNull();

        images = this.bookService.findImagesByIdBook(idBook);
        Assertions.assertThat(images).isNotEmpty();
        i = images.get(0);
        this.bookService.deleteImageById(i.getId());

        Image im = this.bookService.findImageById(i.getId());
        Assertions.assertThat(im).isNull();

        images = this.bookService.findImagesByIdBook(idBook);
        Assertions.assertThat(images).isNotEmpty();
        this.bookService.deleteAllImages(images);

        images = this.bookService.findImagesByIdBook(idBook);
        Assertions.assertThat(images).isEmpty();
    }
}
