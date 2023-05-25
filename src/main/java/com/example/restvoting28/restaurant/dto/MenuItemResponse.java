package com.example.restvoting28.restaurant.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter
@Builder
public class MenuItemResponse {
    private long id;
    private long dishId;
    private BigDecimal price;
}
