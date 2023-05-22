package com.example.restvoting28.restaurant;

import com.example.restvoting28.common.BaseRepository;
import com.example.restvoting28.common.exception.NotFoundException;
import com.example.restvoting28.restaurant.model.Menu;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface MenuRepository extends BaseRepository<Menu> {
    @Query("select m from Menu m where m.id=:id")
    Optional<Menu> get(long id);

    @Override
    default Menu getExisted(long id) {
        return get(id).orElseThrow(() -> new NotFoundException("Menu with id=" + id + " not found"));
    }

    @Query("select m from Menu m left join fetch m.items where m.id=:id")
    @Cacheable(value = "menu-with-items", key = "#id")
    Optional<Menu> getWithItems(long id);

    @Query("select m from Menu m where m.dated=:date")
    List<Menu> getAllByDate(LocalDate date);

    @Query("select m from Menu m where m.id=:id and m.restaurantId=:restaurantId")
    Optional<Menu> getBelonged(long id, long restaurantId);

    @Query("select m from Menu m where m.restaurantId=:restaurantId and m.dated=:date")
    @Cacheable(value = "menu", key = "{#restaurantId, #date}")
    Optional<Menu> getByRestaurantIdAndDate(long restaurantId, LocalDate date);

    @Query(nativeQuery = true, value = "select case when count(*) > 0 then true else false end from MENU m inner join MENU_ITEM i on m.restaurant_id=:restaurantId and m.dated=:date and m.id=i.menu_id limit 1")
    @Cacheable(value = "menu-existing", key = "{#restaurantId, #date}")
    boolean existsByRestaurantIdAndDated(long restaurantId, LocalDate date);
}
