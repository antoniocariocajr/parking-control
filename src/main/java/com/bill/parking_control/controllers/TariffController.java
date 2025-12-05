package com.bill.parking_control.controllers;

import com.bill.parking_control.dtos.tariff.TariffCreateDTO;
import com.bill.parking_control.dtos.tariff.TariffResponseDTO;
import com.bill.parking_control.dtos.tariff.TariffUpdateDto;
import com.bill.parking_control.persistences.entities.Vehicle.VehicleType;
import com.bill.parking_control.services.TariffService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tariffs")
@RequiredArgsConstructor
@Tag(name = "Tariffs", description = "Tariffs management")
@SecurityRequirement(name = "bearerAuth")
public class TariffController {

    private final TariffService tariffService;

    @PostMapping
    @Operation(summary = "Create a new tariff", description = "Create a new tariff")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Tariff created"),
            @ApiResponse(responseCode = "401", description = "Not authenticated"),
            @ApiResponse(responseCode = "404", description = "Tariff not found")
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public TariffResponseDTO createTariff(@RequestBody @Valid TariffCreateDTO dto) {
        return tariffService.createTariff(dto);
    }

    @GetMapping
    @Operation(summary = "Get all tariffs", description = "Get all tariffs")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tariffs found"),
            @ApiResponse(responseCode = "401", description = "Not authenticated"),
            @ApiResponse(responseCode = "404", description = "Tariffs not found")
    })
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('SCOPE_OPERATOR')")
    public Page<TariffResponseDTO> getAllTariffs(Pageable pageable) {
        return tariffService.getAllTariffs(pageable);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get tariff by id", description = "Get tariff by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tariff found"),
            @ApiResponse(responseCode = "401", description = "Not authenticated"),
            @ApiResponse(responseCode = "404", description = "Tariff not found")
    })
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('SCOPE_OPERATOR')")
    public TariffResponseDTO getTariffById(@PathVariable String id) {
        return tariffService.getTariffById(id);
    }

    @GetMapping("/type/{type}")
    @Operation(summary = "Get tariff by vehicle type", description = "Get tariff by vehicle type")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tariff found"),
            @ApiResponse(responseCode = "401", description = "Not authenticated"),
            @ApiResponse(responseCode = "404", description = "Tariff not found")
    })
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('SCOPE_OPERATOR')")
    public TariffResponseDTO getTariffByVehicleType(@PathVariable VehicleType type) {
        return tariffService.getTariffByVehicleType(type);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update tariff", description = "Update tariff")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tariff updated"),
            @ApiResponse(responseCode = "401", description = "Not authenticated"),
            @ApiResponse(responseCode = "404", description = "Tariff not found")
    })
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public TariffResponseDTO updateTariff(@PathVariable String id,
            @RequestBody @Valid TariffUpdateDto dto) {
        return tariffService.updateTariff(id, dto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete tariff", description = "Delete tariff")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Tariff deleted"),
            @ApiResponse(responseCode = "401", description = "Not authenticated"),
            @ApiResponse(responseCode = "404", description = "Tariff not found")
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public void deleteTariff(@PathVariable String id) {
        tariffService.deleteTariff(id);
    }
}
