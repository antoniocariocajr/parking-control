package com.bill.parking_control.controllers;

import com.bill.parking_control.dtos.payment.PaymentResponseDTO;
import com.bill.parking_control.dtos.session.ParkingSessionResponseDTO;
import com.bill.parking_control.dtos.session.ParkingSessionStartDTO;
import com.bill.parking_control.persitenses.entities.Payment.PaymentMethod;
import com.bill.parking_control.services.CheckoutService;
import com.bill.parking_control.services.ParkingSessionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sessions")
@RequiredArgsConstructor
public class ParkingSessionController {

    private final ParkingSessionService parkingSessionService;
    private final CheckoutService checkoutService;

    @PostMapping("/checkin")
    public ResponseEntity<ParkingSessionResponseDTO> startSession(
            @RequestBody @Valid ParkingSessionStartDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(parkingSessionService.startSession(dto));
    }

    @GetMapping
    public ResponseEntity<Page<ParkingSessionResponseDTO>> getAllSessions(@PageableDefault Pageable pageable) {
        return ResponseEntity.ok(parkingSessionService.getAllSessions(pageable));
    }

    @GetMapping("/active")
    public ResponseEntity<Page<ParkingSessionResponseDTO>> getActiveSessions(@PageableDefault Pageable pageable) {
        return ResponseEntity.ok(parkingSessionService.getActiveSessions(pageable));
    }

    @PostMapping("/{id}/checkout/{paymentMethod}")
    public ResponseEntity<PaymentResponseDTO> checkoutSession(
            @PathVariable String id,
            @PathVariable PaymentMethod paymentMethod) {
        return ResponseEntity.ok(checkoutService.checkout(id, paymentMethod));
    }
}
