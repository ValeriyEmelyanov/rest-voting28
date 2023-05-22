package com.example.restvoting28.restaurant;

import com.example.restvoting28.common.BaseRepository;
import com.example.restvoting28.restaurant.model.Dish;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface DishRepository extends BaseRepository<Dish> {
    @Query("select d from Dish d where d.id=:id and d.restaurantId=:restaurantId")
    Optional<Dish> getBelonged(long id, long restaurantId);

    List<Dish> findAllByRestaurantIdOrderByName(long restaurantId);
}
