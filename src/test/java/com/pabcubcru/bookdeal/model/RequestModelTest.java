package com.pabcubcru.bookdeal.model;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import com.pabcubcru.bookdeal.models.Request;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

public class RequestModelTest {

    private Validator createValidator() throws Exception {
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();

        return validator;
    }

    @ParameterizedTest
    @CsvFileSource(resources = "../csv/model/Requests.csv", encoding = "utf-8", numLinesToSkip = 1, delimiterString = ";")
    void errorWhenFieldIsBlankOrNullOrNotValid(String username1, String username2, String idBook1, String idBook2, String status,
    String action, Double pay, String comment) throws Exception {

        Request request = new Request(username1, username2, idBook1, idBook2, status, action, pay, comment);

        Validator validator = createValidator();
        Set<ConstraintViolation<Request>> constrains = validator.validate(request);
        Assertions.assertThat(constrains.size()).isEqualTo(1);
    }
    
}
