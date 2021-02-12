package com.example.demo.security;

import java.util.Collections;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.model.persistence.Customer;
import com.example.demo.model.persistence.repositories.UserRepository;

/**
 * It implements the UserDetailsService interface,
 * and defines only one method that retrieves the User obejct from the database
 */
@Slf4j
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Customer customer = userRepository.findByUsername(username);
        if (customer == null) {
            log.info(" ===== CUSTOMER_DOES_NOT_EXIST: " + username + " doesn't exist");
            throw new UsernameNotFoundException(username);
        }
        return new User(customer.getUsername(), customer.getPassword(), Collections.emptyList());
    }
}
