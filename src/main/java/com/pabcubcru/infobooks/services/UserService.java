package com.pabcubcru.infobooks.services;

import java.util.ArrayList;
import java.util.List;

import com.pabcubcru.infobooks.models.Authorities;
import com.pabcubcru.infobooks.models.User;
import com.pabcubcru.infobooks.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    
    private UserRepository userRepository;

    @Autowired
    private AuthoritiesService authoritiesService;

    @Autowired
	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
    
    @Transactional(readOnly = true)
    public List<User> listAll(){
        List<User> res = new ArrayList<User>();
        this.userRepository.findAll().iterator().forEachRemaining(res::add);
        return res;
    }

    @Transactional
    public void save(User user, Boolean isNewPassword) {
        if(isNewPassword){
            user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        }
        user.setEnabled(true);
        if(user.getId() == null){
            Authorities authority = new Authorities();
            authority.setUsername(user.getUsername());
            authority.setAuthority("user");
            this.authoritiesService.save(authority);
        }
        this.userRepository.save(user);
    }

    @Transactional
    public User findUserById(String id){
        return this.userRepository.findById(id).orElse(null);
    }

    @Transactional
    public void delete(User User){
        this.userRepository.delete(User);
    }

    @Transactional
    public Boolean existUserWithSameEmail(String email) {
        return this.userRepository.findByEmail(email) != null;
    }

    @Transactional
    public Boolean existUserWithSameUsername(String username) {
        return this.userRepository.findByUsername(username) != null;
    }

    @Transactional
    public User findByUsername(String username){
        return this.userRepository.findByUsername(username);
    }


}
