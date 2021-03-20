package com.pabcubcru.infobooks.repository;

import java.util.List;

import com.pabcubcru.infobooks.models.Authorities;
import com.pabcubcru.infobooks.models.User;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthoritiesRepository extends ElasticsearchRepository<Authorities, String> {

    //@Query("SELECT a FROM Authorities a WHERE a.user.id = ?1")
    List<Authorities> findByUser(User user);
    
}
