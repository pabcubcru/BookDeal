package com.pabcubcru.bookdeal.model;

import java.time.LocalDate;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import com.pabcubcru.bookdeal.models.User;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

public class UserValidatorTests {

    private Validator createValidator() throws Exception {
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();

        return validator;
    }

    @ParameterizedTest
    @CsvFileSource(resources = "../csv/model/Users.csv", encoding = "utf-8", numLinesToSkip = 1, delimiterString = ";")
    void errorWhenFieldIsNullOrBlankOrNotValid(String name, String email, String phone, LocalDate birthDate,
            String province, String postCode, String genres, String username, String password, Boolean enabled,
            Boolean accept, String confirmPassword) throws Exception {

        User user = new User(name, email, phone, birthDate, province, postCode, genres, username, password, enabled,
                accept, confirmPassword);

        Validator validator = createValidator();
        Set<ConstraintViolation<User>> constrains = validator.validate(user);
        if(constrains.size() > 1) {
            System.out.println(constrains);
            System.out.println(user.getUsername());
        }

        if(constrains.size() < 1) {
            System.out.println("----------------" + user.getUsername());
        }
        Assertions.assertThat(constrains.size()).isEqualTo(1);
    }
}
