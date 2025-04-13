package com.orders;

import com.orders.dto.OrderDto;
import com.orders.dto.CustomerDto;
import com.orders.entity.Customer;
import com.orders.repository.CustomerRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.test.annotation.Rollback;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
@Rollback
class OrderIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    void testCreateAndGetOrder() {
        // Arrange: Create a new customer
        Customer testCustomer = new Customer();
        testCustomer.setName("John Doe");
        testCustomer.setEmail("john.doe@example.com");
        testCustomer.setMobileNumber("123456789");
        Customer savedCustomer = customerRepository.save(testCustomer);
        System.out.println("Customer saved with ID: " + savedCustomer.getCustomerId());

        // Create a CustomerDto from the saved customer
        CustomerDto customerDto = new CustomerDto();
        customerDto.setName(savedCustomer.getName());
        customerDto.setEmail(savedCustomer.getEmail());
        customerDto.setMobileNumber(savedCustomer.getMobileNumber());

        // Arrange: Create a new order linked to the customer
        OrderDto newOrder = new OrderDto();
        newOrder.setProduct("Test Product");
        newOrder.setAmount(100L);
        newOrder.setOrderDate(LocalDate.now());
        newOrder.setPrice(49.99);
        newOrder.setCustomer(customerDto);

        // Act: Create the order via the API
        ResponseEntity<OrderDto> createResponse = restTemplate.postForEntity("/api/orders", newOrder, OrderDto.class);
        System.out.println("Response Status: " + createResponse.getStatusCode());
        System.out.println("Response Body: " + createResponse.getBody());
        
        assertEquals(HttpStatus.CREATED, createResponse.getStatusCode(), "Expected status 201 CREATED but got: " + createResponse.getStatusCode());
        assertNotNull(createResponse.getBody());
    }
}
