package com.example.demo.controllers;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import com.example.demo.security.HashUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.SecureRandom;
import java.util.Base64;


@Slf4j
@RestController
@RequestMapping("/api/user")
public class UserController {

	private @Autowired UserRepository userRepository;
	private @Autowired CartRepository cartRepository;
	private @Autowired BCryptPasswordEncoder bCryptPasswordEncoder;

	@GetMapping("/id/{id}")
	public ResponseEntity<User> findById(@PathVariable Long id) {
		return ResponseEntity.of(userRepository.findById(id));
	}
	
	@GetMapping("/{username}")
	public ResponseEntity<User> findByUsername(@PathVariable String username) {
		User user = userRepository.findByUsername(username);
		return user == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(user);
	}
	
	@PostMapping("/create")
	public ResponseEntity<User> createUser(@RequestBody CreateUserRequest createUserRequest) {

		if(createUserRequest.getPassword().length() < 7 ||
				!createUserRequest.getPassword().equals(createUserRequest.getConfirmPassword())){
			log.info("createUser, password doesn't match password pattern");
			return ResponseEntity.badRequest().build();
		}

		// Creating an empty cart
		Cart cart = new Cart();
		cartRepository.save(cart);
		log.info("Cart created successfully for " + createUserRequest.getUsername());

		// salting, hashing password
		SecureRandom random = new SecureRandom();
		byte[] salt = new byte[16];
		random.nextBytes(salt);
		String encodedSalt = Base64.getEncoder().encodeToString(salt);
		String password = HashUtil.getHashedValue(createUserRequest.getPassword(), encodedSalt);

		// Set user attributes
		User user = new User();
		user.setUsername(createUserRequest.getUsername()); // username
		user.setCart(cart); // cart
		user.setPassword(password); // password
		user.setSalt(encodedSalt); // salt
		user.setPassword(bCryptPasswordEncoder.encode(createUserRequest.getPassword()));
		userRepository.save(user);
		log.info("User successfully created " + createUserRequest.getUsername());
		return ResponseEntity.ok(user);
	}
	
}
