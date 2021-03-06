package com.pabcubcru.bookdeal.services;

import java.util.List;

import com.pabcubcru.bookdeal.models.Authorities;
import com.pabcubcru.bookdeal.repository.AuthoritiesRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthoritiesService {

    private AuthoritiesRepository authoritiesRepository;

    @Autowired
    public AuthoritiesService(AuthoritiesRepository authoritiesRepository) {
        this.authoritiesRepository = authoritiesRepository;
    }

    @Transactional
    public void save(Authorities authorities) {
        this.authoritiesRepository.save(authorities);
    }

    @Transactional
    public List<Authorities> findByUsername(String username) {
        return this.authoritiesRepository.findByUsername(username);
    }
}
