package com.pabcubcru.infobooks.controllers;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

	@Autowired
	private UserService userService;

	@Autowired
	private AuthoritiesService authoritiesService;
	
	@GetMapping(value = {"/", "/register", "login", "/login-error", "/logged"})
	public ModelAndView main(Principal principal) {
		ModelAndView model = new ModelAndView();
		model.setViewName("Main");
		return model;
	}

	@GetMapping("/principal")
	public Map<String, Object> getPrincipal(Principal principal){
		Map<String, Object> res = new HashMap<>();
		if(principal != null) {
			res.put("isLogged", true);
			User user = this.userService.findByUsername(principal.getName());
			List<Authorities> authorities = authoritiesService.findByUserId(user.getId());
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

}
