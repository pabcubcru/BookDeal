package com.pabcubcru.infobooks.controllers;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import com.pabcubcru.infobooks.models.Authorities;
import com.pabcubcru.infobooks.models.ProvinceEnum;
import com.pabcubcru.infobooks.models.User;
import com.pabcubcru.infobooks.services.AuthoritiesService;
import com.pabcubcru.infobooks.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private AuthoritiesService authoritiesService;

	@GetMapping("/user/principal")
	public Map<String, Object> getPrincipal(Principal principal){
		Map<String, Object> res = new HashMap<>();
		if(principal != null) {
			res.put("isLogged", true);
			User user = this.userService.findByUsername(principal.getName());
			List<Authorities> authorities = this.authoritiesService.findByUser(user);
			Boolean isAdmin = false;
			for(Authorities a : authorities) {
				if(a.getAuthority().equals("admin")){
					isAdmin = true;
					break;
				}
			}
			res.put("isAdmin", isAdmin);
		} else {
			res.put("isLogged", false);
			res.put("isAdmin", false);
		}
		return res;
	}

	@GetMapping(value = "/user/provinces")
	public Map<String, Object> getProvinces() {
		Map<String, Object> res = new HashMap<>();

		res.put("provinces", List.of(ProvinceEnum.values()));

		return res;
	}

	@PostMapping(value="/register")
	public Map<String, Object> register(@RequestBody @Valid User user){
		Map<String, Object> res = new HashMap<>();

		if(!user.getEmail().isEmpty() && !user.getUsername().isEmpty() && !user.getPassword().isEmpty()){
			Boolean existEmail = this.userService.existUserWithSameEmail(user.getEmail());
			Boolean existUsername = this.userService.existUserWithSameUsername(user.getUsername());
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
				this.userService.save(user, true);
				res.put("success", true);
			}
		}
		return res;
	}

	@GetMapping("/user/get-username")
	public Map<String, Object> getIdPrincipal(Principal principal) {
		Map<String, Object> res = new HashMap<>();
		
		res.put("username", principal.getName());

		return res;
	}

	@GetMapping("/user/{username}")
	public Map<String, Object> get(@PathVariable("username") String username) {
		Map<String, Object> res = new HashMap<>();
		if(username != null) {
			User user = this.userService.findByUsername(username);
			res.put("user", user);
			res.put("success", true);
		} else {
			res.put("success", false);
		}

		return res;
	}

	@PutMapping("/user/{username}/edit")
	public Map<String, Object> edit(@RequestBody @Valid User user, @PathVariable("username") String username) {
		Map<String, Object> res = new HashMap<>();

		if(!user.getEmail().isEmpty()){
			Boolean existEmail = this.userService.existUserWithSameEmail(user.getEmail());
			User userOld = this.userService.findByUsername(username);
			if(existEmail && !userOld.getEmail().equals(user.getEmail())) {
				res.put("message", "Este correo electrónico ya está registrado.");
				res.put("success", false);
			} else {
				user.setId(userOld.getId());
				Boolean newPassword = true;
				if (user.getPassword().isEmpty()) {
					user.setPassword(userOld.getPassword());
					newPassword = false;
				}
				user.setProvince("Sevilla");
				this.userService.save(user, newPassword);
				res.put("success", true);
			}
		}
		return res;

	}
}
