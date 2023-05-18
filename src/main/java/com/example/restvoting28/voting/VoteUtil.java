package com.example.restvoting28.voting;

import com.example.restvoting28.common.DateTimeService;
import com.example.restvoting28.common.exception.WrongTimeException;
import lombok.experimental.UtilityClass;

import java.time.LocalTime;

@UtilityClass
public class VoteUtil {
    private final LocalTime DEAD_LINE_TIME = LocalTime.of(11, 0);

    public static void checkCurrentTime(DateTimeService dateTimeService) {
        if (dateTimeService.timeNow().isAfter(DEAD_LINE_TIME)) {
            throw new WrongTimeException("Voting is impossible after " + DEAD_LINE_TIME);
        }
    }
}
