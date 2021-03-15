package com.pabcubcru.infobooks.repository;

import com.pabcubcru.infobooks.models.User;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {

    @Query("SELECT count(u)>0 FROM User u WHERE u.email = ?1")
    public Boolean existUserWithSameEmail(String email);

    @Query("SELECT count(u)>0 FROM User u WHERE u.username = ?1")
    public Boolean existUserWithSameUsername(String username);

    @Query("SELECT u FROM User u WHERE u.username = ?1")
    public User findByUsername(String username);
}
