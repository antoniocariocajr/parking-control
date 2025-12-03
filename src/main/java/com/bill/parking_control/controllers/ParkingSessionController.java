package com.bill.parking_control.controllers;

import com.bill.parking_control.dtos.session.ParkingSessionResponseDTO;
import com.bill.parking_control.dtos.session.ParkingSessionStartDTO;
import com.bill.parking_control.persitenses.entities.User;
import com.bill.parking_control.services.ParkingSessionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sessions")
@RequiredArgsConstructor
public class ParkingSessionController {

    private final ParkingSessionService parkingSessionService;

    @PostMapping("/start")
    public ResponseEntity<ParkingSessionResponseDTO> startSession(
            @RequestBody @Valid ParkingSessionStartDTO dto,
            @AuthenticationPrincipal User operator) {
        return ResponseEntity.status(HttpStatus.CREATED).body(parkingSessionService.startSession(dto, operator));
    }

    @PostMapping("/{id}/end")
    public ResponseEntity<ParkingSessionResponseDTO> endSession(
            @PathVariable String id,
            @AuthenticationPrincipal User operator) {
        return ResponseEntity.ok(parkingSessionService.endSession(id, operator));
    }

    @GetMapping
    public ResponseEntity<List<ParkingSessionResponseDTO>> getAllSessions() {
        return ResponseEntity.ok(parkingSessionService.getAllSessions());
    }

    @GetMapping("/active")
    public ResponseEntity<List<ParkingSessionResponseDTO>> getActiveSessions() {
        return ResponseEntity.ok(parkingSessionService.getActiveSessions());
    }
}
