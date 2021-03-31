package com.pabcubcru.infobooks.configuration;

import com.pabcubcru.infobooks.services.UserLoginService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserLoginService userLoginService;
	
	public void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
		.antMatchers("/").permitAll()
		.antMatchers("/resources/**").permitAll()
		.antMatchers("/user/**").hasAnyAuthority("user")
		.antMatchers("/books/list/all-me", "/books/{id}", "/books/get/{id}").permitAll()
		.antMatchers("/books/**").authenticated()
		.antMatchers("/register", "/login", "/login-error").anonymous()
		.and().csrf().disable()
		.formLogin().loginPage("/login")
		.failureUrl("/login-error")
		.and()
		.logout()
		.logoutSuccessUrl("/");

		http.headers().frameOptions().sameOrigin();
	}

	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userLoginService).passwordEncoder(passwordEncoder());	
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {	    
		return new BCryptPasswordEncoder();
	}
	

}
