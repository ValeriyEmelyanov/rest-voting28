package com.example.restvoting28.restaurant.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter
public class MenuItemResponse {
    private String dish;
    private BigDecimal price;
}
