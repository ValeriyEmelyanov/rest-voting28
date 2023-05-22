package com.example.restvoting28.common;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public interface DateTimeService {
    LocalDateTime now();
    LocalDate dateNow();
    LocalTime timeNow();
}
