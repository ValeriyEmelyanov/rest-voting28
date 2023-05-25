package com.example.restvoting28.restaurant.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@NoArgsConstructor
@Setter
@Getter
@ToString
public class MenuItemRequest {
    @NotNull
    private Long dishId;

    @Digits(integer = 10, fraction = 2)
    @NotNull
    private BigDecimal price;
}
