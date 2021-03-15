package com.pabcubcru.infobooks.models;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "authorities")
@Getter @Setter
public class Authorities extends BaseEntity{

    @ManyToOne
	@JoinColumn(name = "username")
	User user;
	
	@Size(min = 3, max = 50)
	String authority;
}
