package com.pabcubcru.infobooks.repository;

import com.pabcubcru.infobooks.models.Search;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SearchRepository extends ElasticsearchRepository<Search, String> {

    public Search findFirstByUsername(String username);

}
