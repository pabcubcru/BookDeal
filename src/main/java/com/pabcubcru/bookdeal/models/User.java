package com.pabcubcru.bookdeal.models;

import java.time.LocalDate;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;

import org.springframework.data.annotation.Transient;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document(indexName = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User extends BaseEntity {

	@NotBlank(message = "El nombre es un campo requerido.")
	@Field(type = FieldType.Text, name = "name")
	private String name;

	@Field(type = FieldType.Text, name = "email")
	@NotBlank(message = "El email es un campo requerido.")
	@Email(message = "Debe ser un email válido.")
	private String email;

	@Field(type = FieldType.Text, name = "phone")
	@Pattern(regexp = "^(\\+34|0034|34)?[6789]\\d{8}$", message = "El número de teléfono no es válido.")
	private String phone;

	@NotNull(message = "La fecha de nacimiento es un campo requerido.")
	@Field(type = FieldType.Date, name = "bithDate", format = DateFormat.basic_date)
	@Past(message = "La fecha de nacimiento debe ser anterior a la fecha actual.")
	private LocalDate birthDate;

	@NotBlank(message = "La provincia es un campo requerido.")
	@Field(type = FieldType.Keyword, name = "province")
	private String province;

	@Field(type = FieldType.Text, name = "postCode")
	@Pattern(regexp = "0[1-9][0-9]{3}|[1-4][0-9]{4}|5[0-2][0-9]{3}", message = "El código postal no es válido.")
	private String postCode;

	@Field(type = FieldType.Text, name = "genres")
	@NotBlank(message = "Los géneros es un campo requerido.")
	private String genres;

	@Field(type = FieldType.Text, name = "username")
	@NotBlank(message = "El nombre de usuario es un campo requerido.")
	private String username;

	@Field(type = FieldType.Binary, name = "password")
	private String password;

	@Field(type = FieldType.Boolean, name = "enabled")
	private boolean enabled;

	@Transient
	private Boolean accept;

	@Transient
	private String confirmPassword;

}
