package com.example.demo.model.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Customer;

public interface CartRepository extends JpaRepository<Cart, Long> {
//	Cart findByCustomer(Customer customer);
}
