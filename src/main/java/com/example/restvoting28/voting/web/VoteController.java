package com.example.restvoting28.voting.web;

import com.example.restvoting28.common.DateTimeService;
import com.example.restvoting28.common.exception.IllegalRequestDataException;
import com.example.restvoting28.common.exception.NotFoundException;
import com.example.restvoting28.restaurant.MenuRepository;
import com.example.restvoting28.security.AuthUser;
import com.example.restvoting28.voting.VoteRepository;
import com.example.restvoting28.voting.dto.VoteRequest;
import com.example.restvoting28.voting.model.Vote;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

import static com.example.restvoting28.voting.VoteUtil.checkCurrentTime;

@RestController
@RequestMapping(VoteController.URL)
@RequiredArgsConstructor
@Slf4j
public class VoteController {
    public static final String URL = "/api/votes";

    private final VoteRepository repository;
    private final MenuRepository menuRepository;
    private final DateTimeService dateTimeService;

    @GetMapping
    public Vote get(@AuthenticationPrincipal AuthUser authUser) {
        log.info("Get vote by userId={}", authUser.id());
        LocalDate now = dateTimeService.dateNow();
        return repository.findByUserIdAndDated(authUser.id(), now)
                .orElseThrow(() -> new NotFoundException("No vote by userId=" + authUser.id() + " and date=" + now));
    }

    @GetMapping("/date/{date}")
    public Vote getByDate(@PathVariable LocalDate date, @AuthenticationPrincipal AuthUser authUser) {
        log.info("Get vote by userId={} and date={}", authUser.id(), date);
        return repository.findByUserIdAndDated(authUser.id(), date)
                .orElseThrow(() -> new NotFoundException("No vote by userId=" + authUser.id() + " and date=" + date));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public Vote vote(@Valid @RequestBody VoteRequest request, @AuthenticationPrincipal AuthUser authUser) {
        log.info("Vote userId={}", authUser.id());
        checkCurrentTime(dateTimeService);
        LocalDate date = dateTimeService.dateNow();
        if (!menuRepository.existsByRestaurantIdAndDated(request.getRestaurantId(), date)) {
            throw new IllegalRequestDataException("No menu for restaurantId=" + request.getRestaurantId() + " today");
        }
        return repository.prepareAndSave(new Vote(authUser.id(), request.getRestaurantId(), date));
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@AuthenticationPrincipal AuthUser authUser) {
        log.info("Delete vote by userId={}", authUser.id());
        checkCurrentTime(dateTimeService);
        repository.deleteExisted(authUser.id(), dateTimeService.dateNow());
    }
}
