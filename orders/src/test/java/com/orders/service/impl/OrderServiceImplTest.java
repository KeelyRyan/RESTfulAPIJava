package com.orders.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;

import com.orders.dto.OrderDto;
import com.orders.entity.Order;
import com.orders.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    @Test
    void testGetOrderById() {
        // Arrange
        Order order = new Order(1L, LocalDate.now(), 100L, "Book", 12.99, null, null);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        // Act
        OrderDto orderDto = orderService.getOrder(1L);

        // Assert
        assertEquals(1L, orderDto.getOrderId());
        assertEquals("Book", orderDto.getProduct());
    }
}
