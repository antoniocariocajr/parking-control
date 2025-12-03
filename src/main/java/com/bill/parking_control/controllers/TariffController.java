package com.bill.parking_control.controllers;

import com.bill.parking_control.dtos.TariffCreateDTO;
import com.bill.parking_control.dtos.TariffResponseDTO;
import com.bill.parking_control.services.TariffService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<List<TariffResponseDTO>> getAllTariffs() {
        return ResponseEntity.ok(tariffService.getAllTariffs());
    }
}
