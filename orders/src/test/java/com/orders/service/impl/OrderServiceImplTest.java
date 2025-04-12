package com.orders.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.util.Optional;

import com.orders.dto.OrderDto;
import com.orders.entity.Customer;
import com.orders.entity.Order;
import com.orders.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

@Test
void testGetOrderById() {
    Customer customer = new Customer();
    customer.setName("John Doe");
    Order order = new Order();
    order.setCustomer(customer);

    // Mocking the behavior of the order repository
    when(orderRepository.findById(anyLong())).thenReturn(Optional.of(order));

    // Call the method to test
    OrderDto orderDto = orderService.getOrder(1L);

    // Asserting the result
    assertNotNull(orderDto);
    assertEquals("John Doe", orderDto.getCustomer().getName());
}

}
