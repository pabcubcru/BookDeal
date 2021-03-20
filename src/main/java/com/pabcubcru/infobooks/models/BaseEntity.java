package com.pabcubcru.infobooks.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BaseEntity {
    
    @Id
	private String id;
}
