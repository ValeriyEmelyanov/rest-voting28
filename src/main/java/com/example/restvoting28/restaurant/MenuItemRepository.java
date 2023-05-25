package com.example.restvoting28.restaurant;

import com.example.restvoting28.common.BaseRepository;
import com.example.restvoting28.common.exception.IllegalRequestDataException;
import com.example.restvoting28.common.exception.NotFoundException;
import com.example.restvoting28.restaurant.model.MenuItem;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface MenuItemRepository extends BaseRepository<MenuItem> {
    @Query("select mi from MenuItem mi where mi.dated=:date")
    List<MenuItem> getAllByDate(LocalDate date);

    @Query("select mi from MenuItem mi where mi.id=:id and mi.restaurantId=:restaurantId")
    Optional<MenuItem> getBelonged(long id, long restaurantId);

    @Query("select mi from MenuItem mi where mi.restaurantId=:restaurantId and mi.dated=:date")
    List<MenuItem> getAllByRestaurantIdAndDate(long restaurantId, LocalDate date);

    @Query(nativeQuery = true, value = "select case when count(*) > 0 then true else false end from MENU_ITEM mi where mi.restaurant_id=:restaurantId and mi.dated=:date limit 1")
    boolean existsByRestaurantIdAndDated(long restaurantId, LocalDate date);

    default void checkExists(long restaurantId, LocalDate date) {
        if (!existsByRestaurantIdAndDated(restaurantId, date)) {
            throw new IllegalRequestDataException("No menu for restaurantId=" + restaurantId + " on date=" + date);
        }
    }

    @Transactional
    @Modifying
    @Query("delete from MenuItem mi where mi.restaurantId=:restaurantId and mi.dated=:date")
    int deleteAllByRestaurantIdAndDate(long restaurantId, LocalDate date);

    default void deleteAllByRestaurantIdAndDateExisted(long restaurantId, LocalDate date) {
        if (deleteAllByRestaurantIdAndDate(restaurantId, date) == 0) {
            throw new NotFoundException("Menu by restaurantId=" + restaurantId + " on date=" + date + " not found");
        }
    }

}
