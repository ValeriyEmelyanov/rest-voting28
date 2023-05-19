package com.example.restvoting28.restaurant;

import com.example.restvoting28.restaurant.dto.MenuItemResponse;
import com.example.restvoting28.restaurant.dto.MenuResponse;
import com.example.restvoting28.restaurant.model.Menu;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ToMenuResponseConverter implements Converter<Menu, MenuResponse> {
    @Override
    public MenuResponse convert(Menu menu) {
        List<MenuItemResponse> responseItems = null;
        if (menu.getItems() != null) {
            responseItems = menu.getItems().stream()
                    .map(i -> new MenuItemResponse(i.getDish() != null ? i.getDish().getName() : "", i.getPrice()))
                    .collect(Collectors.toList());
        }
        return MenuResponse.builder()
                .id(menu.id())
                .restaurant(menu.getRestaurant())
                .dated(menu.getDated())
                .items(responseItems)
                .build();
    }
}
