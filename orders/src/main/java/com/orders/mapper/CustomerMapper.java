package com.orders.mapper;

import com.orders.dto.CustomerDto;
import com.orders.entity.Customer;

public class CustomerMapper {

    public static CustomerDto toDto(Customer customer) {
        if (customer == null) {
            return null;
        }
        return new CustomerDto(customer.getName(), customer.getEmail(), customer.getMobileNumber());
    }

    public static Customer toEntity(CustomerDto dto) {
        if (dto == null) {
            return null;
        }
        Customer customer = new Customer();
        customer.setName(dto.getName());
        customer.setEmail(dto.getEmail());
        customer.setMobileNumber(dto.getMobileNumber());
        return customer;
    }
}
