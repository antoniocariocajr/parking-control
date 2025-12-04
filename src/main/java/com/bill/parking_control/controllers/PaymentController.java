package com.bill.parking_control.controllers;

import java.time.Instant;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bill.parking_control.dtos.payment.PaymentResponseDTO;
import com.bill.parking_control.persitenses.entities.Payment.PaymentMethod;
import com.bill.parking_control.persitenses.entities.Payment.PaymentStatus;
import com.bill.parking_control.services.PaymentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponseDTO> findById(@PathVariable String id) {
        return ResponseEntity.ok(paymentService.findById(id));
    }

    @GetMapping
    public ResponseEntity<Page<PaymentResponseDTO>> findAll(Pageable pageable) {
        return ResponseEntity.ok(paymentService.findAll(pageable));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<Page<PaymentResponseDTO>> findByPaymentStatus(Pageable pageable,
            @PathVariable PaymentStatus status) {
        return ResponseEntity.ok(paymentService.findByPaymentStatus(pageable, status));
    }

    @GetMapping("/paid-between")
    public ResponseEntity<Page<PaymentResponseDTO>> findPaidBetween(Pageable pageable, @RequestParam Instant startOfDay,
            @RequestParam Instant endOfDay) {
        return ResponseEntity.ok(paymentService.findPaidBetween(startOfDay, endOfDay, pageable));
    }

    @PostMapping("/{id}/confirm")
    public ResponseEntity<PaymentResponseDTO> confirmPayment(@PathVariable String id,
            @RequestParam String transactionId) {
        return ResponseEntity.ok(paymentService.confirmPayment(id, transactionId));
    }

    @PostMapping("/{id}/refund")
    public ResponseEntity<PaymentResponseDTO> refundPayment(@PathVariable String id) {
        return ResponseEntity.ok(paymentService.refundPayment(id));
    }

    @PostMapping("/{id}/update-method")
    public ResponseEntity<PaymentResponseDTO> updatePaymentMethod(@PathVariable String id,
            @RequestParam PaymentMethod method) {
        return ResponseEntity.ok(paymentService.updatePaymentMethod(id, method));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePayment(@PathVariable String id) {
        paymentService.deletePayment(id);
        return ResponseEntity.ok().build();
    }
}
