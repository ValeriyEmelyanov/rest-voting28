package com.example.restvoting28.voting.web;

import com.example.restvoting28.common.DateTimeService;
import com.example.restvoting28.common.exception.IllegalRequestDataException;
import com.example.restvoting28.common.exception.NotFoundException;
import com.example.restvoting28.security.AuthUser;
import com.example.restvoting28.voting.VoteRepository;
import com.example.restvoting28.voting.dto.VoteRequest;
import com.example.restvoting28.voting.model.Vote;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static com.example.restvoting28.voting.VoteUtil.checkCurrentTime;

@RestController
@RequestMapping(VoteController.URL)
@RequiredArgsConstructor
@Slf4j
public class VoteController {
    public static final String URL = "/api/vote";

    private final VoteRepository repository;
    private final DateTimeService dateTimeService;

    @GetMapping("/today")
    public Vote getToday(@AuthenticationPrincipal AuthUser authUser) {
        log.info("Get vote by userId={} today", authUser.id());
        LocalDate now = dateTimeService.dateNow();
        return repository.findByUserIdAndDated(authUser.id(), now)
                .orElseThrow(() -> new NotFoundException("Vote by userId=" + authUser.id() + " on date=" + now + " not found"));
    }

    @GetMapping
    public Vote get(@RequestParam LocalDate date, @AuthenticationPrincipal AuthUser authUser) {
        log.info("Get vote by userId={} on date={}", authUser.id(), date);
        return repository.findByUserIdAndDated(authUser.id(), date)
                .orElseThrow(() -> new NotFoundException("Vote by userId=" + authUser.id() + " on date=" + date + " not found"));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public Vote create(@Valid @RequestBody VoteRequest request, @AuthenticationPrincipal AuthUser authUser) {
        log.info("Vote userId={}", authUser.id());
        LocalDate dateNow = dateTimeService.dateNow();
        repository.findByUserIdAndDated(authUser.id(), dateNow)
                .ifPresent(v -> {
                    throw new IllegalRequestDataException("User id=" + v.getUserId() + " voted on date=" + v.getDated() + " already");
                });
        return repository.save(new Vote(authUser.id(), request.getRestaurantId(), dateNow));
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public Vote update(@Valid @RequestBody VoteRequest request, @AuthenticationPrincipal AuthUser authUser) {
        log.info("Change vote userId={}", authUser.id());
        LocalDateTime now = dateTimeService.now();
        checkCurrentTime(now.toLocalTime());
        Vote dbVote = repository.findByUserIdAndDated(authUser.id(), now.toLocalDate())
                .orElseThrow(() -> new IllegalRequestDataException("User id=" + authUser.id() + " don't voted on date=" + now.toLocalDate()));
        dbVote.setRestaurantId(request.getRestaurantId());
        return repository.save(dbVote);
    }
}
