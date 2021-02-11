package com.example.demo.controllers;

import java.util.Optional;
import java.util.stream.IntStream;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.Customer;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;

@Slf4j
@RestController
@RequestMapping("/api/cart")
public class CartController {

	private @Autowired UserRepository userRepository;
	private @Autowired CartRepository cartRepository;
	private @Autowired ItemRepository itemRepository;
	
	@PostMapping("/addToCart")
	public ResponseEntity<Cart> addToCart(@RequestBody ModifyCartRequest request) {
		Customer customer = userRepository.findByUsername(request.getUsername());
		if(customer == null) {
			log.info("  =====  AddToCart, user with name: " + request.getUsername() + " doesn't exist");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		Optional<Item> item = itemRepository.findById(request.getItemId());
		if(!item.isPresent()) {
			log.info("  =====  AddToCart, item with id: " + request.getItemId() + " doesn't exist");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		Cart cart = customer.getCart();
		IntStream.range(0, request.getQuantity())
			.forEach(i -> cart.addItem(item.get()));
		cartRepository.save(cart);
		log.info("  =====  AddToCart, item with id: " + request.getItemId() + " Added to "+ customer.getUsername() +"'s cart");
		return ResponseEntity.ok(cart);
	}
	
	@PostMapping("/removeFromCart")
	public ResponseEntity<Cart> removeFromCart(@RequestBody ModifyCartRequest request) {
		Customer customer = userRepository.findByUsername(request.getUsername());
		if(customer == null) {
			log.info("  =====  RemoveFromCart, user with name: " + request.getUsername() + " doesn't exist");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		Optional<Item> item = itemRepository.findById(request.getItemId());
		if(!item.isPresent()) {
			log.info("  =====  RemoveFromCart, item with id: " + request.getItemId() + " doesn't exist");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		Cart cart = customer.getCart();
		IntStream.range(0, request.getQuantity())
			.forEach(i -> cart.removeItem(item.get()));
		cartRepository.save(cart);
		log.info("  =====  RemoveFromCart, item with id: " + request.getItemId() + " successfully removed from "+ customer.getUsername() +"'s cart");
		return ResponseEntity.ok(cart);
	}
		
}
