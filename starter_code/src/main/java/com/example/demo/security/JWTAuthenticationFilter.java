package com.example.demo.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.example.demo.model.persistence.User;
import com.fasterxml.jackson.databind.ObjectMapper;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;

/**
 * This custom class is responsible for the authentication process.
 * This class extends the UsernamePasswordAuthenticationFilter class,
 * which is available under both spring-security-web and spring-boot-starter-web dependency.
 * The Base class parses the user credentials (username and a password).
 * You can have a look at all the available methods of the Base class here
 * (https://docs.spring.io/spring-security/site/docs/current/api/org/springframework/security/web/authentication/UsernamePasswordAuthenticationFilter.html).
 * **/

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

   private AuthenticationManager authenticationManager;

   public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
      this.authenticationManager = authenticationManager;
   }

   // both internally used by spring boot
   @Override
   //It performs actual authentication by parsing (also called filtering) the user credentials.
   public Authentication attemptAuthentication(HttpServletRequest req,
                                               HttpServletResponse res) throws AuthenticationException {
      try {
         User credentials = new ObjectMapper()
                 .readValue(req.getInputStream(), User.class);

         Authentication auth =  authenticationManager.authenticate(
                 new UsernamePasswordAuthenticationToken(
                         credentials.getUsername(),
                         credentials.getPassword(),
                         new ArrayList<>()));
         logger.info("[JWTAuthenticationFilter] User name set up with" + credentials.getUsername() +
                 "has successfully logged in; please copy the token provided in the header");
         return auth;
      } catch (IOException e) {
         logger.error("[JWTAuthenticationFilter] User " +
                 "has failed to log in; please try again");
         throw new RuntimeException(e);
      }
   }

   @Override
   //This method is originally present in the parent of the Base class.
   // After overriding, this method will be called after a user logs in successfully.
   // Below, it is generating a String token (JWT) for this user.
   protected void successfulAuthentication(HttpServletRequest req,
                                           HttpServletResponse res,
                                           FilterChain chain,
                                           Authentication auth) throws IOException, ServletException {

      String token = JWT.create()
              .withSubject(((org.springframework.security.core.userdetails.User) auth.getPrincipal()).getUsername())
              .withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
              .sign(HMAC512(SecurityConstants.SECRET.getBytes()));
      res.addHeader(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + token);
   }
}