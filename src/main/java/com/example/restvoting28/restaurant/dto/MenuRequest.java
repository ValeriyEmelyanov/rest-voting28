package com.example.restvoting28.restaurant.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@Setter
@Getter
@ToString
public class MenuRequest {
    @NotNull
    private Long restaurantId;

    @NotNull
    private LocalDate date;

    @NotNull
    @NotEmpty
    List<MenuItemRequest> items;
}
