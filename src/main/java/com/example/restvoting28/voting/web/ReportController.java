package com.example.restvoting28.voting.web;

import com.example.restvoting28.voting.VoteRepository;
import com.example.restvoting28.voting.dto.GuestCountResponse;
import com.example.restvoting28.voting.dto.GuestResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(ReportController.URL)
@RequiredArgsConstructor
@Slf4j
public class ReportController {
    public static final String URL = "/api/report";

    private final VoteRepository repository;

    @GetMapping("/restaurants/{restaurantId}")
    public List<GuestResponse> getGuestsByRestaurantId(@PathVariable long restaurantId) {
        log.info("Get guests by restaurantId={}", restaurantId);
        return repository.getGuestsByRestaurantIdAndDate(restaurantId, LocalDate.now());
    }

    @GetMapping("/restaurants/{restaurantId}/date/{date}")
    public List<GuestResponse> getGuestsByRestaurantIdAndDate(@PathVariable long restaurantId, @PathVariable LocalDate date) {
        log.info("Get guests by restaurantId={} and date={}", restaurantId, date);
        return repository.getGuestsByRestaurantIdAndDate(restaurantId, date);
    }

    @GetMapping("/restaurants/{restaurantId}/count")
    public long getGuestsCountByRestaurantId(@PathVariable long restaurantId) {
        log.info("Count guests by restaurantId={}", restaurantId);
        return repository.countAllByRestaurantIdAndDate(restaurantId, LocalDate.now());
    }

    @GetMapping("/restaurants/{restaurantId}/count/date/{date}")
    public long getGuestsCountByRestaurantIdAndDate(@PathVariable long restaurantId, @PathVariable LocalDate date) {
        log.info("Count guests by restaurantId={} and date={}", restaurantId, date);
        return repository.countAllByRestaurantIdAndDate(restaurantId, date);
    }

    @GetMapping("/restaurants/count")
    public List<GuestCountResponse> getGuestsCount() {
        log.info("Count guests");
        return repository.countByDate(LocalDate.now());
    }

    @GetMapping("/restaurants/count/date/{date}")
    public List<GuestCountResponse> getGuestsCountByDate(@PathVariable LocalDate date) {
        log.info("Count guests by date={}", date);
        return repository.countByDate(date);
    }
}
