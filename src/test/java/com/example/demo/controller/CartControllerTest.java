package com.example.demo.controller;

import com.example.demo.TestUtils;
import com.example.demo.controllers.CartController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Customer;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartControllerTest {
    private CartController cartController;
    private final UserRepository userRepository = mock(UserRepository.class);
    private final CartRepository cartRepository = mock(CartRepository.class);
    private final ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setUp(){
        cartController = new CartController();
        TestUtils.injectObject(cartController, "userRepository", userRepository);
        TestUtils.injectObject(cartController, "cartRepository", cartRepository);
        TestUtils.injectObject(cartController, "itemRepository", itemRepository);
    }

    @Test
    public void successfulAddToCart()throws Exception {
        // _TODO

        Customer customer = ObjectBuilder.buildUser();
        List<Item> items = ObjectBuilder.buildItemsList();
        Item item0 = items.get(0);
        Cart cart = ObjectBuilder.buildCart(customer, items);
        ModifyCartRequest cartReq = ObjectBuilder.buildCartRequest(item0.getId(), 10, customer.getUsername());

        when (userRepository.findByUsername(cartReq.getUsername())).thenReturn(customer);
        when(itemRepository.findById(cartReq.getItemId())).thenReturn(Optional.of(item0));

        final ResponseEntity<Cart> cartRespEntity = cartController.addToCart(cartReq);
        assertNotNull(cartRespEntity);
        assertEquals(200, cartRespEntity.getStatusCodeValue());

        Cart cart1 = cartRespEntity.getBody();

        assertNotNull(cart);
        assertEquals(items.size(), cart.getItems().size());
    }

    @Test
    public void successfulRemoveFromCart(){
        // _TODO implement
        Customer customer = ObjectBuilder.buildUser(); // user
        List<Item> items = ObjectBuilder.buildItemsList(); // list of items with one item only
        Item item0 = items.get(0);
        ModifyCartRequest cartReq = ObjectBuilder.buildCartRequest(items.get(0).getId(), 1, customer.getUsername());
        Cart cart = ObjectBuilder.buildCart(customer, items); // creating cart with two items

        when (userRepository.findByUsername(cartReq.getUsername())).thenReturn(customer);
        when(itemRepository.findById(cartReq.getItemId())).thenReturn(Optional.of(item0));
        when(cartRepository.findById(customer.getCart().getId())).thenReturn(Optional.of(cart));

        // Adding item to cart
        ResponseEntity<Cart> cartResponse = cartController.addToCart(cartReq);
        assertNotNull(cartResponse);
        assertEquals(200, cartResponse.getStatusCodeValue());
        assertEquals(cart.getItems().size(), Objects.requireNonNull(cartResponse.getBody()).getItems().size());

        // Removing an item from the cart
        items.remove(0);
        Cart modifiedCart = ObjectBuilder.buildCart(customer, items);
        ModifyCartRequest toBeRemoved = ObjectBuilder.buildCartRequest(item0.getId(), 1, customer.getUsername());
        ResponseEntity<Cart> cartResponseAfterRemove = cartController.removeFromCart(toBeRemoved);

        when(cartRepository.findById(customer.getCart().getId())).thenReturn(Optional.of(modifiedCart));

        assertEquals(200, cartResponseAfterRemove.getStatusCodeValue());
        assertNotNull(cartResponseAfterRemove);

        Cart cart1 = cartResponseAfterRemove.getBody();
        assertEquals(200, cartResponseAfterRemove.getStatusCodeValue());
        assertEquals(modifiedCart.getItems().size(), cart1.getItems().size());
    }

}
