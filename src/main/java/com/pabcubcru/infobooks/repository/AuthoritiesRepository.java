package com.pabcubcru.infobooks.repository;

import java.util.List;

import com.pabcubcru.infobooks.models.Authorities;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthoritiesRepository extends CrudRepository<Authorities, Integer> {

    @Query("SELECT a FROM Authorities a WHERE a.user.id = ?1")
    List<Authorities> findByUserId(int userId);
    
}
