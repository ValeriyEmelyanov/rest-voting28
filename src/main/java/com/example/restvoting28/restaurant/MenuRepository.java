package com.example.restvoting28.restaurant;

import com.example.restvoting28.common.BaseRepository;
import com.example.restvoting28.restaurant.model.Menu;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Transactional(readOnly = true)
public interface MenuRepository extends BaseRepository<Menu> {
    List<Menu> findAllByDate(LocalDate date);

    List<Menu> findAllByRestaurantIdAndDate(long restaurantId, LocalDate date);
}
