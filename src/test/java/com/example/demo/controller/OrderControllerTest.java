package com.example.demo.controller;

import com.example.demo.TestUtils;
import com.example.demo.controllers.OrderController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Customer;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
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
    public void successfulSubmitOrder()throws Exception {
        // TODO
        Item item1 = ObjectBuilder.buildItem();
        ArrayList<Item> itemList = new ArrayList<>();
        itemList.add(item1);

        Customer customer = ObjectBuilder.buildUser();

        Cart cart1 = ObjectBuilder.buildCart(customer, itemList);
        cart1.setCustomer(customer);
        cart1.setItems(itemList);
        customer.setCart(cart1);

        when(userRepository.findByUsername(ObjectBuilder.USERNAME)).thenReturn(customer);
        ResponseEntity<UserOrder> submitOrder = orderController.submit(ObjectBuilder.USERNAME);
        assertNotNull(submitOrder);
        assertEquals(200, submitOrder.getStatusCodeValue());

        UserOrder order = submitOrder.getBody();
        assertNotNull(order);
        assertEquals(1, order.getItems().size());
    }

    @Test
    public void successfulGetOrderForUser(){
        // TODO
        Item item1 = ObjectBuilder.buildItem();
        ArrayList<Item> itemList = new ArrayList<>();
        itemList.add(item1);

        Customer customer = ObjectBuilder.buildUser();

        Cart cart1 = ObjectBuilder.buildCart(customer, itemList);
        cart1.setCustomer(customer);
        cart1.setItems(itemList);
        customer.setCart(cart1);

        when(userRepository.findByUsername("orderHistSuccess")).thenReturn(customer);

        final ResponseEntity<List<UserOrder>> userOrderHistory =
                orderController.getOrdersForUser("orderHistSuccess");

        assertNotNull(userOrderHistory);
        assertEquals(200, userOrderHistory.getStatusCodeValue());

        List<UserOrder> orderList = userOrderHistory.getBody();
        assertNotNull(orderList);
    }


}
