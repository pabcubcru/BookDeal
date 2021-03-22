package com.pabcubcru.infobooks.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public abstract class BaseEntity {
    
    @Id
    @Field(type = FieldType.Text, name = "id")
	private String id;
}
