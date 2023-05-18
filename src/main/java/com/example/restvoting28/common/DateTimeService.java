package com.example.restvoting28.common;

import java.time.LocalDate;
import java.time.LocalTime;

public interface DateTimeService {
    LocalDate dateNow();
    LocalTime timeNow();
}
