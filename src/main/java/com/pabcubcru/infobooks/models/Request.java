package com.pabcubcru.infobooks.models;

import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import lombok.Getter;
import lombok.Setter;

@Document(indexName = "requests")
@Getter @Setter
public class Request extends BaseEntity {
    
    @Field(type = FieldType.Keyword, name = "username1")
    private String username1;

    @Field(type = FieldType.Keyword, name = "username2")
    private String username2;

    @Field(type = FieldType.Keyword, name = "idBook1")
    private String idBook1;

    @Field(type = FieldType.Keyword, name = "idBook2")
    private String idBook2;

    @Field(type = FieldType.Keyword, name = "status")
    private String status;

    @Field(type = FieldType.Keyword, name = "action")
    private String action;

    @Field(type = FieldType.Keyword, name = "pay")
    private Double pay;

    @Field(type = FieldType.Text, name = "comment")
    private String comment;
}
