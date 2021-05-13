package com.pabcubcru.infobooks.models;

import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import lombok.Getter;
import lombok.Setter;

@Document(indexName = "user_favouritebook")
@Getter
@Setter
public class UserFavouriteBook extends BaseEntity {

    @Field(type = FieldType.Keyword, name = "username")
    private String username;

    @Field(type = FieldType.Keyword, name = "bookId")
    private String bookId;

}
