package com.bill.parking_control.controllers;

import com.bill.parking_control.dtos.vehicle.VehicleCreateDTO;
import com.bill.parking_control.dtos.vehicle.VehicleResponseDTO;
import com.bill.parking_control.dtos.vehicle.VehicleUpdateDto;
import com.bill.parking_control.services.VehicleService;

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
@RequestMapping("/vehicles")
@RequiredArgsConstructor
@Tag(name = "Vehicles", description = "Vehicles management")
@SecurityRequirement(name = "bearerAuth")
public class VehicleController {

    private final VehicleService vehicleService;

    @PostMapping
    @Operation(summary = "Create a new vehicle", description = "Create a new vehicle")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Vehicle created"),
            @ApiResponse(responseCode = "401", description = "Not authenticated"),
            @ApiResponse(responseCode = "404", description = "Vehicle not found")
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('SCOPE_OPERATOR')")
    public VehicleResponseDTO createVehicle(@RequestBody @Valid VehicleCreateDTO dto) {
        return vehicleService.createVehicle(dto);
    }

    @GetMapping
    @Operation(summary = "Get all vehicles", description = "Get all vehicles")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Vehicles found"),
            @ApiResponse(responseCode = "401", description = "Not authenticated"),
            @ApiResponse(responseCode = "404", description = "Vehicles not found")
    })
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('SCOPE_OPERATOR')")
    public Page<VehicleResponseDTO> getAllVehicles(Pageable pageable) {
        return vehicleService.getAllVehicles(pageable);
    }

    @GetMapping("/client/{clientId}")
    @Operation(summary = "Get all vehicles by client id", description = "Get all vehicles by client id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Vehicles found"),
            @ApiResponse(responseCode = "401", description = "Not authenticated"),
            @ApiResponse(responseCode = "404", description = "Vehicles not found")
    })
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('SCOPE_OPERATOR')")
    public Page<VehicleResponseDTO> getAllVehiclesByClientId(@PathVariable String clientId,
            Pageable pageable) {
        return vehicleService.getAllVehiclesByClientId(clientId, pageable);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get vehicle by id", description = "Get vehicle by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Vehicle found"),
            @ApiResponse(responseCode = "401", description = "Not authenticated"),
            @ApiResponse(responseCode = "404", description = "Vehicle not found")
    })
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('SCOPE_OPERATOR')")
    public VehicleResponseDTO getVehicleById(@PathVariable String id) {
        return vehicleService.getVehicleById(id);
    }

    @GetMapping("/license-plate/{licensePlate}")
    @Operation(summary = "Get vehicle by license plate", description = "Get vehicle by license plate")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Vehicle found"),
            @ApiResponse(responseCode = "401", description = "Not authenticated"),
            @ApiResponse(responseCode = "404", description = "Vehicle not found")
    })
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('SCOPE_OPERATOR')")
    public VehicleResponseDTO getVehicleByLicensePlate(@PathVariable String licensePlate) {
        return vehicleService.getVehicleByLicensePlate(licensePlate);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update vehicle", description = "Update vehicle")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Vehicle updated"),
            @ApiResponse(responseCode = "401", description = "Not authenticated"),
            @ApiResponse(responseCode = "404", description = "Vehicle not found")
    })
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('SCOPE_OPERATOR')")
    public VehicleResponseDTO updateVehicle(@PathVariable String id,
            @RequestBody @Valid VehicleUpdateDto dto) {
        return vehicleService.updateVehicle(id, dto);
    }

    @PutMapping("/{id}/owner/{ownerId}")
    @Operation(summary = "Update vehicle owner", description = "Update vehicle owner")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Vehicle owner updated"),
            @ApiResponse(responseCode = "401", description = "Not authenticated"),
            @ApiResponse(responseCode = "404", description = "Vehicle not found")
    })
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('SCOPE_OPERATOR')")
    public VehicleResponseDTO updateVehicleOwner(@PathVariable String id,
            @PathVariable String ownerId) {
        return vehicleService.updateVehicleOwner(id, ownerId);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete vehicle", description = "Delete vehicle")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Vehicle deleted"),
            @ApiResponse(responseCode = "401", description = "Not authenticated"),
            @ApiResponse(responseCode = "404", description = "Vehicle not found")
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public void deleteVehicle(@PathVariable String id) {
        vehicleService.deleteVehicle(id);
    }
}
