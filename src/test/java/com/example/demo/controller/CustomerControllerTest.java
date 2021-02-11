package com.example.demo.controller;

import com.example.demo.TestUtils;
import com.example.demo.controllers.UserController;
import com.example.demo.model.persistence.Customer;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CustomerControllerTest {
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

    @Test
    public void successfulUserCreation()throws Exception {
        when(bCryptPasswordEncoder.encode(ObjectBuilder.PASSWORD)).thenReturn("ItIsOK");

        final ResponseEntity<Customer> userResponse = createUser();
        assertNotNull(userResponse);
        assertEquals(200, userResponse.getStatusCodeValue());

        Customer customer = userResponse.getBody();
        assertNotNull(customer);
        assertEquals(0, customer.getId());
        assertEquals(ObjectBuilder.USERNAME, customer.getUsername());
        assertEquals("ItIsOK", customer.getPassword());
    }


    @Test
    public void successfulFindUserByName(){
        // _TODO
        Customer customer = new Customer();
        customer.setUsername(ObjectBuilder.USERNAME);
        customer.setPassword(ObjectBuilder.PASSWORD);
        when(userRepository.findByUsername(ObjectBuilder.USERNAME)).thenReturn(customer);

        final ResponseEntity<Customer> userResponse = userController.findByUsername(ObjectBuilder.USERNAME);
        assertEquals (200, userResponse.getStatusCodeValue());
        assertNotNull(userResponse);
        assertNotNull(ObjectBuilder.USERNAME);
        assertEquals(ObjectBuilder.USERNAME, userResponse.getBody().getUsername());
    }

    @Test
    public void successfulFindUserById(){
        // _TODO
        Customer customer = new Customer();
        customer.setId(1);
        customer.setUsername(ObjectBuilder.USERNAME);
        customer.setPassword(ObjectBuilder.PASSWORD);
        when(userRepository.findById(1L)).thenReturn(java.util.Optional.of(customer));

        final ResponseEntity<Customer> userResponse = userController.findById(1L);
        assertEquals (200, userResponse.getStatusCodeValue());
        assertNotNull(userResponse);
        assertNotNull(1L);
        assertEquals(1L, userResponse.getBody().getId());
    }

    private ResponseEntity<Customer> createUser(){
        return userController.createUser(ObjectBuilder.buildUserRequest(ObjectBuilder.USERNAME, ObjectBuilder.PASSWORD));
    }

}
