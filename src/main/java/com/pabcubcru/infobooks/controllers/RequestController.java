package com.pabcubcru.infobooks.controllers;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import com.pabcubcru.infobooks.models.Book;
import com.pabcubcru.infobooks.models.Request;
import com.pabcubcru.infobooks.models.RequestStatus;
import com.pabcubcru.infobooks.services.BookService;
import com.pabcubcru.infobooks.services.RequestService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping(value = "/requests")
public class RequestController {
    
    @Autowired
    private RequestService requestService;

    @Autowired
    private BookService bookService;

    @GetMapping(value = {"/me"})
    public ModelAndView mainWithSecurity() {
        ModelAndView model = new ModelAndView();
        model.setViewName("Main");
        return model;
    }

    @GetMapping(value = {"/{id}/add"})
    public ModelAndView mainWithSecurity(@PathVariable("id") String id) {
        ModelAndView model = new ModelAndView();
        model.setViewName("Main");
        if(id != null) {
            Book book = this.bookService.findBookById(id);
            if(book == null) {
                model.setViewName("errors/Error404");
            }
        }
        return model;
    }

    @PostMapping(value = "/{id}/new")
    public Map<String, Object> addRequest(@RequestBody Request request, @PathVariable("id") String id, Principal principal) {
        Map<String, Object> res = new HashMap<>();

        try {
            Book book = this.bookService.findBookById(id);
            if(book.getAction().equals("INTERCAMBIO")) {
                request.setAction("INTERCAMBIO");
            } else if(book.getAction().equals("VENTA")) {
                request.setAction("VENTA");
                request.setIdBook1("");
            }
            request.setIdBook2(id);
            request.setStatus(RequestStatus.PENDIENTE.toString());
            request.setUsername1(principal.getName());
            request.setUsername2(book.getUsername());
            this.requestService.save(request);
            res.put("success", true);
        } catch (Exception e) {
            res.put("success", false);
        }

        return res;
    }

    @GetMapping("/my-requests")
    public Map<String, Object> listMyRequest(Principal principal) {
        Map<String, Object> res = new HashMap<>();

        res.put("requests", this.requestService.listMyRequests(principal.getName()));

        return res;
    }
}
