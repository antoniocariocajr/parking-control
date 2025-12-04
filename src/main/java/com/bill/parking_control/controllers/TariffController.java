package com.bill.parking_control.controllers;

import com.bill.parking_control.dtos.tariff.TariffCreateDTO;
import com.bill.parking_control.dtos.tariff.TariffResponseDTO;
import com.bill.parking_control.dtos.tariff.TariffUpdateDto;
import com.bill.parking_control.persitenses.entities.Vehicle.VehicleType;
import com.bill.parking_control.services.TariffService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tariffs")
@RequiredArgsConstructor
public class TariffController {

    private final TariffService tariffService;

    @PostMapping
    public ResponseEntity<TariffResponseDTO> createTariff(@RequestBody @Valid TariffCreateDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(tariffService.createTariff(dto));
    }

    @GetMapping
    public ResponseEntity<Page<TariffResponseDTO>> getAllTariffs(Pageable pageable) {
        return ResponseEntity.ok(tariffService.getAllTariffs(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TariffResponseDTO> getTariffById(@PathVariable String id) {
        return ResponseEntity.ok(tariffService.getTariffById(id));
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<TariffResponseDTO> getTariffByVehicleType(@PathVariable VehicleType type) {
        return ResponseEntity.ok(tariffService.getTariffByVehicleType(type));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TariffResponseDTO> updateTariff(@PathVariable String id,
            @RequestBody @Valid TariffUpdateDto dto) {
        return ResponseEntity.ok(tariffService.updateTariff(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTariff(@PathVariable String id) {
        tariffService.deleteTariff(id);
        return ResponseEntity.noContent().build();
    }
}
