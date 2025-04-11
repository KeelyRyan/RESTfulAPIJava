package com.orders.controller;

import com.orders.dto.CustomerDto;
import com.orders.dto.OrderDto;
import com.orders.entity.Customer;
import com.orders.mapper.CustomerMapper;
import com.orders.service.IOrderService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class OrderController {

    private final IOrderService orderService;
    



    @GetMapping(path = "/orders/{orderId}")
    public ResponseEntity<OrderDto> getOrder(@PathVariable Long orderId) {
        OrderDto orderDto = orderService.getOrder(orderId);
        return ResponseEntity.ok(orderDto);
    }
    @GetMapping(path = "/orders")
    public Page<OrderDto> getAllOrders(
            @PageableDefault(size = 10, sort = "orderDate") Pageable pageable) {
        return orderService.getAllOrders(pageable);
    }
    @GetMapping("/customer/{customerId}/orders")
    public ResponseEntity<Page<OrderDto>> getOrdersByCustomerId(
            @PathVariable Long customerId,
            @PageableDefault(size = 10) Pageable pageable) {
        Page<OrderDto> orders = orderService.getAllCusOrdersByCustomerId(customerId, pageable);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/search/by-date")
    public ResponseEntity<List<OrderDto>> getOrdersWithinDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date cannot be after end date.");
        }

        List<OrderDto> orders = orderService.getOrdersWithinDateRange(startDate, endDate);
        return ResponseEntity.ok(orders);
    }
    
    @GetMapping("/sorted")
    public ResponseEntity<List<OrderDto>> getOrdersSortedByDate(
            @RequestParam(defaultValue = "asc") String sort) {
        List<OrderDto> orders = orderService.getOrdersSortedByDate(sort);
        return ResponseEntity.ok(orders);
    }

    @GetMapping(path = "/customer")
    public ResponseEntity<CustomerDto> getCustomer(@RequestParam String mobileNumber) {
        CustomerDto customerDto = orderService.getCustomerByMobile(mobileNumber);
        return ResponseEntity.ok(customerDto);
    }
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<OrderDto>> getOrdersByCustomerId(@PathVariable Long customerId) {
        List<OrderDto> orders = orderService.getOrdersByCustomerId(customerId);
        return ResponseEntity.ok(orders);
    }
    
    @PostMapping(path = "/orders")
    public ResponseEntity<OrderDto> createOrder(@Valid @RequestBody OrderDto orderDto) {
        OrderDto createdOrder = orderService.createOrder(orderDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
    }    
    
    @PutMapping("/orders/{orderId}")
    public ResponseEntity<OrderDto> updateOrder(@Valid @PathVariable Long orderId, @RequestBody OrderDto orderDto) {
        orderService.updateOrder(orderId, orderDto);
        OrderDto updatedOrder = orderService.getOrder(orderId);
        return ResponseEntity.ok(updatedOrder);
    }
    @PutMapping("/customer/{mobileNumber}")
    public ResponseEntity<Void> updateCustomer(@Valid @PathVariable String mobileNumber, @RequestBody CustomerDto customerDto) {
        orderService.updateCustomer(mobileNumber, customerDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/customer")
    public ResponseEntity<CustomerDto> createCustomer(@Valid @RequestBody CustomerDto customerDto) {
        Customer customer = orderService.createCustomer(customerDto);
        return ResponseEntity.ok(CustomerMapper.toDto(customer));
    }

    @DeleteMapping("/orders/{orderId}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long orderId) {
        orderService.deleteOrder(orderId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/customer/{mobileNumber}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable String mobileNumber) {
        boolean isDeleted = orderService.deleteCustomer(mobileNumber);
        return isDeleted ? ResponseEntity.ok().build() : ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).build();
}


}

