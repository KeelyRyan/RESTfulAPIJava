package com.orders;

import com.orders.entity.Order;
import com.orders.entity.Customer;
import com.orders.dto.OrderDto;
import com.orders.repository.OrderRepository;
import com.orders.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Commit;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OrderControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    @Commit
    void testGetOrder() {
        // Arrange: Create and save a new customer
        Customer customer = new Customer();
        customer.setName("John Doe");
        customer.setEmail("john.doe@example.com");
        customer.setMobileNumber("123456789");
        customer = customerRepository.saveAndFlush(customer);

        // Arrange: Create and save a new order linked to the customer
        Order testOrder = new Order();
        testOrder.setProduct("Test Product");
        testOrder.setAmount(100L);
        testOrder.setOrderDate(LocalDate.now());
        testOrder.setCustomer(customer);

        // Save and flush to ensure data is in the database
        Order savedOrder = orderRepository.saveAndFlush(testOrder);

        // Act: Make the REST API call to retrieve the order
        Long orderId = savedOrder.getOrderId();  // Get the dynamically generated ID
        ResponseEntity<OrderDto> response = restTemplate.getForEntity("/api/orders/" + orderId, OrderDto.class);

        // Assert: Validate the response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(orderId, response.getBody().getOrderId());
        assertEquals("Test Product", response.getBody().getProduct());
        assertEquals(100L, response.getBody().getAmount());
        assertEquals("John Doe", response.getBody().getCustomer().getName());
        assertEquals("john.doe@example.com", response.getBody().getCustomer().getEmail());
        assertEquals("123456789", response.getBody().getCustomer().getMobileNumber());
    }
}
