package com.bill.parking_control.services.impl;

import java.time.Instant;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.bill.parking_control.dtos.payment.PaymentResponseDTO;
import com.bill.parking_control.persitenses.entities.Payment;
import com.bill.parking_control.persitenses.entities.Payment.PaymentMethod;
import com.bill.parking_control.persitenses.entities.Payment.PaymentStatus;
import com.bill.parking_control.persitenses.repositories.PaymentRepository;
import com.bill.parking_control.services.PaymentService;
import com.bill.parking_control.services.mappers.PaymentMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;

    @Override
    public PaymentResponseDTO findById(String id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Payment not found"));
        return paymentMapper.toDTO(payment);
    }

    @Override
    public Page<PaymentResponseDTO> findAll(Pageable pageable) {
        return paymentRepository.findAll(pageable).map(paymentMapper::toDTO);
    }

    @Override
    public Page<PaymentResponseDTO> findByPaymentStatus(Pageable pageable, PaymentStatus status) {
        return paymentRepository.findByPaymentStatus(pageable, status).map(paymentMapper::toDTO);
    }

    @Override
    public Page<PaymentResponseDTO> findPaidBetween(Instant startOfDay, Instant endOfDay, Pageable pageable) {
        return paymentRepository.findPaidBetween(startOfDay, endOfDay, pageable).map(paymentMapper::toDTO);
    }

    @Override
    public PaymentResponseDTO confirmPayment(String id, String transactionId) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Payment not found"));

        payment.setStatus(Payment.PaymentStatus.PAID);
        payment.setPaidAt(Instant.now());
        payment.setTransactionId(transactionId);
        payment = paymentRepository.save(payment);
        return paymentMapper.toDTO(payment);
    }

    @Override
    public PaymentResponseDTO refundPayment(String id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Payment not found"));

        payment.setStatus(Payment.PaymentStatus.REFUNDED);
        payment = paymentRepository.save(payment);
        return paymentMapper.toDTO(payment);
    }

    @Override
    public PaymentResponseDTO updatePaymentMethod(String id, PaymentMethod method) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Payment not found"));

        payment.setMethod(method);
        payment = paymentRepository.save(payment);
        return paymentMapper.toDTO(payment);
    }

    @Override
    public void deletePayment(String id) {
        paymentRepository.deleteById(id);
    }
}
