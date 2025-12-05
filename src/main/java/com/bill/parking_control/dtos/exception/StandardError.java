package com.bill.parking_control.dtos.exception;

import java.time.Instant;

public record StandardError(
        Instant timestamp,
        Integer status,
        String error,
        String message,
        String path) {
}
