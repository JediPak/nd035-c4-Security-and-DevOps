package com.example.demo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.boot.autoconfigure.security.Http401AuthenticationEntryPoint;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

@EnableWebSecurity
/**
 * After defining the authentication and authorization modules, we need to configure them on the Spring Security filter chain.
 * The WebSecurity class is a custom implementation of the default web security configuration provided by Spring Security.
 * In this class, we have overridden two overloaded methods:
 * **/
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {
	
	private UserDetailsServiceImpl userDetailsService;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
	
    public WebSecurityConfiguration(UserDetailsServiceImpl userDetailsService,
			BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.userDetailsService = userDetailsService;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}
    
    @Override
    /**
     * Defines public resources. Below, we have set the SIGN_UP_URL endpoint as public. The http.cors() is used to make the Spring Security support the CORS (Cross-Origin Resource Sharing) and CSRF (Cross-Site Request Forgery).
     * Read more here.
     * (https://docs.spring.io/spring-security/site/docs/4.2.x/reference/html/cors.html)
     * **/
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable().authorizeRequests()
                .antMatchers(HttpMethod.POST, SecurityConstants.SIGN_UP_URL).permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilter(new JWTAuthenticationFilter(authenticationManager()))
                .addFilter(new JWTAuthenticationVerficationFilter(authenticationManager()))
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                //Adding below line of code to get 401 error code when try login with incorrect Username or Password.
                .and().exceptionHandling().authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));

    }
    
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
    
    @Override
    /**
     * It declares the BCryptPasswordEncoder as the encoding technique, and loads user-specific data.
     * **/
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth//.parentAuthenticationManager(authenticationManagerBean())
                //Removing line of code as it send your code to infinite loop when login with incorrect Username or Password.
            .userDetailsService(userDetailsService)
            .passwordEncoder(bCryptPasswordEncoder);
    }
}
