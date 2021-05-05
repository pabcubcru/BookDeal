package com.pabcubcru.infobooks.models;

import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import lombok.Getter;
import lombok.Setter;

@Document(indexName = "images")
@Getter @Setter
public class Image extends BaseEntity {

    @Field(type = FieldType.Text, name = "fileName")
    private String fileName;

    @Field(type = FieldType.Keyword, name = "idBook")
    private String idBook;

    @Field(type = FieldType.Text, name = "urlImage")
    private String urlImage;

    @Field(type = FieldType.Text, name = "urlDelete")
    private String urlDelete;
    
}
