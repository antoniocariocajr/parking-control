package com.bill.parking_control.services.mappers;

import org.springframework.stereotype.Component;

import com.bill.parking_control.dtos.payment.PaymentResponseDTO;
import com.bill.parking_control.persistences.entities.Payment;

@Component
public class PaymentMapper {

    public PaymentResponseDTO toDTO(Payment entity) {
        return new PaymentResponseDTO(
                entity.getId(),
                entity.getSession() != null ? entity.getSession().getId() : null,
                entity.getAmount(),
                entity.getMethod(),
                entity.getStatus(),
                entity.getPaidAt(),
                entity.getTransactionId());
    }
}
