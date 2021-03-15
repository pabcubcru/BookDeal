package com.pabcubcru.infobooks.controllers;

import java.security.Principal;
import java.util.List;

import com.pabcubcru.infobooks.models.Authorities;
import com.pabcubcru.infobooks.models.User;
import com.pabcubcru.infobooks.services.AuthoritiesService;
import com.pabcubcru.infobooks.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class MainController {

	/*@Autowired
	private UserService userService;

	@Autowired
	private AuthoritiesService authoritiesService;*/
	
	@GetMapping(value = {"/", "/register", "login", "/login-error", "/logged"})
	public ModelAndView main(Principal principal) {
		ModelAndView model = new ModelAndView();
		model.setViewName("Main");
		/*if(principal != null) {
			model.addObject("isLogged", true);
			User user = this.userService.findByUsername(principal.getName());
			List<Authorities> authorities = authoritiesService.findByUserId(user.getId());
			Boolean isAdmin = false;
			for(Authorities a : authorities) {
				if(a.getAuthority().equals("admin")){
					isAdmin = true;
					break;
				}
			}
			model.addObject("isAdmin", isAdmin);
		} else {
			model.addObject("isLogged", false);
		}*/
		return model;
	}

}
