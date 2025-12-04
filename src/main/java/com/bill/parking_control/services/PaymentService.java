package com.bill.parking_control.services;

import java.time.Instant;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.bill.parking_control.dtos.payment.PaymentResponseDTO;
import com.bill.parking_control.persitenses.entities.Payment.PaymentMethod;
import com.bill.parking_control.persitenses.entities.Payment.PaymentStatus;

public interface PaymentService {

    PaymentResponseDTO findById(String id);

    Page<PaymentResponseDTO> findAll(Pageable pageable);

    Page<PaymentResponseDTO> findByPaymentStatus(Pageable pageable, PaymentStatus status);

    Page<PaymentResponseDTO> findPaidBetween(Instant startOfDay, Instant endOfDay, Pageable pageable);

    PaymentResponseDTO confirmPayment(String id, String transactionId);

    PaymentResponseDTO refundPayment(String id);

    PaymentResponseDTO updatePaymentMethod(String id, PaymentMethod method);

    void deletePayment(String id);
}
