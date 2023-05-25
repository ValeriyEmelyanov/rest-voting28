package com.example.restvoting28.voting.web;

import com.example.restvoting28.common.DateTimeService;
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
    public static final String URL = "/api";
    public static final String ADMIN_GUESTS_PATH = "/admin/report/guests";
    public static final String ADMIN_COUNTS_PATH = "/admin/report/counts";
    public static final String COUNTS_PATH = "/report/counts";

    private final VoteRepository repository;
    private final DateTimeService dateTimeService;

    @GetMapping(ADMIN_GUESTS_PATH + "/today")
    public List<GuestResponse> getGuestsByRestaurantId(@RequestParam long restaurantId) {
        log.info("Get guests by restaurantId={}", restaurantId);
        return repository.getGuestsByRestaurantIdAndDated(restaurantId, dateTimeService.dateNow());
    }

    @GetMapping(ADMIN_GUESTS_PATH)
    public List<GuestResponse> getGuestsByRestaurantIdAndDate(@RequestParam long restaurantId, @RequestParam LocalDate date) {
        log.info("Get guests by restaurantId={} and date={}", restaurantId, date);
        return repository.getGuestsByRestaurantIdAndDated(restaurantId, date);
    }

    @GetMapping(COUNTS_PATH + "/today")
    public long getGuestsCountByRestaurantId(@RequestParam long restaurantId) {
        log.info("Count guests by restaurantId={}", restaurantId);
        return repository.countAllByRestaurantIdAndDated(restaurantId, dateTimeService.dateNow());
    }

    @GetMapping(ADMIN_COUNTS_PATH)
    public long getGuestsCountByRestaurantIdAndDate(@RequestParam long restaurantId, @RequestParam LocalDate date) {
        log.info("Count guests by restaurantId={} and date={}", restaurantId, date);
        return repository.countAllByRestaurantIdAndDated(restaurantId, date);
    }

    @GetMapping(COUNTS_PATH + "/all/today")
    public List<GuestCountResponse> getGuestsCount() {
        log.info("Count guests");
        return repository.countByDate(dateTimeService.dateNow());
    }

    @GetMapping(ADMIN_COUNTS_PATH + "/all")
    public List<GuestCountResponse> getGuestsCountByDate(@RequestParam LocalDate date) {
        log.info("Count guests by date={}", date);
        return repository.countByDate(date);
    }
}
