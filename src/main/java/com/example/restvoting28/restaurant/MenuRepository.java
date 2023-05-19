package com.example.restvoting28.restaurant;

import com.example.restvoting28.common.BaseRepository;
import com.example.restvoting28.restaurant.model.Menu;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface MenuRepository extends BaseRepository<Menu> {
    List<Menu> findAllByDated(LocalDate dated);

    @Cacheable(value = "menu", key = "{#restaurantId, #dated}")
    Optional<Menu> findByRestaurantIdAndDated(long restaurantId, LocalDate dated);

    @Cacheable(value = "menu-existing", key = "{#restaurantId, #dated}")
    boolean existsByRestaurantIdAndDated(long restaurantId, LocalDate dated);
}
