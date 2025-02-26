package com.orders.service.impl;

import com.orders.dto.CustomerDto;
import com.orders.dto.OrderDto;
import com.orders.entity.Customer;
import com.orders.entity.Order;
import com.orders.exception.ResourceNotFoundException;
import com.orders.mapper.CustomerMapper;
import com.orders.mapper.OrderMapper;
import com.orders.repository.CustomerRepository;
import com.orders.repository.OrderRepository;
import com.orders.service.IOrderService;
import lombok.AllArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class OrderServiceImpl implements IOrderService {

    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;

    //Orders 
    @Override
    @Transactional
    public void createOrder(OrderDto orderDto) {
        // Find or create customer
        Customer customer = customerRepository.findByMobileNumber(orderDto.getCustomer().getMobileNumber())
                .orElseGet(() -> {
                    Customer newCustomer = new Customer();
                    newCustomer.setName(orderDto.getCustomer().getName());
                    newCustomer.setEmail(orderDto.getCustomer().getEmail());
                    newCustomer.setMobileNumber(orderDto.getCustomer().getMobileNumber());
                    return customerRepository.save(newCustomer);
                });

        // Ensure customer ID is available after persistence
        if (customer.getCustomerId() == null) {
            throw new RuntimeException("Failed to persist customer and retrieve ID.");
        }

        // Create order and link to customer
        Order order = new Order();
        order.setOrderDate(LocalDate.now());
        order.setAmount(orderDto.getAmount());
        order.setProduct(orderDto.getProduct());
        order.setPrice(orderDto.getPrice());
        order.setCustomer(customer);

        // Save order
        orderRepository.save(order);
    }

    @Override
    public OrderDto getOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "ID", orderId.toString()));
        return OrderMapper.mapToOrderDto(order);
    }

    @Override
    public Page<OrderDto> getAllOrders(Pageable pageable) {
        return orderRepository.findAll(pageable)
                .map(OrderMapper::mapToOrderDto);
}
    @Override
    public List<OrderDto> getOrdersByCustomerId(Long customerId) {
        List<Order> orders = orderRepository.findByCustomerCustomerId(customerId);
        return orders.stream().map(OrderMapper::mapToOrderDto).collect(Collectors.toList());
    }

    public List<OrderDto> getOrdersWithinDateRange(LocalDate startDate, LocalDate endDate) {
        List<Order> orders = orderRepository.findByOrderDateBetween(startDate, endDate);
        return orders.stream().map(OrderMapper::mapToOrderDto).collect(Collectors.toList());
    }

    public List<OrderDto> getOrdersSortedByDate(String sortDirection) {
        List<Order> orders;
        if ("desc".equalsIgnoreCase(sortDirection)) {
            orders = orderRepository.findAllByOrderByOrderDateDesc();
        } else {
            orders = orderRepository.findAllByOrderByOrderDateAsc();
        }
        return orders.stream().map(OrderMapper::mapToOrderDto).collect(Collectors.toList());
    }
    
    @Override
    @Transactional
    public void updateOrder(Long orderId, OrderDto orderDto) {
        Order existingOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "ID", orderId.toString()));

        existingOrder.setAmount(orderDto.getAmount());
        existingOrder.setProduct(orderDto.getProduct());
        existingOrder.setPrice(orderDto.getPrice());
        existingOrder.setUpdatedAt(LocalDateTime.now());

        orderRepository.save(existingOrder);
    }

    @Override
    @Transactional
    public void deleteOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "ID", orderId.toString()));
        orderRepository.delete(order);
    }
    
    
    //Customers
    @Override
    public CustomerDto getCustomerByMobile(String mobileNumber) {
        Customer customer = customerRepository.findByMobileNumber(mobileNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "Mobile Number", mobileNumber));
        return new CustomerDto(customer.getName(), customer.getEmail(), customer.getMobileNumber());
    }
    @Override
    public Page<OrderDto> getAllCusOrdersByCustomerId(Long customerId, Pageable pageable) {
        Page<Order> orders = orderRepository.findByCustomerCustomerId(customerId, pageable);
        return orders.map(OrderMapper::mapToOrderDto);
    }
    @Override
    @Transactional
    public void updateCustomer(String mobileNumber, CustomerDto customerDto) {
        Customer customer = customerRepository.findByMobileNumber(mobileNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "Mobile Number", mobileNumber));

        customer.setName(customerDto.getName());
        customer.setEmail(customerDto.getEmail());
        customerRepository.save(customer);
    }

    @Override
    @Transactional
    public boolean deleteCustomer(String mobileNumber) {
        Customer customer = customerRepository.findByMobileNumber(mobileNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "Mobile Number", mobileNumber));
        customerRepository.delete(customer);
        return true;
    }


    public Customer createCustomer(CustomerDto customerDto) {
        Customer customer = CustomerMapper.toEntity(customerDto);
        return customerRepository.save(customer);
    }
    
	@Override
	public boolean isDeleted(String mobileNumber) {
		// TODO Auto-generated method stub
		return false;
	}

	
}

