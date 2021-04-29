package com.pabcubcru.infobooks.repository;

import java.util.Optional;

import com.pabcubcru.infobooks.models.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends ElasticsearchRepository<User, String> {

    public User findByEmail(String email);

    public User findByUsername(String username);

    public Optional<User> findById(String id);

    public Page<User> findByPostCode(String postCode, Pageable pageable);

    public Page<User> findByCity(String city, Pageable pageable);

    public Page<User> findByProvince(String province, Pageable pageable);

    public Page<User> findByPostCodeOrCityOrProvince(String postCode, String city, String province, Pageable pageable);
}
