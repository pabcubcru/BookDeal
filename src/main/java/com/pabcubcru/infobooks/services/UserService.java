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
    public void save(User user) {
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        user.setEnabled(true);
        this.userRepository.save(user);
        Authorities authorities = new Authorities();
        authorities.setUser(user);
        authorities.setAuthority("user");
        this.authoritiesService.save(authorities);
    }

    @Transactional
    public User findUserById(int id){
        return this.userRepository.findUserById(id);
    }

    @Transactional
    public void delete(User User){
        this.userRepository.delete(User);
    }

    @Transactional
    public Boolean existUserWithSameEmail(String email) {
        return this.userRepository.existUserWithSameEmail(email);
    }

    @Transactional
    public Boolean existUserWithSameUsername(String username) {
        return this.userRepository.existUserWithSameUsername(username);
    }

    @Transactional
    public User findByUsername(String username){
        return this.userRepository.findByUsername(username);
    }


}
