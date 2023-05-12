package com.example.restvoting28.voting;

import com.example.restvoting28.common.BaseRepository;
import com.example.restvoting28.common.exception.NotFoundException;
import com.example.restvoting28.voting.dto.GuestCountResponse;
import com.example.restvoting28.voting.dto.GuestResponse;
import com.example.restvoting28.voting.model.Vote;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface VoteRepository extends BaseRepository<Vote> {
    Optional<Vote> findByUserIdAndDate(long userId, LocalDate date);

    @Transactional
    default Vote prepareAndSave(Vote vote) {
        findByUserIdAndDate(vote.getUserId(), vote.getDate()).ifPresent(dbVote -> vote.setId(dbVote.getId()));
        return save(vote);
    }

    @Transactional
    @Modifying
    @Query("delete from Vote v where v.userId=:userId and v.date=:date")
    int delete(long userId, LocalDate date);

    default void deleteExisted(long userId, LocalDate date) {
        if (delete(userId, date) == 0) {
            throw new NotFoundException("Vote with userId=" + userId + " and date=" + date + " not found");
        }
    }

    @Query("select new com.example.restvoting28.voting.dto.GuestResponse(v.user.firstName, v.user.lastName, v.user.contact) from Vote v where v.restaurantId=:restaurantId and v.date=:date")
    List<GuestResponse> getGuestsByRestaurantIdAndDate(long restaurantId, LocalDate date);

    long countAllByRestaurantIdAndDate(long restaurant, LocalDate date);

    @Query("select new com.example.restvoting28.voting.dto.GuestCountResponse(v.restaurant.name, count(v.userId)) from Vote v where v.date=:date group by v.restaurant.name order by v.restaurant.name")
    List<GuestCountResponse> countByDate(LocalDate date);
}
