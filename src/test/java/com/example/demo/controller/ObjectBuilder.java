package com.example.demo.controller;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.requests.CreateUserRequest;
import com.example.demo.model.requests.ModifyCartRequest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ObjectBuilder {

    public static final String USERNAME = "mostafa";
    public static final String PASSWORD = "password";

    // User
    public static User buildUser() {
        User user = new User();
        user.setId(1L);
        user.setUsername(USERNAME);
        user.setPassword(PASSWORD);
        user.setCart(new Cart());
        return user;
    }
    public static CreateUserRequest buildUserRequest(String username, String password){
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername(username);
        request.setPassword(password);
        request.setConfirmPassword(password);
        return request;
    }

    // Item
    public static List<Item> buildItemsList() {
        List<Item> items = new ArrayList<>();
        items.add(buildItem());
        return items;
    }
    public static Item buildItem() {
        String name = "Item Name";
        String description = "Item Description";
        Long id = 1L;
        BigDecimal price = new BigDecimal ("10.0");

        Item item = new Item();
        item.setId(id);
        item.setName(name);
        item.setPrice(price);
        item.setDescription(description);
        return item;
    }

    // Cart
    public static Cart buildCart(User user, List<Item> items) {
        Cart myCart = new Cart();
        myCart.setId(1L);
        myCart.setItems(items);
        myCart.setUser(user);
        return myCart;
    }
    public static ModifyCartRequest buildCartRequest(Long itemId, int quantity, String username) {
        ModifyCartRequest cartReq = new ModifyCartRequest();
        cartReq.setItemId(itemId);
        cartReq.setQuantity(quantity);
        cartReq.setUsername(username);
        return cartReq;
    }

}
