package com.pabcubcru.infobooks.repository;

import java.util.List;

import com.pabcubcru.infobooks.models.Request;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestRepository extends ElasticsearchRepository<Request, String> {

    public List<Request> findByUsername1OrderByAction(String username);

    public Request findByUsername1AndIdBook2(String username, String idBook);

}
