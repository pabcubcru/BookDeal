package com.pabcubcru.infobooks.models;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import lombok.Getter;
import lombok.Setter;

@Document(indexName = "books")
@Getter @Setter
public class Book extends BaseEntity {

    @NotBlank(message = "El título es un campo requerido.")
    @Field(type = FieldType.Text, name = "title")
    private String title;

    @Field(type = FieldType.Text, name = "originalTitle")
    private String originalTitle;

    @NotBlank(message = "El ISBN es un campo requerido.")
    @Pattern(regexp = "^(?:ISBN(?:-1[03])?:? )?(?=[0-9X]{10}$|(?=(?:[0-9]+[- ]){3})[- 0-9X]{13}$|97[89][0-9]{10}$|"+
    "(?=(?:[0-9]+[- ]){4})[- 0-9]{17}$)(?:97[89][- ]?)?[0-9]{1,5}[- ]?[0-9]+[- ]?[0-9]+[- ]?[0-9X]$", 
    message = "El ISBN no es válido.")
    @Field(type = FieldType.Text, name = "isbn")
    private String isbn;

    @NotNull(message = "El año de publicación es un campo requerido.")
    @Max(value = 2021L, message = "El año de publicación debe ser anterior o igual al presente año.")
    @Field(type = FieldType.Integer, name = "publicationYear")
    private Integer publicationYear;
    
    @NotBlank(message = "La editorial es un campo requerido.")
    @Field(type = FieldType.Text, name = "publisher")
    private String publisher;

    @NotBlank(message = "Los géneros es un campo requerido.")
    @Field(type = FieldType.Text, name = "genres")
    private String genres;

    @NotBlank(message = "El autor es un campo requerido.")
    @Field(type = FieldType.Keyword, name = "author")
    private String author;

    @NotBlank(message = "La descripción es un campo requerido.")
    @Field(type = FieldType.Text, name = "description")
    private String description;

    @NotBlank(message = "La imagen es un campo requerido.")
    @Field(type = FieldType.Text, name = "image")
    private String image;

    @NotBlank(message = "La acción es un campo requerido.")
    @Field(type = FieldType.Keyword, name = "action")
    private String action;

    @Field(type = FieldType.Double, name = "price")
    private Double price;

    @Field(type = FieldType.Object, name = "user")
    private User user;

    
    
}
