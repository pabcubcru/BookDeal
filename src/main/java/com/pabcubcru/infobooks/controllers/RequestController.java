package com.pabcubcru.infobooks.controllers;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pabcubcru.infobooks.models.Book;
import com.pabcubcru.infobooks.models.Request;
import com.pabcubcru.infobooks.models.RequestStatus;
import com.pabcubcru.infobooks.services.BookService;
import com.pabcubcru.infobooks.services.RequestService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
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
            Request req = this.requestService.findByUsername1AndIdBook2(principal.getName(), id);
            if(req == null) {
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
            } else {
                res.put("success", false);
            }
        } catch (Exception e) {
            res.put("success", false);
        }

        return res;
    }

    @GetMapping("/my-requests")
    public Map<String, Object> listMyRequest(Principal principal) {
        Map<String, Object> res = new HashMap<>();
        List<String> idBooks1 = new ArrayList<>();
        List<String> idBooks2 = new ArrayList<>();

        List<Request> requests = this.requestService.listMyRequests(principal.getName());

        for(int i = 0; i<requests.size(); i++){
            Request r = requests.get(i);
            idBooks1.add(i, r.getIdBook1());
            idBooks2.add(i, r.getIdBook2());
        }

        res.put("requests", requests);
        res.put("books1", this.bookService.findByIds(idBooks1));
        res.put("books2", this.bookService.findByIds(idBooks2));

        return res;
    }

    @GetMapping(value = "/{id}/cancel")
    public void cancelRequest(@PathVariable("id") String id) {
        Request request = this.requestService.findById(id);

        if(request.getStatus().equals(RequestStatus.PENDIENTE.toString())) {
            request.setStatus(RequestStatus.CANCELADA.toString());
            this.requestService.save(request);
        }
    }

    @DeleteMapping(value = "/{id}/delete")
    public void deleteRequest(@PathVariable("id") String id) {
        Request request = this.requestService.findById(id);

        if(request.getStatus().equals(RequestStatus.CANCELADA.toString())) {
            this.requestService.deleteById(id);
        }
    }
}
