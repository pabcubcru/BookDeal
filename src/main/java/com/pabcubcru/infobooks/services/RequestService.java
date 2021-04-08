package com.pabcubcru.infobooks.services;

import java.util.List;

import com.pabcubcru.infobooks.models.Request;
import com.pabcubcru.infobooks.repository.RequestRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RequestService {

    private RequestRepository requestRepository;

    public RequestService(RequestRepository requestRepository) {
        this.requestRepository = requestRepository;
    }

    @Transactional
    public void save(Request request) {
        this.requestRepository.save(request);
    }

    @Transactional(readOnly = true)
    public List<Request> listMyRequests(String username) {
        return this.requestRepository.findByUsername1OrderByAction(username);
    }

    @Transactional
    public Request findByUsername1AndIdBook2(String username, String idBook) {
        return this.requestRepository.findByUsername1AndIdBook2(username, idBook);
    }

    @Transactional
    public Request findById(String id) {
        return this.requestRepository.findById(id).orElse(null);
    }

    @Transactional
    public void deleteById(String id) {
        this.requestRepository.deleteById(id);
    }
    
}
