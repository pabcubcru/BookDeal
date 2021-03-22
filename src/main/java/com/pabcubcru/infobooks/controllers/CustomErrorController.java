package com.pabcubcru.infobooks.controllers;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;


import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class CustomErrorController implements ErrorController {

    @Override
    public String getErrorPath() {
        // TODO Auto-generated method stub
        return "/error";
    }

    @GetMapping("/error")
    public ModelAndView customError(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        ModelAndView model = new ModelAndView();
        
        if(status != null) {
            int code = Integer.parseInt(status.toString());
            if(code == HttpStatus.NOT_FOUND.value()) {
                model.setViewName("/errors/Error404");
            } else if (code == HttpStatus.FORBIDDEN.value()) {
                model.setViewName("/errors/Error403");
            } else if (code == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                model.setViewName("/errors/Error500");
            } else if (code == HttpStatus.SERVICE_UNAVAILABLE.value()) {
                model.setViewName("/errors/Error503");
            }

        }

        return model;
    }
    
}
