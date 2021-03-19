package com.pabcubcru.infobooks.models;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="users")
@Getter @Setter
public class User extends BaseEntity {

	@NotBlank(message = "El nombre es un campo requerido.")
	@Column(name = "name")
	private String name;
	
	@Column(name = "email", unique = true)
	@NotBlank(message = "El email es un campo requerido.")
	@Email(message = "Debe ser un email válido.")
	private String email;
	
	@Column(name = "phone")
	@Pattern(regexp = "^(\\+34|0034|34)?[6789]\\d{8}$", message = "El número de teléfono no es válido.")
	private String phone;

	@NotNull(message = "La fecha de nacimiento es un campo requerido.")
	@Column(name = "birthdate")
	@Past(message = "La fecha de nacimiento debe ser anterior a la fecha actual.")
	private LocalDate birthDate;

	@NotNull(message = "La provincia es un campo requerido.")
	@Column(name = "province")
	private ProvinceEnum province;

	@NotBlank(message = "La ciudad es un campo requerido.")
	@Column(name = "city")
	private String city;

	@Column(name = "postcode")
	@Pattern(regexp = "0[1-9][0-9]{3}|[1-4][0-9]{4}|5[0-2][0-9]{3}", message = "El código postal no es válido.")
	private String postCode;

	@Column(name = "username", unique = true)
	@NotBlank(message = "El nombre de usuario es un campo requerido.")
    private String username;

    private String password;

    boolean enabled; 

    /*@OneToMany(cascade = CascadeType.ALL, mappedBy = "user", fetch = FetchType.EAGER)
	private Set<Authorities> authorities;*/

}
