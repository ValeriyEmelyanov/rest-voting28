package com.example.restvoting28.common;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;

@Service
public class DateTimeServiceImpl implements DateTimeService {
    @Override
    public LocalDate dateNow() {
        return LocalDate.now();
    }

    @Override
    public LocalTime timeNow() {
        return LocalTime.now();
    }
}
