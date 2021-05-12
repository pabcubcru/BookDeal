package com.pabcubcru.infobooks.models;

import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import lombok.Getter;
import lombok.Setter;

@Document(indexName = "searches")
@Getter
@Setter
public class Search extends BaseEntity {

    @Field(type = FieldType.Text, name = "text")
    private String text;

    @Field(type = FieldType.Integer, name = "number1")
    private Integer number1;

    @Field(type = FieldType.Integer, name = "number2")
    private Integer number2;

    @Field(type = FieldType.Keyword, name = "type")
    private String type;

    @Field(type = FieldType.Keyword, name = "username")
    private String username;

}
