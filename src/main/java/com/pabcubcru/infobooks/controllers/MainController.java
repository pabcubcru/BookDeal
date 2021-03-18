package com.pabcubcru.infobooks.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class MainController {
	
	@GetMapping(value = {"/*"})
	public ModelAndView main() {
		ModelAndView model = new ModelAndView();
		model.setViewName("Main");
		return model;
	}

}
