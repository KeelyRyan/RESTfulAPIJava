package com.orders.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDto {
    private String status;
    private String message;
}