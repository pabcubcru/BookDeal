package com.pabcubcru.infobooks.repository;

import com.pabcubcru.infobooks.models.User;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends ElasticsearchRepository<User, Integer> {

    //@Query("SELECT count(u)>0 FROM User u WHERE u.email = ?1")
    public User findByEmail(String email);

    //@Query("SELECT count(u)>0 FROM User u WHERE u.username = ?1")
    public User findByUsername(String username);

    //@Query("SELECT u FROM User u WHERE u.id = ?1")
    public User findById(int id);
}
