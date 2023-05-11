package com.example.restvoting28.voting;

import com.example.restvoting28.common.BaseRepository;
import com.example.restvoting28.common.exception.NotFoundException;
import com.example.restvoting28.voting.model.Vote;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
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
    @Query("DELETE FROM Vote v WHERE v.userId=:userId AND v.date=:date")
    int delete(long userId, LocalDate date);

    default void deleteExisted(long userId, LocalDate date) {
        if (delete(userId, date) == 0) {
            throw new NotFoundException("Vote with userId=" + userId + " and date=" + date + " not found");
        }
    }

}
