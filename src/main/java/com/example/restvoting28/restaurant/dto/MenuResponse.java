package com.example.restvoting28.restaurant.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@Getter
@Builder
public class MenuResponse {
    private long restaurantId;
    private LocalDate date;
    private List<MenuItemResponse> items;
}
