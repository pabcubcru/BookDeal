package com.pabcubcru.infobooks.models;

import java.time.LocalDate;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="users")
@Getter @Setter
public class User extends BaseEntity{

	@NotBlank(message = "El nombre es un campo requerido.")
	@Column(name = "name")
	private String name;
	
	@Column(name = "email", unique = true)
	@NotBlank(message = "El email es un campo requerido.")
	@Email(message = "Debe ser un email válido.")
	private String email;
	
	@NotBlank(message = "El teléfono es un campo requerido.")
	@Column(name = "phone")
	private String phone;

	@NotNull(message = "La fecha de nacimiento es un campo requerido.")
	@Column(name = "birthdate")
	private LocalDate birthDate;

	@Column(name = "username", unique = true)
	@NotBlank(message = "El nombre de usuario es un campo requerido.")
    private String username;

    @NotBlank(message = "La contraseña es un campo requerido.")
    private String password;

    boolean enabled; 

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
	private Set<Authorities> authorities;

}
