package com.orders.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.orders.dto.CustomerDto;
import com.orders.dto.OrderDto;
import com.orders.entity.Customer;

public interface IOrderService {
    void createOrder(OrderDto orderDto);
    OrderDto getOrder(Long orderId);
    CustomerDto getCustomerByMobile(String mobileNumber);
	boolean isDeleted(String mobileNumber);
	void deleteOrder(Long orderId);
	void updateOrder(Long orderId, OrderDto orderDto);
	void updateCustomer(String mobileNumber, CustomerDto customerDto);
	boolean deleteCustomer(String mobileNumber);
    Page<OrderDto> getAllOrders(Pageable pageable);
	List<OrderDto> getOrdersByCustomerId(Long customerId);
    Page<OrderDto> getAllCusOrdersByCustomerId(Long customerId, Pageable pageable);
	List<OrderDto> getOrdersWithinDateRange(LocalDate startDate, LocalDate endDate);
	List<OrderDto> getOrdersSortedByDate(String sort);
	Customer createCustomer(CustomerDto customerDto);
}