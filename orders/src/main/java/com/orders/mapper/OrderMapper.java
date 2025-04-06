package com.orders.mapper;

import com.orders.controller.OrderController;
import com.orders.dto.CustomerDto;
import com.orders.dto.OrderDto;
import com.orders.entity.Order;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

public class OrderMapper {

    public static OrderDto mapToOrderDto(Order order) {
        OrderDto dto = new OrderDto();
        dto.setOrderId(order.getOrderId());
        dto.setOrderDate(order.getOrderDate());
        dto.setAmount(order.getAmount());
        dto.setProduct(order.getProduct());
        dto.setPrice(order.getPrice());
        dto.setUpdatedAt(order.getUpdatedAt());

        // Adding null check for order and customer
        if (order != null && order.getCustomer() != null) {
            dto.setCustomer(new CustomerDto(
                    order.getCustomer().getName(), 
                    order.getCustomer().getEmail(), 
                    order.getCustomer().getMobileNumber()));

            Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(OrderController.class)
                    .getOrder(order.getOrderId()))
                    .withSelfRel();

            Link allOrdersLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(OrderController.class)
                    .getAllOrders(null))
                    .withRel("all-orders");
            
            Link updateLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(OrderController.class)
                    .updateOrder(order.getOrderId(), null))
                    .withRel("update");

            Link deleteLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(OrderController.class)
                    .deleteOrder(order.getOrderId()))
                    .withRel("delete");
            
            dto.add(selfLink);
            dto.add(allOrdersLink);
            dto.add(updateLink);
            dto.add(deleteLink);
        } else {
            // Handling case when order or customer is null
            dto.setCustomer(new CustomerDto("Unknown", "Unknown", "Unknown"));
        }

        return dto;
    }

    public static Order mapToOrder(OrderDto dto) {
        Order order = new Order();
        order.setOrderId(dto.getOrderId());
        order.setOrderDate(dto.getOrderDate());
        order.setAmount(dto.getAmount());
        order.setProduct(dto.getProduct());
        order.setPrice(dto.getPrice());
        order.setUpdatedAt(dto.getUpdatedAt());
        return order;
    }
}
