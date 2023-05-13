package com.example.restvoting28.restaurant;

import com.example.restvoting28.common.BaseRepository;
import com.example.restvoting28.restaurant.model.Menu;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
@CacheConfig(cacheNames = {"menu","menu-existing"})
public interface MenuRepository extends BaseRepository<Menu> {
    List<Menu> findAllByDate(LocalDate date);

    @Cacheable(value = "menu", key = "{#restaurantId, #date}")
    Optional<Menu> findByRestaurantIdAndDate(long restaurantId, LocalDate date);

    @Cacheable(value = "menu-existing", key = "{#restaurantId, #date}")
    boolean existsByRestaurantIdAndDate(long restaurantId, LocalDate date);
}
