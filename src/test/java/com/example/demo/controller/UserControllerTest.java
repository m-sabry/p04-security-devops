package com.example.demo.controller;

import com.example.demo.TestUtils;
import com.example.demo.controllers.UserController;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
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

    @Test
    public void successfulUserCreation()throws Exception {
        when(bCryptPasswordEncoder.encode(ObjectBuilder.PASSWORD)).thenReturn("ItIsOK");

        final ResponseEntity<User> userResponse = createUser();
        assertNotNull(userResponse);
        assertEquals(200, userResponse.getStatusCodeValue());

        User user = userResponse.getBody();
        assertNotNull(user);
        assertEquals(0, user.getId());
        assertEquals(ObjectBuilder.USERNAME, user.getUsername());
        assertEquals("ItIsOK", user.getPassword());
    }


    @Test
    public void successfulFindUserByName(){
        // _TODO
        User user = new User();
        user.setUsername(ObjectBuilder.USERNAME);
        user.setPassword(ObjectBuilder.PASSWORD);
        when(userRepository.findByUsername(ObjectBuilder.USERNAME)).thenReturn(user);

        final ResponseEntity<User> userResponse = userController.findByUsername(ObjectBuilder.USERNAME);
        assertEquals (200, userResponse.getStatusCodeValue());
        assertNotNull(userResponse);
        assertNotNull(ObjectBuilder.USERNAME);
        assertEquals(ObjectBuilder.USERNAME, userResponse.getBody().getUsername());
    }

    @Test
    public void successfulFindUserById(){
        // _TODO
        User user = new User();
        user.setId(1);
        user.setUsername(ObjectBuilder.USERNAME);
        user.setPassword(ObjectBuilder.PASSWORD);
        when(userRepository.findById(1L)).thenReturn(java.util.Optional.of(user));

        final ResponseEntity<User> userResponse = userController.findById(1L);
        assertEquals (200, userResponse.getStatusCodeValue());
        assertNotNull(userResponse);
        assertNotNull(1L);
        assertEquals(1L, userResponse.getBody().getId());
    }

    private ResponseEntity<User> createUser(){
        return userController.createUser(ObjectBuilder.buildUserRequest(ObjectBuilder.USERNAME, ObjectBuilder.PASSWORD));
    }

}
