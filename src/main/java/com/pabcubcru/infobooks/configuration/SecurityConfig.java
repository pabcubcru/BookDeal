package com.pabcubcru.infobooks.configuration;

import javax.sql.DataSource;

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

	@Autowired
	DataSource dataSource;
	
	public void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
		.antMatchers("/", "/resources/**").permitAll()
		.antMatchers("/user/**").hasAnyAuthority("user")
		.antMatchers("/register", "/login", "/login-error").anonymous()
		//.antMatchers("/login").anonymous()
		//.antMatchers("/login-error").anonymous()
		.and().csrf().disable()
		.formLogin().loginPage("/login")
		.failureUrl("/login-error")
		.and()
		.logout()
		.logoutSuccessUrl("/");
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
