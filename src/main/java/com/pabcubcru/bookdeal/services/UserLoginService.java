package com.pabcubcru.bookdeal.services;

import java.util.ArrayList;
import java.util.Collection;

import com.pabcubcru.bookdeal.models.Authorities;
import com.pabcubcru.bookdeal.models.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserLoginService implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthoritiesService authoritiesService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("Username " + username + " not found");
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                getGrantedAuthorities(user.getUsername()));
    }

    private Collection<GrantedAuthority> getGrantedAuthorities(String username) {
        Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        Authorities auth = this.authoritiesService.findByUsername(username).get(0);
        if(auth.getAuthority().equals("admin")) {
            grantedAuthorities.add(new SimpleGrantedAuthority("admin"));
        } 
        grantedAuthorities.add(new SimpleGrantedAuthority("user"));

        return grantedAuthorities;
    }

}
