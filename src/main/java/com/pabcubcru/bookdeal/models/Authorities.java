package com.pabcubcru.bookdeal.models;

import javax.validation.constraints.Size;

import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document(indexName = "authorities")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Authorities extends BaseEntity {

	@Field(type = FieldType.Text, name = "username")
	private String username;

	@Size(min = 3, max = 50)
	@Field(type = FieldType.Text, name = "authority")
	private String authority;
}
