package com.bill.parking_control.controllers;

import com.bill.parking_control.dtos.spot.ParkingSpotCreateDTO;
import com.bill.parking_control.dtos.spot.ParkingSpotResponseDTO;
import com.bill.parking_control.services.ParkingSpotService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/spots")
@RequiredArgsConstructor
public class ParkingSpotController {

    private final ParkingSpotService parkingSpotService;

    @PostMapping
    public ResponseEntity<ParkingSpotResponseDTO> createSpot(@RequestBody @Valid ParkingSpotCreateDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(parkingSpotService.createSpot(dto));
    }

    @GetMapping
    public ResponseEntity<List<ParkingSpotResponseDTO>> getAllSpots() {
        return ResponseEntity.ok(parkingSpotService.getAllSpots());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ParkingSpotResponseDTO> getSpotById(@PathVariable String id) {
        return ResponseEntity.ok(parkingSpotService.getSpotById(id));
    }
}
