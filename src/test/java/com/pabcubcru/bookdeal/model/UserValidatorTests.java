package com.pabcubcru.bookdeal.model;

import java.time.LocalDate;
import java.util.Locale;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import com.pabcubcru.bookdeal.models.User;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

public class UserValidatorTests {

    private Validator createValidator() throws Exception {
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();

        return validator;
    }

    /*@ParameterizedTest
    @CsvFileSource(resources = "../csv/model/Users.csv", encoding = "utf-8", numLinesToSkip = 1, delimiterString = ";")
    void errorWhenFieldIsNullOrBlankOrNotValid() throws Exception {

        Validator validator = createValidator();
        Set<ConstraintViolation<User>> constrains = validator.validate(user);
        Assertions.assertThat(constrains.size()).isEqualTo(1);*/
    }
}
