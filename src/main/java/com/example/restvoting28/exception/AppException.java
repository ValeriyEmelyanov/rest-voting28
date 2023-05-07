package com.example.restvoting28.exception;

import org.springframework.lang.NonNull;

public class AppException extends RuntimeException {

    public AppException(@NonNull String message) {
        super(message);
    }
}
