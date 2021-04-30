package com.pabcubcru.infobooks.controllers;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.validation.Valid;

import com.pabcubcru.infobooks.models.Book;
import com.pabcubcru.infobooks.models.Request;
import com.pabcubcru.infobooks.models.RequestStatus;
import com.pabcubcru.infobooks.models.User;
import com.pabcubcru.infobooks.services.BookService;
import com.pabcubcru.infobooks.services.RequestService;
import com.pabcubcru.infobooks.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping(value = "/requests")
public class RequestController {
    
    @Autowired
    private RequestService requestService;

    @Autowired
    private BookService bookService;

    @Autowired
    private UserService userService;

    @GetMapping(value = {"/me/{page}", "/received/{page}"})
    public ModelAndView mainWithSecurity() {
        ModelAndView model = new ModelAndView();
        model.setViewName("Main");
        return model;
    }

    @GetMapping(value = {"/{id}/add"})
    public ModelAndView mainWithSecurity(@PathVariable("id") String id, Principal principal) {
        ModelAndView model = new ModelAndView();
        model.setViewName("Main");

        if(id != null) {
            Book book = this.bookService.findBookById(id);
            Request req = this.requestService.findByUsername1AndIdBook2(principal.getName(), id);
            if(req != null) {
                model.setViewName("errors/Error403");
            }
            if(book == null) {
                model.setViewName("errors/Error404");
            }
        } else {
            model.setViewName("errors/Error404");
        }
        return model;
    }

    @PostMapping(value = "/{id}/new")
    public Map<String, Object> addRequest(@RequestBody @Valid Request request, BindingResult result, @PathVariable("id") String id, Principal principal) {
        Map<String, Object> res = new HashMap<>();

        if(request.getAction().equals("COMPRA") && request.getPay() == null) {
            result.rejectValue("pay", "El precio es un campo requerido.", "El precio es un campo requerido.");
        }

        if(!result.hasErrors()) {
            try {
                Request req = this.requestService.findByUsername1AndIdBook2(principal.getName(), id);
                if(req == null) {
                    Book book = this.bookService.findBookById(id);
                    if(request.getAction().equals("INTERCAMBIO")) {
                        request.setPay(null);
                    } else if(request.getAction().equals("COMPRA")) {
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
        } else {
            res.put("errors", result.getAllErrors());
            res.put("success", false);
        }

        return res;
    }

    @GetMapping("/my-requests")
    public Map<String, Object> listMyRequest(Principal principal, @RequestParam(name = "page", defaultValue = "0") Integer page) {
        Map<String, Object> res = new HashMap<>();
        List<Book> books1 = new ArrayList<>();
        List<Book> books2 = new ArrayList<>();
        List<User> users = new ArrayList<>();
        PageRequest pageRequest = PageRequest.of(page, 10);

        Page<Request> pageOfRequests = this.requestService.listMyRequests(principal.getName(), pageRequest);

        for(Request r : pageOfRequests.getContent()){
            if(r.getAction().equals("INTERCAMBIO")) {
                books1.add(this.bookService.findBookById(r.getIdBook1()));
            } else {
                books1.add(null);
            }
            if(r.getStatus().equals(RequestStatus.ACEPTADA.toString())) {
                users.add(this.userService.findByUsername(r.getUsername2()));
            } else{
                users.add(null);
            }
            books2.add(this.bookService.findBookById(r.getIdBook2()));
        }

        res.put("requests", pageOfRequests.getContent());
        res.put("books1", books1);
        res.put("books2", books2);
        res.put("users", users);

        Integer numberOfPages = pageOfRequests.getTotalPages();
        res.put("numTotalPages", numberOfPages);
        res.put("pages", new ArrayList<Integer>());
        if(numberOfPages > 0) {
            List<Integer> pages = IntStream.rangeClosed(page-10 <= 0 ? 0 : page-10, page+10 >= numberOfPages-1 ? numberOfPages-1 : page+10).boxed().collect(Collectors.toList());
            res.put("pages", pages);
        }

        return res;
    }

    @GetMapping("/received-requests")
    public Map<String, Object> listReceivedRequest(Principal principal, @RequestParam(name = "page", defaultValue = "0") Integer page) {
        Map<String, Object> res = new HashMap<>();
        List<Book> books1 = new ArrayList<>();
        List<Book> books2 = new ArrayList<>();
        List<User> users = new ArrayList<>();
        PageRequest pageRequest = PageRequest.of(page, 10);

        Page<Request> pageOfRequests = this.requestService.listReceivedRequests(principal.getName(), pageRequest);

        for(Request r : pageOfRequests.getContent()){
            if(r.getAction().equals("INTERCAMBIO")) {
                books1.add(this.bookService.findBookById(r.getIdBook1()));
            } else {
                books1.add(null);
            }
            books2.add(this.bookService.findBookById(r.getIdBook2()));
            if(r.getStatus().equals(RequestStatus.ACEPTADA.toString())) {
                users.add(this.userService.findByUsername(r.getUsername1()));
            } else{
                users.add(null);
            }
        }

        res.put("requests", pageOfRequests.getContent());
        res.put("books1", books1);
        res.put("books2", books2);
        res.put("users", users);

        Integer numberOfPages = pageOfRequests.getTotalPages();
        res.put("numTotalPages", numberOfPages);
        res.put("pages", new ArrayList<Integer>());
        if(numberOfPages > 0) {
            List<Integer> pages = IntStream.rangeClosed(page-10 <= 0 ? 0 : page-10, page+10 >= numberOfPages-1 ? numberOfPages-1 : page+10).boxed().collect(Collectors.toList());
            res.put("pages", pages);
        }

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

        if(request.getStatus().equals(RequestStatus.CANCELADA.toString()) || request.getStatus().equals(RequestStatus.RECHAZADA.toString())) {
            this.requestService.deleteById(id);
        }
    }

    @GetMapping("/{id}/accept")
    public ModelAndView acceptRequest(@PathVariable("id") String id, Principal principal) {
        Request request = this.requestService.findById(id);

        if(request.getUsername2().equals(principal.getName())) {
            if(request.getAction().equals("VENTA")) {
                List<Request> requests = this.requestService.findByIdBook2AndStatusNotAndStatusNotAndAction(request.getIdBook2(), "VENTA");
                requests.remove(request);
                for(Request r : requests) {
                    if(r.getStatus().equals(RequestStatus.PENDIENTE.toString())) {
                        r.setStatus(RequestStatus.RECHAZADA.toString());
                    }
                }
                this.requestService.saveAll(requests);
                request.setStatus(RequestStatus.ACEPTADA.toString());
                this.requestService.save(request);
            } else {
                List<Request> requests = this.requestService.findByIdBook1AndStatusNotAndStatusNotAndAction(request.getIdBook1(), "INTERCAMBIO");
                requests.addAll(this.requestService.findByIdBook2AndStatusNotAndStatusNotAndAction(request.getIdBook2(), "INTERCAMBIO"));
                requests.remove(request);
                for(Request r : requests) {
                    if(r.getStatus().equals(RequestStatus.PENDIENTE.toString())) {
                        r.setStatus(RequestStatus.RECHAZADA.toString());
                    }
                }
                this.requestService.saveAll(requests);
                request.setStatus(RequestStatus.ACEPTADA.toString());
                this.requestService.save(request);
            }
            return null;
        } else {
            ModelAndView model = new ModelAndView();
            model.setViewName("errors/Error403");
            return model;
        }
    }

    @GetMapping("/{id}/reject")
    public ModelAndView rejectRequest(@PathVariable("id") String id, Principal principal) {
        Request request = this.requestService.findById(id);

        if(request.getUsername2().equals(principal.getName())) {
            if(request.getStatus().equals(RequestStatus.PENDIENTE.toString())) {

                request.setStatus(RequestStatus.RECHAZADA.toString());
                this.requestService.save(request);
            }
            return null;
        } else {
            ModelAndView model = new ModelAndView();
            model.setViewName("errors/Error403");
            return model;
        }
    }
}
