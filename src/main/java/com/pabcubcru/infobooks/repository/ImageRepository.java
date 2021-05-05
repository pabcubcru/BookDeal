package com.pabcubcru.infobooks.repository;

import java.util.List;

import com.pabcubcru.infobooks.models.Image;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends ElasticsearchRepository<Image, String> {

    public List<Image> findByIdBook(String idBook);
    
}
