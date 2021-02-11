package com.example.demo.controller;

import com.example.demo.TestUtils;
import com.example.demo.controllers.ItemController;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
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

public class ItemControllerTest {
    private ItemController itemController;
    private ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setUp(){
        itemController = new ItemController();
        TestUtils.injectObject(itemController, "itemRepository", itemRepository);
    }

    @Test
    public void successfulGetItems(){
        // _TODO
        ResponseEntity<List<Item>> itemResponse = itemController.getItems();
        assertNotNull(itemResponse);
        assertEquals(HttpStatus.OK, itemResponse.getStatusCode());
        assertNotNull(itemResponse.getBody());
        assertEquals(0, itemResponse.getBody().size());
    }


    @Test
    public void successfulGetItemById(){
        // _TODO
        Item item = ObjectBuilder.buildItem();

        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));
        ResponseEntity<Item> resp = itemController.getItemById(1L);
        assertNotNull(resp);
        assertEquals(HttpStatus.OK, resp.getStatusCode());

        Item retrievedItem = resp.getBody();
        assertNotNull(item);
        assertEquals(item.getId(), retrievedItem.getId());
        assertEquals(item.getName(), retrievedItem.getName());
        assertEquals(item.getDescription(), retrievedItem.getDescription());
        assertEquals(item.getPrice(), retrievedItem.getPrice());
    }

    @Test
    public void successfulGetItemByName(){
        // _TODO
        String itemName = "Item Name";
        String itemDescription = "Item Description";
        Long itemId = 1L;
        BigDecimal price = new BigDecimal ("10.0");

        Item item = ObjectBuilder.buildItem();

        List<Item> items = new ArrayList<>();
        items.add(item);

        when (itemRepository.findByName(itemName)).thenReturn(items);

        ResponseEntity<List<Item>> itemResponse = itemController.getItemsByName(itemName);
        assertNotNull(itemResponse);
        assertEquals(HttpStatus.OK, itemResponse.getStatusCode());

        List<Item> retrievedItem = itemResponse.getBody();
        assertNotNull(retrievedItem);
        assertEquals (itemId, retrievedItem.get(0).getId());
        assertEquals(itemName, retrievedItem.get(0).getName());
        assertEquals(itemDescription, retrievedItem.get(0).getDescription());
        assertEquals(price, retrievedItem.get(0).getPrice());
    }

}
