package com.example.restvoting28.restaurant;

import com.example.restvoting28.common.BaseRepository;
import com.example.restvoting28.restaurant.model.Dish;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface DishRepository extends BaseRepository<Dish> {
    List<Dish> findAllByRestaurantIdOrderByName(long restaurantId);
}
