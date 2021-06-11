package com.pabcubcru.bookdeal.repository;

import com.pabcubcru.bookdeal.models.Search;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SearchRepository extends ElasticsearchRepository<Search, String> {

    public Search findFirstByUsername(String username);

}
