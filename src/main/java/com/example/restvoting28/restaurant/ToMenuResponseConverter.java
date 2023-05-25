package com.example.restvoting28.restaurant;

import com.example.restvoting28.restaurant.dto.MenuItemResponse;
import com.example.restvoting28.restaurant.dto.MenuResponse;
import com.example.restvoting28.restaurant.model.MenuItem;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ToMenuResponseConverter implements Converter<List<MenuItem>, MenuResponse> {
    @Override
    public MenuResponse convert(List<MenuItem> sourceItems) {
        if (sourceItems == null || sourceItems.isEmpty()) {
            return null;
        }
        List<MenuItemResponse> items = sourceItems.stream()
                .map(item -> MenuItemResponse.builder()
                        .id(item.id())
                        .dishId(item.getDishId())
                        .price(item.getPrice())
                        .build())
                .toList();
        MenuItem sourceItem = sourceItems.get(0);
        return MenuResponse.builder()
                .restaurantId(sourceItem.getRestaurantId())
                .date(sourceItem.getDated())
                .items(items)
                .build();
    }
}
