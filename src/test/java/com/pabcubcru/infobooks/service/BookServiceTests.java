package com.pabcubcru.infobooks.service;

import java.util.ArrayList;
import java.util.List;

import com.pabcubcru.infobooks.models.Book;
import com.pabcubcru.infobooks.models.Image;
import com.pabcubcru.infobooks.models.User;
import com.pabcubcru.infobooks.services.BookService;
import com.pabcubcru.infobooks.services.UserService;

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

    @Autowired
    private UserService userService;

    @Test
    public void shouldFindAll() throws Exception {
        PageRequest pageRequest = PageRequest.of(0, 20);
        Page<Book> books = this.bookService.findAll(pageRequest);
        
        Assertions.assertThat(books.getContent()).isNotEmpty();
    }

    @Test
    public void shouldFindMyBooks() throws Exception {
        PageRequest pageRequest = PageRequest.of(0, 20);
        Page<Book> books = this.bookService.findMyBooks("juan1234", pageRequest);

        Assertions.assertThat(books.getContent().size()).isEqualTo(1);
    }

    @Test
    public void shouldFindById() throws Exception {
        Book book = this.bookService.findBookById("book-002");
        Assertions.assertThat(book.getAuthor()).isEqualTo("Author Test");
        Assertions.assertThat(book.getUsername()).isEqualTo("test002");
    }

    @Test
    public void shouldDeleteById() throws Exception {
        this.bookService.deleteBookById("book-001");
        Book book = this.bookService.findBookById("book-001");
        Assertions.assertThat(book).isNull();
    }

    @Test
    public void shouldFindByIds() throws Exception {
        List<String> ids = new ArrayList<>();
        ids.add("book-001");
        ids.add("book-002");
        List<Book> books = this.bookService.findByIds(ids);
        Assertions.assertThat(books.size()).isEqualTo(2);
        Assertions.assertThat(books.get(0).getTitle()).isEqualTo("Title test 1");
        Assertions.assertThat(books.get(1).getTitle()).isEqualTo("Title test 2");
    }

    @Test
    public void shouldSaveValidateBook() throws Exception {
        PageRequest pageRequest = PageRequest.of(0, 20);
        Page<Book> books = this.bookService.findAll(pageRequest);
        Long numberOfBooksBefore = books.getTotalElements();

        Book book = new Book();

        book.setId("book-004");
        book.setTitle("Title test 1");
        book.setIsbn("0-7645-2641-3");
        book.setPublicationYear(2014);
        book.setPublisher("Publisher Test");
        book.setGenres("Comedia");
        book.setAuthor("Author Test");
        book.setDescription("Description test");
        book.setImage("https://i.ibb.co/YRy9kHC/paper.jpg");
        book.setStatus("COMO NUEVO");
        book.setPrice(10.);
        book.setUsername("test003");
        this.bookService.save(book);

        books = this.bookService.findAll(pageRequest);
        Long numberOfBooksAfter = books.getTotalElements();
        Assertions.assertThat(numberOfBooksAfter).isEqualTo(numberOfBooksBefore + 1);
    }

    @Test
    public void shouldEditValidateBook() throws Exception {
        PageRequest pageRequest = PageRequest.of(0, 20);
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
        PageRequest pageRequest = PageRequest.of(0, 20);
        User user = this.userService.findByUsername("pablo123");

        Page<Book> books = this.bookService.findNearBooks(user, pageRequest, showMode);

        Assertions.assertThat(books).isNotEmpty();
    }

    @Test
    public void shouldFindNearBooksProvince() throws Exception {
        String showMode = "province";
        this.findNearBooks(showMode);
    }

    @Test
    public void shouldFindNearBooksPostCode() throws Exception {
        String showMode = "postCode";
        this.findNearBooks(showMode);
    }

    @Test
    public void shouldFindNearBooksGenres() throws Exception {
        String showMode = "genres";
        this.findNearBooks(showMode);
    }

    @Test
    public void shouldFindNearBooksProvinceOrPostCode() throws Exception {
        String showMode = "undefined";
        this.findNearBooks(showMode);
    }

    @Test
    public void shouldFindBooksByUsername() throws Exception {
        List<Book> books = this.bookService.findByUsername("test002");

        Assertions.assertThat(books).isNotEmpty();
    }

    @Test
    public void shouldFindImagesByIdBook() throws Exception {
        List<Image> images = this.bookService.findImagesByIdBook("book-002");

        Assertions.assertThat(images).isNotEmpty();
    }

    @Test
    public void shouldFindPrincipalBookImage() throws Exception {
        Image image = this.bookService.findByIdBookAndPrincipalTrue("book-002");

        Assertions.assertThat(image).isNotNull();
    }

    @Test
    public void shouldSaveImage() throws Exception {
        Image image = new Image();
		image.setId("image-test");
		image.setUrlImage("https://imagessl1.casadellibro.com/a/l/t5/11/9788499926711.jpg");
		image.setIdBook("book-test-image");
		image.setFileName("image-test");
		image.setPrincipal(true);

		this.bookService.saveImage(image);

        List<Image> images = this.bookService.findImagesByIdBook("book-test-image");

        Assertions.assertThat(images).isNotEmpty();
    }

    @Test
    public void shouldDeleteImage() throws Exception {
        Image image = new Image();
		image.setId("image-test-delete");
		image.setUrlImage("https://imagessl1.casadellibro.com/a/l/t5/11/9788499926711.jpg");
		image.setIdBook("book-test-image");
		image.setFileName("image-test");
		image.setPrincipal(true);

		this.bookService.saveImage(image);

        Image i = this.bookService.findImageById("image-test-delete");
        Assertions.assertThat(i).isNotNull();

        this.bookService.deleteImage(image);

        i = this.bookService.findImageById("image-test-delete");
        Assertions.assertThat(i).isNull();
    }

    @Test
    public void shouldDeteleAllImages() throws Exception {
        Image image = new Image();
		image.setId("image-test-delete-1");
		image.setUrlImage("https://imagessl1.casadellibro.com/a/l/t5/11/9788499926711.jpg");
		image.setIdBook("book-test-image");
		image.setFileName("image-test");
		image.setPrincipal(true);

		this.bookService.saveImage(image);

		image.setId("image-test-delete-2");

		this.bookService.saveImage(image);

        List<Image> images = this.bookService.findImagesByIdBook("book-test-image");

        Assertions.assertThat(images).isNotEmpty();

        this.bookService.deleteAllImages(images); 

        images = this.bookService.findImagesByIdBook("book-test-image");

        Assertions.assertThat(images).isEmpty();
    }

    @Test
    public void shouldDeleteImageById() throws Exception {
        Image image = new Image();
		image.setId("image-test-delete");
		image.setUrlImage("https://imagessl1.casadellibro.com/a/l/t5/11/9788499926711.jpg");
		image.setIdBook("book-test-image");
		image.setFileName("image-test");
		image.setPrincipal(true);

		this.bookService.saveImage(image);

        Image i = this.bookService.findImageById("image-test-delete");
        Assertions.assertThat(i).isNotNull();

        this.bookService.deleteImageById("image-test-delete");

        i = this.bookService.findImageById("image-test-delete");
        Assertions.assertThat(i).isNull();
    }

}
