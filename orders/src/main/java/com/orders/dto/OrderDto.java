package com.orders.dto;

import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.springframework.hateoas.RepresentationModel;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto extends RepresentationModel<OrderDto> {
    private Long orderId;
    private LocalDate orderDate;
    @Min(value = 1, message = "Amount must be at least 1.")
    private Long amount;
    @NotBlank(message = "Product name is required.")
    private String product;
    @NotNull(message = "Price cannot be null.")
    private Double price;
    private CustomerDto customer;
	public LocalDateTime updatedAt;
}
