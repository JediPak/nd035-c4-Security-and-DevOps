package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTest {
   private OrderController orderController;
   private UserRepository userRepository = mock(UserRepository.class);
   private OrderRepository orderRepository = mock(OrderRepository.class);

   @Before
   public void setUp(){
      orderController = new OrderController();
      TestUtils.injectObject(orderController, "userRepository", userRepository);
      TestUtils.injectObject(orderController, "orderRepository", orderRepository);

   }

   @Test
   public void submit() throws Exception{

      User user = new User();
      user.setId(0L);
      user.setUsername("test");
      user.setPassword("testPassword");

      Item item = new Item();
      item.setId(0L);
      item.setDescription("description");
      item.setName("name");
      item.setPrice(BigDecimal.valueOf(3.21));

      Item item1 = new Item();
      item1.setId(1L);
      item1.setDescription("description1");
      item1.setName("name1");
      item1.setPrice(BigDecimal.valueOf(4.21));

      Cart cart = new Cart();
      cart.setId(0L);
      cart.setUser(user);
      cart.addItem(item);
      cart.addItem(item1);

      user.setCart(cart);

      when(userRepository.findByUsername("test")).thenReturn(user);

      ResponseEntity<UserOrder> responseEntity = orderController.submit("test");

      UserOrder userOrder = responseEntity.getBody();
      assertNotNull(responseEntity);
      assertEquals(HttpStatus.OK.value(), responseEntity.getStatusCodeValue());
      assertEquals(user, userOrder.getUser());
      assertEquals(item.getPrice().add(item1.getPrice()), userOrder.getTotal());
      assertEquals(2, userOrder.getItems().size());

   }

   @Test
   public void submit_noUser() throws Exception{

      when(userRepository.findByUsername("test")).thenReturn(null);

      ResponseEntity<UserOrder> responseEntity = orderController.submit("test");

      assertNotNull(responseEntity);
      assertEquals(HttpStatus.NOT_FOUND.value(), responseEntity.getStatusCodeValue());

   }

   @Test
   public void getOrdersForUser_noUser() throws Exception{

      when(userRepository.findByUsername("test")).thenReturn(null);

      ResponseEntity<List<UserOrder>> responseEntity = orderController.getOrdersForUser("test");

      assertNotNull(responseEntity);
      assertEquals(HttpStatus.NOT_FOUND.value(), responseEntity.getStatusCodeValue());

   }

}
