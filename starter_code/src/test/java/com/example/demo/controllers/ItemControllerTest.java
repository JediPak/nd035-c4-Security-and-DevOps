package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemControllerTest {
   private ItemController itemController;
   private ItemRepository itemRepository = mock(ItemRepository.class);

   @Before
   public void setUp(){
      itemController = new ItemController();
      TestUtils.injectObject(itemController, "itemRepository", itemRepository);
   }

   @Test
   public void getItems() throws Exception{

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

      List <Item> itemList = new ArrayList <>();
      itemList.add(item);
      itemList.add(item1);

      when(itemRepository.findAll()).thenReturn(itemList);

      ResponseEntity<List<Item>> responseEntity = itemController.getItems();

      assertNotNull(responseEntity);
      assertEquals(HttpStatus.OK.value(), responseEntity.getStatusCodeValue());

      List<Item> itemList1 = responseEntity.getBody();
      assertEquals(2, itemList1.size());

   }

   @Test
   public void getItemById() throws Exception{

      Item item = new Item();
      item.setId(0L);
      item.setDescription("description");
      item.setName("name");
      item.setPrice(BigDecimal.valueOf(3.21));

      when(itemRepository.findById(0L)).thenReturn(Optional.of(item));

      ResponseEntity<Item> responseEntity = itemController.getItemById(0L);

      assertNotNull(responseEntity);
      assertEquals(HttpStatus.OK.value(), responseEntity.getStatusCodeValue());

      Item item1 = responseEntity.getBody();
      assertEquals(item.getId(), item1.getId());
      assertEquals(item.getDescription(), item1.getDescription());
      assertEquals(item.getName(), item1.getName());
      assertEquals(item.getPrice(), item1.getPrice());

   }

   @Test
   public void getItemsByName() throws Exception{

      Item item = new Item();
      item.setId(0L);
      item.setDescription("description");
      item.setName("name");
      item.setPrice(BigDecimal.valueOf(3.21));

      Item item1 = new Item();
      item1.setId(1L);
      item1.setDescription("description1");
      item1.setName("name");
      item1.setPrice(BigDecimal.valueOf(4.21));

      List <Item> itemList = new ArrayList <>();
      itemList.add(item);
      itemList.add(item1);

      when(itemRepository.findByName("name")).thenReturn(itemList);

      ResponseEntity<List<Item>> responseEntity = itemController.getItemsByName("name");

      assertNotNull(responseEntity);
      assertEquals(HttpStatus.OK.value(), responseEntity.getStatusCodeValue());

      List<Item> itemList1 = responseEntity.getBody();
      assertEquals(2, itemList1.size());

   }

   @Test
   public void getItemsByName__noItems() throws Exception{

      when(itemRepository.findByName("name")).thenReturn(null);

      ResponseEntity<List<Item>> responseEntity = itemController.getItemsByName("name");

      assertNotNull(responseEntity);
      assertEquals(HttpStatus.NOT_FOUND.value(), responseEntity.getStatusCodeValue());

   }

}
