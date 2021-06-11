package com.pabcubcru.bookdeal.repository;

import java.util.List;

import com.pabcubcru.bookdeal.models.Image;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends ElasticsearchRepository<Image, String> {

    public List<Image> findByIdBookOrderByPrincipalDesc(String idBook);

    public Image findByIdBookAndPrincipalTrue(String idBook);

}
