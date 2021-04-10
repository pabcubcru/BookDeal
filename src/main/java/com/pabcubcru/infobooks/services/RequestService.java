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

    @Transactional(readOnly = true)
    public List<Request> listReceivedRequests(String username) {
        return this.requestRepository.findByUsername2OrderByAction(username);
    }

    @Transactional
    public Request findByUsername1AndIdBook2(String username, String idBook) {
        return this.requestRepository.findByUsername1AndIdBook2(username, idBook);
    }

    @Transactional
    public List<Request> findByUsername2AndIdBook2(String username, String idBook, String status) {
        return this.requestRepository.findByUsername2AndIdBook2AndStatusNot(username, idBook, status);
    }

    @Transactional
    public Request findFirstByIdBook1AndStatus(String idBook1, String status) {
        return this.requestRepository.findFirstByIdBook1AndStatus(idBook1, status);
    }

    @Transactional
    public Request findFirstByIdBook2AndStatus(String idBook1, String status) {
        return this.requestRepository.findFirstByIdBook2AndStatus(idBook1, status);
    }

    @Transactional
    public Request findById(String id) {
        return this.requestRepository.findById(id).orElse(null);
    }

    @Transactional
    public void deleteById(String id) {
        this.requestRepository.deleteById(id);
    }

    @Transactional
    public void saveAll(List<Request> requests) {
        this.requestRepository.saveAll(requests);
    }
    
}
