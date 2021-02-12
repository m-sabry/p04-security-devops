package com.example.demo.controllers;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Customer;
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
	public ResponseEntity<Customer> findById(@PathVariable Long id) {
		return ResponseEntity.of(userRepository.findById(id));
	}
	
	@GetMapping("/{username}")
	public ResponseEntity<Customer> findByUsername(@PathVariable String username) {
		Customer customer = userRepository.findByUsername(username);
		return customer == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(customer);
	}
	
	@PostMapping("/create")
	public ResponseEntity<Customer> createUser(@RequestBody CreateUserRequest createUserRequest) {

		if(createUserRequest.getPassword().length() < 7 ||
				!createUserRequest.getPassword().equals(createUserRequest.getConfirmPassword())){
			log.info(" ** USER_NOT_CREATED, password doesn't match password pattern");
			return ResponseEntity.badRequest().build();
		}

		// Creating an empty cart
		Cart cart = new Cart();
		cartRepository.save(cart);

		// salting, hashing password
		SecureRandom random = new SecureRandom();
		byte[] salt = new byte[16];
		random.nextBytes(salt);
		String encodedSalt = Base64.getEncoder().encodeToString(salt);
		String password = HashUtil.getHashedValue(createUserRequest.getPassword(), encodedSalt);

		// Set user attributes
		Customer customer = new Customer();
		customer.setUsername(createUserRequest.getUsername()); // username
		customer.setCart(cart); // cart
		customer.setPassword(password); // password
		customer.setSalt(encodedSalt); // salt
		customer.setPassword(bCryptPasswordEncoder.encode(createUserRequest.getPassword()));
		userRepository.save(customer);
		log.info(" ** USER_CREATED, user name: " + createUserRequest.getUsername());
		return ResponseEntity.ok(customer);
	}
	
}
