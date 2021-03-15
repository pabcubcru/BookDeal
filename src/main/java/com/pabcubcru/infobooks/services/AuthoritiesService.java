package com.pabcubcru.infobooks.services;

import java.util.List;

import javax.transaction.Transactional;

import com.pabcubcru.infobooks.models.Authorities;
import com.pabcubcru.infobooks.repository.AuthoritiesRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthoritiesService {
    
    private AuthoritiesRepository authoritiesRepository;

    @Autowired
    public AuthoritiesService(AuthoritiesRepository authoritiesRepository) {
        this.authoritiesRepository = authoritiesRepository;
    }

    @Transactional
    public void save(Authorities authorities){
        this.authoritiesRepository.save(authorities);
    }

    @Transactional
    public List<Authorities> findByUserId(int userId){
        return this.authoritiesRepository.findByUserId(userId);
    }
}
