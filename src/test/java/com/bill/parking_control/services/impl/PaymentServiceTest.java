package com.bill.parking_control.services.impl;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import com.bill.parking_control.dtos.payment.PaymentResponseDTO;
import com.bill.parking_control.persistences.entities.Payment;
import com.bill.parking_control.persistences.entities.Payment.PaymentStatus;
import com.bill.parking_control.persistences.repositories.PaymentRepository;
import com.bill.parking_control.services.mappers.PaymentMapper;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;
    @Mock
    private PaymentMapper paymentMapper;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    @Test
    void findById_Success() {
        String id = "payment-id";
        Payment payment = Payment.builder().id(id).build();
        PaymentResponseDTO responseDTO = new PaymentResponseDTO(id, "session-id", null, null, null, null, null);

        when(paymentRepository.findById(id)).thenReturn(Optional.of(payment));
        when(paymentMapper.toDTO(payment)).thenReturn(responseDTO);

        PaymentResponseDTO result = paymentService.findById(id);

        assertNotNull(result);
        verify(paymentRepository).findById(id);
    }

    @Test
    void findById_NotFound() {
        String id = "payment-id";

        when(paymentRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> paymentService.findById(id));
    }

    @Test
    void confirmPayment_Success() {
        String id = "payment-id";
        String transactionId = "trans-id";
        Payment payment = Payment.builder().id(id).status(PaymentStatus.PENDING).build();
        PaymentResponseDTO responseDTO = new PaymentResponseDTO(id, "session-id", null, null, PaymentStatus.PAID, null,
                transactionId);

        when(paymentRepository.findById(id)).thenReturn(Optional.of(payment));
        when(paymentRepository.save(payment)).thenReturn(payment);
        when(paymentMapper.toDTO(payment)).thenReturn(responseDTO);

        PaymentResponseDTO result = paymentService.confirmPayment(id, transactionId);

        assertNotNull(result);
        verify(paymentRepository).save(payment);
    }
}
