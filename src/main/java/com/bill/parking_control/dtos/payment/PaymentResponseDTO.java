package com.bill.parking_control.dtos.payment;

import java.math.BigDecimal;
import java.time.Instant;

import com.bill.parking_control.persistences.entities.Payment.PaymentMethod;
import com.bill.parking_control.persistences.entities.Payment.PaymentStatus;

public record PaymentResponseDTO(
                String id,
                String sessionId,
                BigDecimal amount,
                PaymentMethod method,
                PaymentStatus status,
                Instant paidAt,
                String transactionId) {
}
