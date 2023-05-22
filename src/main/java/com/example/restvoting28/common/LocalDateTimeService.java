package com.example.restvoting28.common;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
public class LocalDateTimeService implements DateTimeService {
    @Override
    public LocalDateTime now() {
        return LocalDateTime.now();
    }

    @Override
    public LocalDate dateNow() {
        return LocalDate.now();
    }

    @Override
    public LocalTime timeNow() {
        return LocalTime.now();
    }
}
