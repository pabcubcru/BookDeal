package com.pabcubcru.infobooks.models;

import javax.validation.constraints.Size;

import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import lombok.Getter;
import lombok.Setter;

@Document(indexName = "authorities")
@Getter @Setter
public class Authorities extends BaseEntity {

	@Field(type = FieldType.Object, name = "user")
	User user;
	
	@Size(min = 3, max = 50)
	@Field(type = FieldType.Text, name = "authority")
	String authority;
}
