package com.pabcubcru.infobooks.repository;

import java.util.List;

import com.pabcubcru.infobooks.models.Authorities;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthoritiesRepository extends ElasticsearchRepository<Authorities, String> {

    List<Authorities> findByUsername(String username);

}
