package com.pabcubcru.infobooks.services;

import java.util.List;

import com.pabcubcru.infobooks.models.Request;
import com.pabcubcru.infobooks.models.RequestStatus;
import com.pabcubcru.infobooks.repository.RequestRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public Page<Request> listMyRequests(String username, Pageable pageable) {
        return this.requestRepository.findByUsername1OrderByStatus(username, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Request> listReceivedRequests(String username, Pageable pageable) {
        return this.requestRepository.findByUsername2AndStatusNotAndStatusNotOrderByStatusDesc(username,
                RequestStatus.RECHAZADA.toString(), RequestStatus.CANCELADA.toString(), pageable);
    }

    @Transactional
    public Request findByUsername1AndIdBook2(String username, String idBook) {
        return this.requestRepository.findByUsername1AndIdBook2(username, idBook);
    }

    @Transactional
    public List<Request> findByUsername2AndIdBook2AndStatusNot(String username, String idBook, String status) {
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
    public List<Request> findByIdBook1AndStatusNotAndStatusNotAndAction(String idBook1, String action) {
        return this.requestRepository.findByIdBook1AndStatusNotAndStatusNotAndAction(idBook1,
                RequestStatus.RECHAZADA.toString(), RequestStatus.CANCELADA.toString(), action);
    }

    @Transactional
    public List<Request> findByIdBook2AndStatusNotAndStatusNotAndAction(String idBook2, String action) {
        return this.requestRepository.findByIdBook2AndStatusNotAndStatusNotAndAction(idBook2,
                RequestStatus.RECHAZADA.toString(), RequestStatus.CANCELADA.toString(), action);
    }

    @Transactional
    public List<Request> findByIdBook1OrIdBook2(String idBook1, String idBook2) {
        return this.requestRepository.findByIdBook1OrIdBook2(idBook1, idBook2);
    }

    @Transactional
    public Request findFirstByIdBook1OrIdBook2(String idBook) {
        return this.requestRepository.findFirstByIdBook1OrIdBook2(idBook, idBook);
    }

    @Transactional
    public void deleteById(String id) {
        this.requestRepository.deleteById(id);
    }

    @Transactional
    public void deleteAll(List<Request> requests) {
        this.requestRepository.deleteAll(requests);
    }

    @Transactional
    public void saveAll(List<Request> requests) {
        this.requestRepository.saveAll(requests);
    }

}
