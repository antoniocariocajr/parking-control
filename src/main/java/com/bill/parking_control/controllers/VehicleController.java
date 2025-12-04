package com.bill.parking_control.controllers;

import com.bill.parking_control.dtos.vehicle.VehicleCreateDTO;
import com.bill.parking_control.dtos.vehicle.VehicleResponseDTO;
import com.bill.parking_control.dtos.vehicle.VehicleUpdateDto;
import com.bill.parking_control.services.VehicleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/vehicles")
@RequiredArgsConstructor
public class VehicleController {

    private final VehicleService vehicleService;

    @PostMapping
    public ResponseEntity<VehicleResponseDTO> createVehicle(@RequestBody @Valid VehicleCreateDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(vehicleService.createVehicle(dto));
    }

    @GetMapping
    public ResponseEntity<Page<VehicleResponseDTO>> getAllVehicles(Pageable pageable) {
        return ResponseEntity.ok(vehicleService.getAllVehicles(pageable));
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<Page<VehicleResponseDTO>> getAllVehiclesByClientId(@PathVariable String clientId,
            Pageable pageable) {
        return ResponseEntity.ok(vehicleService.getAllVehiclesByClientId(clientId, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<VehicleResponseDTO> getVehicleById(@PathVariable String id) {
        return ResponseEntity.ok(vehicleService.getVehicleById(id));
    }

    @GetMapping("/license-plate/{licensePlate}")
    public ResponseEntity<VehicleResponseDTO> getVehicleByLicensePlate(@PathVariable String licensePlate) {
        return ResponseEntity.ok(vehicleService.getVehicleByLicensePlate(licensePlate));
    }

    @PutMapping("/{id}")
    public ResponseEntity<VehicleResponseDTO> updateVehicle(@PathVariable String id,
            @RequestBody @Valid VehicleUpdateDto dto) {
        return ResponseEntity.ok(vehicleService.updateVehicle(id, dto));
    }

    @PutMapping("/{id}/owner/{ownerId}")
    public ResponseEntity<VehicleResponseDTO> updateVehicleOwner(@PathVariable String id,
            @PathVariable String ownerId) {
        return ResponseEntity.ok(vehicleService.updateVehicleOwner(id, ownerId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVehicle(@PathVariable String id) {
        vehicleService.deleteVehicle(id);
        return ResponseEntity.ok().build();
    }
}
