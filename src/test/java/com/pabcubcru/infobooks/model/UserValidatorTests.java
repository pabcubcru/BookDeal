package com.pabcubcru.infobooks.model;

import java.time.LocalDate;
import java.util.Locale;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import com.pabcubcru.infobooks.models.User;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

public class UserValidatorTests {
    
    private Validator createValidator() throws Exception {
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();

        return validator;
    }

    @Test
    void errorWhenFieldsAreNullOrBlank() throws Exception {
        LocaleContextHolder.setLocale(Locale.ENGLISH);
        User user = new User();

        Validator validator = createValidator();
        Set<ConstraintViolation<User>> constrains = validator.validate(user);
        Assertions.assertThat(constrains.size()).isEqualTo(5); //Son 5 campos con @NotNull o @NotBlank
    }

    @Test
    void errorWhenEmailIsNotValid() throws Exception {
        LocaleContextHolder.setLocale(Locale.ENGLISH);
        User user = new User();

        user.setName("Test");
        user.setEmail("email no valid"); //No contiene @ ni es correcto
        user.setPhone("+34654987321");
        user.setBirthDate(LocalDate.of(2020, 11, 23));
        user.setProvince("Province");
        user.setPostCode("41012");
        user.setUsername("username");
        user.setPassword(new BCryptPasswordEncoder().encode("password123"));
        user.setEnabled(true);

        Validator validator = createValidator();
        Set<ConstraintViolation<User>> constrains = validator.validate(user);
        Assertions.assertThat(constrains.size()).isEqualTo(1);
        ConstraintViolation<User> violation = constrains.iterator().next();
        Assertions.assertThat(violation.getPropertyPath().toString()).isEqualTo("email");
        Assertions.assertThat(violation.getMessage()).isEqualTo("Debe ser un email válido.");
    }

    @Test
    void errorWhenPhoneHasNotCorrectPattern() throws Exception {
        LocaleContextHolder.setLocale(Locale.ENGLISH);
        User user = new User();

        user.setName("Test");
        user.setEmail("email@email.com");
        user.setPhone("87321"); //No tiene 9 dígitos ni tiene un formato válido
        user.setBirthDate(LocalDate.of(2020, 11, 23));
        user.setProvince("Province");
        user.setPostCode("41012");
        user.setUsername("username");
        user.setPassword(new BCryptPasswordEncoder().encode("password123"));
        user.setEnabled(true);

        Validator validator = createValidator();
        Set<ConstraintViolation<User>> constrains = validator.validate(user);
        Assertions.assertThat(constrains.size()).isEqualTo(1);
        ConstraintViolation<User> violation = constrains.iterator().next();
        Assertions.assertThat(violation.getPropertyPath().toString()).isEqualTo("phone");
        Assertions.assertThat(violation.getMessage()).isEqualTo("El número de teléfono no es válido.");
    }

    @Test
    void errorWhenBirthDateIsNotPast() throws Exception {
        LocaleContextHolder.setLocale(Locale.ENGLISH);
        User user = new User();

        user.setName("Test");
        user.setEmail("email@email.com");
        user.setPhone("+34654987321");
        user.setBirthDate(LocalDate.of(2022, 11, 23)); //Fecha en futuro
        user.setProvince("Province");
        user.setPostCode("41012");
        user.setUsername("username");
        user.setPassword(new BCryptPasswordEncoder().encode("password123"));
        user.setEnabled(true);

        Validator validator = createValidator();
        Set<ConstraintViolation<User>> constrains = validator.validate(user);
        Assertions.assertThat(constrains.size()).isEqualTo(1);
        ConstraintViolation<User> violation = constrains.iterator().next();
        Assertions.assertThat(violation.getPropertyPath().toString()).isEqualTo("birthDate");
        Assertions.assertThat(violation.getMessage()).isEqualTo("La fecha de nacimiento debe ser anterior a la fecha actual.");
    }

    @Test
    void errorWhenPostCodeHasNotCorrectPattern() throws Exception {
        LocaleContextHolder.setLocale(Locale.ENGLISH);
        User user = new User();

        user.setName("Test");
        user.setEmail("email@email.com");
        user.setPhone("+34654987321");
        user.setBirthDate(LocalDate.of(2020, 11, 23));
        user.setProvince("Province");
        user.setPostCode("0001"); //Código postal español no válido
        user.setUsername("username");
        user.setPassword(new BCryptPasswordEncoder().encode("password123"));
        user.setEnabled(true);

        Validator validator = createValidator();
        Set<ConstraintViolation<User>> constrains = validator.validate(user);
        Assertions.assertThat(constrains.size()).isEqualTo(1);
        ConstraintViolation<User> violation = constrains.iterator().next();
        Assertions.assertThat(violation.getPropertyPath().toString()).isEqualTo("postCode");
        Assertions.assertThat(violation.getMessage()).isEqualTo("El código postal no es válido.");
    }
}
