package com.orders.mapper;

import com.orders.controller.OrderController;
import com.orders.dto.CustomerDto;
import com.orders.dto.OrderDto;
import com.orders.entity.Order;
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

        dto.setCustomer(mapCustomer(order));

        addOrderLinks(dto, order.getOrderId());

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

    private static CustomerDto mapCustomer(Order order) {
        String unknown = "Unkown";
        if (order.getCustomer() == null) {
            return new CustomerDto(unknown, unknown, unknown);
        }
        return new CustomerDto(
            order.getCustomer().getName(),
            order.getCustomer().getEmail(),
            order.getCustomer().getMobileNumber()
        );
    }

    private static void addOrderLinks(OrderDto dto, Long orderId) {
        dto.add(WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(OrderController.class).getOrder(orderId))
                .withSelfRel());

        dto.add(WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(OrderController.class).getAllOrders(null))
                .withRel("all-orders"));

        dto.add(WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(OrderController.class).updateOrder(orderId, null))
                .withRel("update"));

        dto.add(WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(OrderController.class).deleteOrder(orderId))
                .withRel("delete"));
    }
}
