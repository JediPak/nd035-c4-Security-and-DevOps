package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartControllerTest {
   private CartController cartController;
   private UserRepository userRepository = mock(UserRepository.class);
   private CartRepository cartRepository = mock(CartRepository.class);
   private ItemRepository itemRepository = mock(ItemRepository.class);

   @Before
   public void setUp(){
      cartController = new CartController();
      TestUtils.injectObject(cartController, "userRepository", userRepository);
      TestUtils.injectObject(cartController, "cartRepository", cartRepository);
      TestUtils.injectObject(cartController, "itemRepository", itemRepository);
   }

   @Test
   public void addToCart() throws Exception{

      User user = new User();
      user.setId(0L);
      user.setUsername("test");
      user.setPassword("testPassword");

      Item item = new Item();
      item.setId(0L);
      item.setDescription("description");
      item.setName("name");
      item.setPrice(BigDecimal.valueOf(3.21));

      Cart cart = new Cart();
      cart.setId(0L);
      cart.setUser(user);
      cart.addItem(item);
      user.setCart(cart);
      when(userRepository.findByUsername("test")).thenReturn(user);

      when(itemRepository.findById(0L)).thenReturn(java.util.Optional.of(item));

      ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
      modifyCartRequest.setUsername("test");
      modifyCartRequest.setItemId(0L);
      modifyCartRequest.setQuantity(1);

      ResponseEntity<Cart> responseEntity = cartController.addToCart(modifyCartRequest);

      assertNotNull(responseEntity);
      assertEquals(HttpStatus.OK.value(), responseEntity.getStatusCodeValue());

      Cart cartResponse = responseEntity.getBody();
      assertNotNull(cartResponse);
      assertEquals(java.util.Optional.ofNullable(0L), java.util.Optional.ofNullable(cartResponse.getId()));
      assertEquals(BigDecimal.valueOf(3.21 * 2), cartResponse.getTotal());
      assertEquals(2, cartResponse.getItems().size());
   }

   @Test
   public void addToCart_noUser() throws Exception{

      when(userRepository.findByUsername("test")).thenReturn(null);

      ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
      modifyCartRequest.setUsername("test");
      modifyCartRequest.setItemId(0L);
      modifyCartRequest.setQuantity(1);

      ResponseEntity<Cart> responseEntity = cartController.addToCart(modifyCartRequest);

      assertNotNull(responseEntity);
      assertEquals(HttpStatus.NOT_FOUND.value(), responseEntity.getStatusCodeValue());
   }

   @Test
   public void addToCart_noItem() throws Exception{

      User user = new User();
      user.setId(0L);
      user.setUsername("test");
      user.setPassword("testPassword");
      Cart cart = new Cart();
      cart.setId(0L);
      cart.setUser(user);
      user.setCart(cart);
      when(userRepository.findByUsername("test")).thenReturn(user);

      ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
      modifyCartRequest.setUsername("test");
      modifyCartRequest.setItemId(0L);
      modifyCartRequest.setQuantity(1);

      ResponseEntity<Cart> responseEntity = cartController.addToCart(modifyCartRequest);

      assertNotNull(responseEntity);
      assertEquals(HttpStatus.NOT_FOUND.value(), responseEntity.getStatusCodeValue());

   }

   @Test
   public void removeFromcart() throws Exception{

      User user = new User();
      user.setId(0L);
      user.setUsername("test");
      user.setPassword("testPassword");

      Item item = new Item();
      item.setId(0L);
      item.setDescription("description");
      item.setName("name");
      item.setPrice(BigDecimal.valueOf(3.21));

      Cart cart = new Cart();
      cart.setId(0L);
      cart.setUser(user);
      cart.addItem(item);
      cart.addItem(item);
      cart.addItem(item);
      cart.addItem(item);

      user.setCart(cart);
      when(userRepository.findByUsername("test")).thenReturn(user);

      when(itemRepository.findById(0L)).thenReturn(java.util.Optional.of(item));

      ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
      modifyCartRequest.setUsername("test");
      modifyCartRequest.setItemId(0L);
      modifyCartRequest.setQuantity(1);

      ResponseEntity<Cart> responseEntity = cartController.removeFromcart(modifyCartRequest);

      assertNotNull(responseEntity);
      assertEquals(HttpStatus.OK.value(), responseEntity.getStatusCodeValue());

      Cart cartResponse = responseEntity.getBody();
      assertNotNull(cartResponse);
      assertEquals(java.util.Optional.ofNullable(0L), java.util.Optional.ofNullable(cartResponse.getId()));
      assertEquals(BigDecimal.valueOf(3.21 * 3).setScale(2, RoundingMode.CEILING), cartResponse.getTotal());
      assertEquals(3, cartResponse.getItems().size());
   }
}
