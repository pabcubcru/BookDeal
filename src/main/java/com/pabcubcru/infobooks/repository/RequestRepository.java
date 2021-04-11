package com.pabcubcru.infobooks.repository;

import java.util.List;

import com.pabcubcru.infobooks.models.Request;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestRepository extends ElasticsearchRepository<Request, String> {

    public List<Request> findByUsername1OrderByStatus(String username);

    public List<Request> findByUsername2AndStatusNotAndStatusNotOrderByStatusDesc(String username, String statusRejected, String statusCanceled);

    public Request findByUsername1AndIdBook2(String username, String idBook);

    public List<Request> findByUsername2AndIdBook2AndStatusNot(String username, String idBook, String status);

    public Request findFirstByIdBook1AndStatus(String idBook1, String status);

    public Request findFirstByIdBook2AndStatus(String idBook2, String status);

    public List<Request> findByIdBook1AndStatusNotAndStatusNotAndAction(String idBook1,String statusReject, String statusCanceled, String action);
    
    public List<Request> findByIdBook2AndStatusNotAndStatusNotAndAction(String idBook2,String statusReject, String statusCanceled, String action);

    public List<Request> findByIdBook1OrIdBook2(String idBook1, String idBook2);

}
