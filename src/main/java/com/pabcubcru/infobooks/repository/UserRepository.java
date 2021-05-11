package com.pabcubcru.infobooks.repository;

import java.util.List;
import java.util.Optional;

import com.pabcubcru.infobooks.models.User;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends ElasticsearchRepository<User, String> {

    public User findByEmail(String email);

    public User findByUsername(String username);

    public Optional<User> findById(String id);

    public List<User> findByPostCode(String postCode);

    public List<User> findByProvinceLike(String provinceString);

    public List<User> findByProvince(String provinceString);

    public List<User> findByPostCodeOrProvince(String postCode, String province);
}
