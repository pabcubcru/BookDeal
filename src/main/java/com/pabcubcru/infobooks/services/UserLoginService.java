package com.pabcubcru.infobooks.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.pabcubcru.infobooks.models.Authorities;
import com.pabcubcru.infobooks.models.User;

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
            throw new UsernameNotFoundException("Username "+ username + " not found");
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), getGrantedAuthorities(user));
    }

    private Collection<GrantedAuthority> getGrantedAuthorities(User user) {
		Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        Boolean isAdmin = false;
        List<Authorities> authorities = this.authoritiesService.findByUserId(user.getId());
        for(Authorities a : authorities){
            if(a.getAuthority().equals("admin")){
                isAdmin = true;
                break;
            }
        }
		if(isAdmin){
			grantedAuthorities.add(new SimpleGrantedAuthority("admin"));
		}
		grantedAuthorities.add(new SimpleGrantedAuthority("user"));
		return grantedAuthorities;
	}
    
}
