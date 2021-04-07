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
        return this.requestRepository.findByUsername1(username);
    }
    
}