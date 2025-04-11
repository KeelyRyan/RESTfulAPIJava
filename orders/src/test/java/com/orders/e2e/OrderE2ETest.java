package com.orders.e2e;

import com.orders.dto.CustomerDto;
import com.orders.dto.OrderDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
public class OrderE2ETest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void fullE2EOrderFlow() {
        // Step 1: Create customer
        CustomerDto customerDto = new CustomerDto("Alice Wonderland", "alice@example.com", "1112223334");
        ResponseEntity<CustomerDto> customerResponse = restTemplate.postForEntity("/api/customer", customerDto, CustomerDto.class);
        assertEquals(HttpStatus.OK, customerResponse.getStatusCode());
        assertNotNull(customerResponse.getBody());
        System.out.println("Customer created: " + customerResponse.getBody());
    
        // Step 2: Create order for customer
        OrderDto orderDto = new OrderDto();
        orderDto.setCustomer(customerDto);
        orderDto.setAmount(5L);
        orderDto.setPrice(199.99);
        orderDto.setProduct("Magic Hat");
        orderDto.setOrderDate(LocalDate.now());
    
        ResponseEntity<OrderDto> createOrderResponse = restTemplate.postForEntity("/api/orders", orderDto, OrderDto.class);
        assertEquals(HttpStatus.CREATED, createOrderResponse.getStatusCode());
    
        OrderDto createdOrder = createOrderResponse.getBody();
        assertNotNull(createdOrder);
        Long orderId = createdOrder.getOrderId();
        assertNotNull(orderId, "Order ID should not be null after creation");
    
        // Step 3: Fetch the order
        ResponseEntity<OrderDto> getOrder = restTemplate.getForEntity("/api/orders/" + orderId, OrderDto.class);
        assertEquals(HttpStatus.OK, getOrder.getStatusCode());
        assertEquals("Magic Hat", getOrder.getBody().getProduct());
    
        // Step 4: Update the order
        orderDto.setProduct("Invisibility Cloak");
        HttpEntity<OrderDto> updateRequest = new HttpEntity<>(orderDto);
        restTemplate.put("/api/orders/" + orderId, updateRequest);
    
        // Step 5: Fetch updated order
        OrderDto updated = restTemplate.getForObject("/api/orders/" + orderId, OrderDto.class);
        assertEquals("Invisibility Cloak", updated.getProduct());
    
        // Step 6: Fetch all orders for the customer (paged)
        ResponseEntity<Map> response = restTemplate.getForEntity(
            "/api/customer/" + orderId + "/orders",
            Map.class
        );
        List<Map<String, Object>> content = (List<Map<String, Object>>) response.getBody().get("content");
        assertTrue(content.size() >= 1);
    
        // Step 7: Delete order
        restTemplate.delete("/api/orders/" + orderId);
        ResponseEntity<OrderDto> deleted = restTemplate.getForEntity("/api/orders/" + orderId, OrderDto.class);
        assertEquals(HttpStatus.NOT_FOUND, deleted.getStatusCode());
    
        // Step 8: Delete customer
        restTemplate.delete("/api/customer/" + customerDto.getMobileNumber());
    }
    
}
