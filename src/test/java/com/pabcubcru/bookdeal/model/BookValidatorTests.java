package com.pabcubcru.bookdeal.model;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import com.pabcubcru.bookdeal.models.Book;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

public class BookValidatorTests {

    private Validator createValidator() throws Exception {
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();

        return validator;
    }

    @ParameterizedTest
    @CsvFileSource(resources = "../csv/model/Books.csv", encoding = "utf-8", numLinesToSkip = 1, delimiterString = ";")
    void errorWhenFieldIsBlankOrNullOrNotValid(String title, String originalTitle, String isbn, Integer publicationYear,
            String publisher, String genres, String author, String description, String status, Double price,
            String username, String image) throws Exception {

        Book book = new Book(title, originalTitle, isbn, publicationYear, publisher, genres, author, description,
                status, price, username, image);

        Validator validator = createValidator();
        Set<ConstraintViolation<Book>> constrains = validator.validate(book);
        if(constrains.size() > 1) {
            System.out.println(constrains);
        }
        Assertions.assertThat(constrains.size()).isEqualTo(1);
    }
}
