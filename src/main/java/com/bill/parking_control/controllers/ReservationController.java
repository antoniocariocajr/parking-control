package com.bill.parking_control.controllers;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bill.parking_control.dtos.reservation.ReservationCreateDTO;
import com.bill.parking_control.dtos.reservation.ReservationResponseDTO;
import com.bill.parking_control.persitenses.entities.Reservation.ReservationStatus;
import com.bill.parking_control.services.ReservationService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping
    public ResponseEntity<ReservationResponseDTO> create(@RequestBody @Valid ReservationCreateDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reservationService.create(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservationResponseDTO> findById(@PathVariable String id) {
        return ResponseEntity.ok(reservationService.findById(id));
    }

    @GetMapping
    public ResponseEntity<Page<ReservationResponseDTO>> findAll(Pageable pageable) {
        return ResponseEntity.ok(reservationService.findAll(pageable));
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<Page<ReservationResponseDTO>> findAllByClient(Pageable pageable,
            @PathVariable String clientId) {
        return ResponseEntity.ok(reservationService.findAllByClient(pageable, clientId));
    }

    @GetMapping("/spot/{spotId}")
    public ResponseEntity<Page<ReservationResponseDTO>> findAllBySpot(Pageable pageable, @PathVariable String spotId) {
        return ResponseEntity.ok(reservationService.findAllBySpot(pageable, spotId));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<Page<ReservationResponseDTO>> findAllByStatus(Pageable pageable,
            @PathVariable String status) {
        return ResponseEntity
                .ok(reservationService.findAllByStatus(pageable, ReservationStatus.valueOf(status)));
    }

    @GetMapping("/from/{from}/to/{to}")
    public ResponseEntity<Page<ReservationResponseDTO>> findAllByReservedFromBetween(Pageable pageable,
            @PathVariable LocalDateTime from, @PathVariable LocalDateTime to) {
        return ResponseEntity.ok(reservationService.findAllByReservedFromBetween(pageable, from, to));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelReservation(@PathVariable String id) {
        reservationService.cancelReservation(id);
        return ResponseEntity.noContent().build();
    }
}
