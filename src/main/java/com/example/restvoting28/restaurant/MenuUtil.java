package com.example.restvoting28.restaurant;

import com.example.restvoting28.restaurant.model.Menu;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class MenuUtil {
    public static Menu prepareToSave(Menu menu) {
        if (menu.getItems() == null) {
            menu.setItems(List.of());
        } else {
            menu.getItems().forEach(i -> i.setMenu(menu));
        }
        return menu;
    }
}
