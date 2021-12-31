package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {
   private UserController userController;
   private UserRepository userRepository = mock(UserRepository.class);
   private CartRepository cartRepository = mock(CartRepository.class);
   private BCryptPasswordEncoder bCryptPasswordEncoder = mock(BCryptPasswordEncoder.class);

   @Before
   public void setUp(){
      userController = new UserController();
      TestUtils.injectObject(userController, "userRepository", userRepository);
      TestUtils.injectObject(userController, "cartRepository", cartRepository);
      TestUtils.injectObject(userController, "bCryptPasswordEncoder", bCryptPasswordEncoder);
   }

//   @Ignore
   @Test
   // "sanity test" -testing basic functionality
   public void create_user_happy_path() throws Exception{

      when(bCryptPasswordEncoder.encode("testPassword")).thenReturn("hashed_testPassword");
      CreateUserRequest createUserRequest = new CreateUserRequest();
      createUserRequest.setUsername("test");
      createUserRequest.setPassword("testPassword");
      createUserRequest.setConfirmPassword("testPassword");
      ResponseEntity<User> responseEntity = userController.createUser(createUserRequest);

      assertNotNull(responseEntity);
      assertEquals(HttpStatus.OK.value(), responseEntity.getStatusCodeValue());

      User user = responseEntity.getBody();
      assertNotNull(user);
      assertEquals(0, user.getId());
      assertEquals("test", user.getUsername());
      assertEquals("hashed_testPassword", user.getPassword());
   }

   @Test
   public void create_user_sad_path() throws Exception{

      CreateUserRequest createUserRequest = new CreateUserRequest();
      createUserRequest.setUsername("test");
      createUserRequest.setPassword("short");
      createUserRequest.setConfirmPassword("short");
      ResponseEntity<User> responseEntity = userController.createUser(createUserRequest);

      assertNotNull(responseEntity);
      assertEquals(HttpStatus.BAD_REQUEST.value(), responseEntity.getStatusCodeValue());
   }

   @Test
   public void findById() throws Exception{

      User user = new User();
      user.setId(0L);
      user.setUsername("test");
      user.setPassword("testPassword");
      when(userRepository.findById(0L)).thenReturn(java.util.Optional.of(user));
      ResponseEntity<User> responseEntity = userController.findById(0L);

      assertNotNull(responseEntity);
      assertEquals(HttpStatus.OK.value(), responseEntity.getStatusCodeValue());
      assertEquals(0, user.getId());
      assertEquals("test", user.getUsername());
      assertEquals("testPassword", user.getPassword());
   }

   @Test
   public void findByUserName_noUser() throws Exception{
      when(userRepository.findByUsername("noUsername")).thenReturn(null);
      ResponseEntity<User> responseEntity = userController.findByUserName("noUsername");

      assertNotNull(responseEntity);
      assertEquals(HttpStatus.NOT_FOUND.value(), responseEntity.getStatusCodeValue());
   }

   @Test
   public void findByUserName_User() throws Exception{

      User user = new User();
      user.setId(0L);
      user.setUsername("test");
      user.setPassword("testPassword");
      when(userRepository.findByUsername("test")).thenReturn(user);

      ResponseEntity<User> responseEntity = userController.findByUserName("test");

      assertNotNull(responseEntity);
      assertEquals(200, responseEntity.getStatusCodeValue());
      assertEquals(0, user.getId());
      assertEquals("test", user.getUsername());
      assertEquals("testPassword", user.getPassword());
   }

}
