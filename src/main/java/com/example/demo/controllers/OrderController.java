package com.example.demo.controllers;

import java.util.List;

import com.example.demo.model.persistence.Customer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;

@Slf4j
@RestController
@RequestMapping("/api/order")
public class OrderController {
	
	
	@Autowired private UserRepository userRepository;
	@Autowired private OrderRepository orderRepository;
	
	
	@PostMapping("/submit/{username}")
	public ResponseEntity<UserOrder> submit(@PathVariable String username) {
		Customer customer = userRepository.findByUsername(username);
		if(customer == null) {
			log.info("  ===== Order creation, user with name: " + username + " doesn't exist");
			return ResponseEntity.notFound().build();
		}
		UserOrder order = UserOrder.createFromCart(customer.getCart());
		orderRepository.save(order);
		log.info("  ===== Order Creation, order successfully created for user with name: " + username);
		return ResponseEntity.ok(order);
	}
	
	@GetMapping("/history/{username}")
	public ResponseEntity<List<UserOrder>> getOrdersForUser(@PathVariable String username) {
		Customer customer = userRepository.findByUsername(username);
		if(customer == null) {
			log.info("  ===== getOrdersForUser, user with name: " + username + " doesn't exist");
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(orderRepository.findByCustomer(customer));
	}
}
