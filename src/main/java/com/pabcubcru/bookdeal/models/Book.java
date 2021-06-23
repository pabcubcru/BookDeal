package com.pabcubcru.bookdeal.models;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.PositiveOrZero;

import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.Transient;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document(indexName = "books")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Book extends BaseEntity {

    @NotBlank(message = "El título es un campo requerido.")
    @Field(type = FieldType.Text, name = "title")
    @Length(max = 120, message = "El título no debe superar los 120 carácteres.")
    private String title;

    @Field(type = FieldType.Text, name = "originalTitle")
    @Length(max = 120, message = "El título original no debe superar los 120 carácteres.")
    private String originalTitle;

    @NotBlank(message = "El ISBN es un campo requerido.")
    @Pattern(regexp = "(?=[0-9X]{10}$|(?=(?:[0-9]+[- ]){3})[- 0-9X]{13}$|97[89][0-9]{10}$|"
            + "(?=(?:[0-9]+[- ]){4})[- 0-9]{17}$)(?:97[89][- ]?)?[0-9]{1,5}[- ]?[0-9]+[- ]?[0-9]+[- ]?[0-9X]$", message = "El ISBN no es válido. Debe tener el siguiente formato: 0-123-45678-9 ó 012-3-456-78901-2.")
    @Field(type = FieldType.Text, name = "isbn")
    private String isbn;

    @NotNull(message = "El año de publicación es un campo requerido.")
    @PositiveOrZero(message = "El año de publicación debe ser un número positivo.")
    @Field(type = FieldType.Integer, name = "publicationYear")
    private Integer publicationYear;

    @NotBlank(message = "La editorial es un campo requerido.")
    @Field(type = FieldType.Text, name = "publisher")
    @Length(max = 120, message = "La editorial no debe superar los 120 carácteres.")
    private String publisher;

    @NotBlank(message = "Los géneros es un campo requerido.")
    @Field(type = FieldType.Text, name = "genres")
    private String genres;

    @NotBlank(message = "El autor es un campo requerido.")
    @Length(max = 120, message = "El título original no debe superar los 120 carácteres.")
    @Field(type = FieldType.Keyword, name = "author")
    private String author;

    @NotBlank(message = "La descripción es un campo requerido.")
    @Field(type = FieldType.Text, name = "description")
    private String description;

    @NotBlank(message = "El estado es un campo requerido.")
    @Field(type = FieldType.Keyword, name = "status")
    private String status;

    @NotNull(message = "El precio es un campo requerido.")
    @PositiveOrZero(message = "El precio debe ser un número positivo.")
    @Field(type = FieldType.Double, name = "price")
    private Double price;

    @Field(type = FieldType.Keyword, name = "username")
    private String username;

    @Transient
    private String image;
}
