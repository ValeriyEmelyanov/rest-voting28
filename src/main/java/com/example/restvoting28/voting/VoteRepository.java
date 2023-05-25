package com.example.restvoting28.voting;

import com.example.restvoting28.common.BaseRepository;
import com.example.restvoting28.voting.dto.GuestCountResponse;
import com.example.restvoting28.voting.dto.GuestResponse;
import com.example.restvoting28.voting.model.Vote;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface VoteRepository extends BaseRepository<Vote> {
    Optional<Vote> findByUserIdAndDated(long userId, LocalDate dated);

    @Query("select new com.example.restvoting28.voting.dto.GuestResponse(v.user.firstName, v.user.lastName, v.user.contact) from Vote v where v.restaurantId=:restaurantId and v.dated=:dated")
    List<GuestResponse> getGuestsByRestaurantIdAndDated(long restaurantId, LocalDate dated);

    @Cacheable(value = "votes-restaurant-counts", key = "{#restaurantId, #dated}")
    /* The "votes-restaurant-counts" cache is not invalidated, the cache is updated after the expiration of the cache lifetime,
    the accuracy of counts at a specific time is not important. */
    long countAllByRestaurantIdAndDated(long restaurantId, LocalDate dated);

    @Query("select new com.example.restvoting28.voting.dto.GuestCountResponse(v.restaurantId, v.restaurant.name, count(v.userId)) from Vote v where v.dated=:dated group by v.restaurant.name order by v.restaurant.name")
    /* The "votes-counts" cache is not invalidated, the cache is updated after the expiration of the cache lifetime,
    the accuracy of counts at a specific time is not important. */
    @Cacheable(value = "votes-counts", key = "#dated")
    List<GuestCountResponse> countByDate(LocalDate dated);
}
