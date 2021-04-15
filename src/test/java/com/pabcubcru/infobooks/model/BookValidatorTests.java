package com.pabcubcru.infobooks.model;

import java.util.Locale;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import com.pabcubcru.infobooks.models.Book;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

public class BookValidatorTests {

    private Validator createValidator() {
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();

        return validator;
    }

    @Test
    void errorWhenFieldsAreNullOrBlank() {
        LocaleContextHolder.setLocale(Locale.ENGLISH);
        Book book = new Book();

        Validator validator = createValidator();
        Set<ConstraintViolation<Book>> constrains = validator.validate(book);
        Assertions.assertThat(constrains.size()).isEqualTo(10); //Son 10 campos con @NotNull o @NotBlank
    }

    @Test
    void errorWhenTitleAndPublisherAreBlank() {
        LocaleContextHolder.setLocale(Locale.ENGLISH);
        Book book = new Book();

        book.setTitle(""); //Título en blanco
        book.setIsbn("0-7645-2641-3");
        book.setPublicationYear(2014);
        book.setPublisher(""); //Editorial en blanco
        book.setGenres("Comedia,Aventuras");
        book.setAuthor("Author Test");
        book.setDescription("Description Test");
        book.setImage("https://i.pinimg.com/originals/8d/23/06/8d2306b98839234e49ce96a8b76e20ae.jpg");
        book.setStatus("COMO NUEVO");
        book.setAction("INTERCAMBIO");
        book.setUsername("test001");

        Validator validator = createValidator();
        Set<ConstraintViolation<Book>> constrains = validator.validate(book);
        Assertions.assertThat(constrains.size()).isEqualTo(2);        
    }

    @Test
    void errorWhenGenresAndAuthorAreBlank() {
        LocaleContextHolder.setLocale(Locale.ENGLISH);
        Book book = new Book();

        book.setTitle("Title test");
        book.setIsbn("0-7645-2641-3");
        book.setPublicationYear(2014);
        book.setPublisher("Publisher Test");
        book.setGenres(""); //Géneros en blanco
        book.setAuthor(""); //Autor en blanco
        book.setDescription("Description Test");
        book.setImage("https://i.pinimg.com/originals/8d/23/06/8d2306b98839234e49ce96a8b76e20ae.jpg");
        book.setStatus("COMO NUEVO");
        book.setAction("INTERCAMBIO");
        book.setUsername("test001");

        Validator validator = createValidator();
        Set<ConstraintViolation<Book>> constrains = validator.validate(book);
        Assertions.assertThat(constrains.size()).isEqualTo(2);
    }

    @Test
    void errorWhenDescriptionAndStatusAreBlank() {
        LocaleContextHolder.setLocale(Locale.ENGLISH);
        Book book = new Book();

        book.setTitle("Title test");
        book.setIsbn("0-7645-2641-3");
        book.setPublicationYear(2014);
        book.setPublisher("Publisher Test");
        book.setGenres("Comedia");
        book.setAuthor("Author Test"); 
        book.setDescription(""); //Descripción en blanco
        book.setImage("https://i.pinimg.com/originals/8d/23/06/8d2306b98839234e49ce96a8b76e20ae.jpg");
        book.setStatus(""); //Estado en blanco
        book.setAction("INTERCAMBIO");
        book.setUsername("test001");

        Validator validator = createValidator();
        Set<ConstraintViolation<Book>> constrains = validator.validate(book);
        Assertions.assertThat(constrains.size()).isEqualTo(2);
    }

    @Test
    void errorWhenActionBlank() {
        LocaleContextHolder.setLocale(Locale.ENGLISH);
        Book book = new Book();

        book.setTitle("Title test");
        book.setIsbn("0-7645-2641-3");
        book.setPublicationYear(2014);
        book.setPublisher("Publisher Test");
        book.setGenres("Comedia");
        book.setAuthor("Author Test"); 
        book.setDescription("Description test"); 
        book.setImage("https://i.pinimg.com/originals/8d/23/06/8d2306b98839234e49ce96a8b76e20ae.jpg");
        book.setStatus("COMO NUEVO"); 
        book.setAction(""); //Action en blanco
        book.setUsername("test001");

        Validator validator = createValidator();
        Set<ConstraintViolation<Book>> constrains = validator.validate(book);
        Assertions.assertThat(constrains.size()).isEqualTo(1);
    }

    @Test
    void errorWhenISBNIsNotValid() {
        LocaleContextHolder.setLocale(Locale.ENGLISH);
        Book book = new Book();

        book.setTitle("Title test");
        book.setIsbn("0-7645"); //ISBN no válido
        book.setPublicationYear(2014);
        book.setPublisher("Publisher Test");
        book.setGenres("Comedia");
        book.setAuthor("Author Test"); 
        book.setDescription("Description test"); 
        book.setImage("https://i.pinimg.com/originals/8d/23/06/8d2306b98839234e49ce96a8b76e20ae.jpg");
        book.setStatus("COMO NUEVO"); 
        book.setAction("INTERCAMBIO");
        book.setUsername("test001");

        Validator validator = createValidator();
        Set<ConstraintViolation<Book>> constrains = validator.validate(book);
        Assertions.assertThat(constrains.size()).isEqualTo(1);
        ConstraintViolation<Book> violation = constrains.iterator().next();
        Assertions.assertThat(violation.getPropertyPath().toString()).isEqualTo("isbn");
        Assertions.assertThat(violation.getMessage()).isEqualTo("El ISBN no es válido.");
    }

    @Test
    void errorWhenPublicationYearIsInFuture() {
        LocaleContextHolder.setLocale(Locale.ENGLISH);
        Book book = new Book();

        book.setTitle("Title test");
        book.setIsbn("0-7645-2641-3");
        book.setPublicationYear(2030); //Año en futuro
        book.setPublisher("Publisher Test");
        book.setGenres("Comedia");
        book.setAuthor("Author Test"); 
        book.setDescription("Description test"); 
        book.setImage("https://i.pinimg.com/originals/8d/23/06/8d2306b98839234e49ce96a8b76e20ae.jpg");
        book.setStatus("COMO NUEVO"); 
        book.setAction("INTERCAMBIO");
        book.setUsername("test001");

        Validator validator = createValidator();
        Set<ConstraintViolation<Book>> constrains = validator.validate(book);
        Assertions.assertThat(constrains.size()).isEqualTo(1);
        ConstraintViolation<Book> violation = constrains.iterator().next();
        Assertions.assertThat(violation.getPropertyPath().toString()).isEqualTo("publicationYear");
        Assertions.assertThat(violation.getMessage()).isEqualTo("El año de publicación debe ser anterior o igual al presente año.");
    }

    @Test
    void errorWhenUrlImageIsNotValid() {
        LocaleContextHolder.setLocale(Locale.ENGLISH);
        Book book = new Book();

        book.setTitle("Title test");
        book.setIsbn("0-7645-2641-3");
        book.setPublicationYear(2014); 
        book.setPublisher("Publisher Test");
        book.setGenres("Comedia");
        book.setAuthor("Author Test"); 
        book.setDescription("Description test"); 
        book.setImage("pini/originals/8d/23/06/8d2306b98839234e49ce96a8b76e20a"); //URL no válida
        book.setStatus("COMO NUEVO"); 
        book.setAction("INTERCAMBIO");
        book.setUsername("test001");

        Validator validator = createValidator();
        Set<ConstraintViolation<Book>> constrains = validator.validate(book);
        Assertions.assertThat(constrains.size()).isEqualTo(1);
        ConstraintViolation<Book> violation = constrains.iterator().next();
        Assertions.assertThat(violation.getPropertyPath().toString()).isEqualTo("image");
        Assertions.assertThat(violation.getMessage()).isEqualTo("La URL de la imagen no es válida.");
    }   
}