package com.example.restvoting28.restaurant.web;

import com.example.restvoting28.restaurant.model.Restaurant;
import lombok.experimental.UtilityClass;

@UtilityClass
public class RestaurantTestData {
    public static final long RESTAURANT_CRAZY_COOK_ID = 1L;
    public static String RESTAURANT_CRAZY_COOK_NAME = "Crazy Cook";

    public static Restaurant getNew() {
        return new Restaurant(null, "New restaurant");
    }

    public static Restaurant getNewWithHtml() {
        return new Restaurant(null, "<h1>New restaurant</h1>");
    }

    public static Restaurant getUpdated() {
        return new Restaurant(1L, "Updated Cook");
    }
}
