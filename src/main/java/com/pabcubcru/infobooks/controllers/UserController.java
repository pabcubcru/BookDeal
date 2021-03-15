package com.pabcubcru.infobooks.controllers;
import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import com.pabcubcru.infobooks.models.User;
import com.pabcubcru.infobooks.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

	@Autowired
	UserService employeeService;

	@PostMapping(value="/register")
	public Map<String, Object> register(@RequestBody @Valid User user){
		Map<String, Object> res = new HashMap<>();

		if(!user.getEmail().isEmpty() && !user.getUsername().isEmpty() && !user.getPassword().isEmpty()){
			Boolean existEmail = this.employeeService.existUserWithSameEmail(user.getEmail());
			Boolean existUsername = this.employeeService.existUserWithSameUsername(user.getUsername());
			if(existEmail){
				res.put("message", "Este correo electrónico ya está registrado.");
				res.put("success", false);
			} else if(existUsername) {
				res.put("message", "El nombre de usuario no está disponible.");
				res.put("success", false);
			}else if(user.getPassword().length() < 8 || user.getPassword().length()> 20){
				res.put("message", "La contraseña debe contener entre 8 y 20 carácteres.");
				res.put("success", false);
			} else {
				this.employeeService.save(user);
				res.put("success", true);
			}
		}
		return res;
	}
}
