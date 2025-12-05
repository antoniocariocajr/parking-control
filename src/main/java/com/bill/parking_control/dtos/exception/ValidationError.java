package com.bill.parking_control.dtos.exception;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public record ValidationError(
        Instant timestamp,
        Integer status,
        String error,
        String message,
        String path,
        List<FieldMessage> errors) {
    public ValidationError(Instant timestamp, Integer status, String error, String message, String path) {
        this(timestamp, status, error, message, path, new ArrayList<>());
    }

    public void addError(String fieldName, String message) {
        errors.add(new FieldMessage(fieldName, message));
    }
}
