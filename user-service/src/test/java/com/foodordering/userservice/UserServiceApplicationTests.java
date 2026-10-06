package com.foodordering.userservice;

import com.foodordering.userservice.model.Customer;
import com.foodordering.userservice.model.Role;
import com.foodordering.userservice.repository.CustomerRepository;
import com.foodordering.userservice.security.JwtUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceApplicationTests {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    @Test
    void testCustomerCreationAndJwt() {
        Customer customer = new Customer("Test User", "testuser@example.com", passwordEncoder.encode("password"), "1234567890", Role.CUSTOMER);
        Customer saved = customerRepository.save(customer);

        assertNotNull(saved.getCustomerId());
        assertTrue(passwordEncoder.matches("password", saved.getPassword()));

        String token = jwtUtils.generateToken(saved.getEmail(), saved.getRole().name(), saved.getCustomerId());
        assertNotNull(token);
        assertTrue(jwtUtils.validateToken(token));
        assertEquals("testuser@example.com", jwtUtils.getEmailFromToken(token));
        assertEquals("CUSTOMER", jwtUtils.getRoleFromToken(token));
    }
}
