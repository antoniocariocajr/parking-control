package com.bill.parking_control.controllers;

import com.bill.parking_control.dtos.spot.ParkingSpotCreateDTO;
import com.bill.parking_control.dtos.spot.ParkingSpotResponseDTO;
import com.bill.parking_control.dtos.spot.ParkingSpotUpdateDto;
import com.bill.parking_control.persitenses.entities.ParkingSpot.SpotStatus;
import com.bill.parking_control.persitenses.entities.Vehicle.VehicleType;
import com.bill.parking_control.services.ParkingSpotService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;

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
    public ResponseEntity<Page<ParkingSpotResponseDTO>> getAllSpots(@PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(parkingSpotService.getAllSpots(pageable));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<Page<ParkingSpotResponseDTO>> searchSpots(@PageableDefault(size = 10) Pageable pageable,
            @PathVariable SpotStatus status) {
        return ResponseEntity.ok(parkingSpotService.getAllSpotsByStatus(pageable, status));
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<Page<ParkingSpotResponseDTO>> searchSpotsByType(@PageableDefault(size = 10) Pageable pageable,
            @PathVariable VehicleType type) {
        return ResponseEntity.ok(parkingSpotService.getAllSpotsByType(pageable, type));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ParkingSpotResponseDTO> getSpotById(@PathVariable String id) {
        return ResponseEntity.ok(parkingSpotService.getSpotById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ParkingSpotResponseDTO> updateSpot(@PathVariable String id,
            @RequestBody @Valid ParkingSpotUpdateDto dto) {
        return ResponseEntity.ok(parkingSpotService.updateSpot(id, dto));
    }

    @PutMapping("/{id}/status/{status}")
    public ResponseEntity<ParkingSpotResponseDTO> updateSpotStatus(@PathVariable String id,
            @PathVariable SpotStatus status) {
        return ResponseEntity.ok(parkingSpotService.updateSpotStatus(id, status));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSpot(@PathVariable String id) {
        parkingSpotService.deleteSpot(id);
        return ResponseEntity.noContent().build();
    }
}
