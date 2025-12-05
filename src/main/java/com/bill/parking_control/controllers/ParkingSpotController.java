package com.bill.parking_control.controllers;

import com.bill.parking_control.dtos.spot.ParkingSpotCreateDTO;
import com.bill.parking_control.dtos.spot.ParkingSpotResponseDTO;
import com.bill.parking_control.dtos.spot.ParkingSpotUpdateDto;
import com.bill.parking_control.persistences.entities.ParkingSpot.SpotStatus;
import com.bill.parking_control.persistences.entities.Vehicle.VehicleType;
import com.bill.parking_control.services.ParkingSpotService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/spots")
@RequiredArgsConstructor
@Tag(name = "Parking Spots", description = "Parking Spots management")
@SecurityRequirement(name = "bearerAuth")
public class ParkingSpotController {

        private final ParkingSpotService parkingSpotService;

        @PostMapping
        @Operation(summary = "Create a new parking spot", description = "Create a new parking spot")
        @ApiResponses({
                        @ApiResponse(responseCode = "201", description = "Parking spot created"),
                        @ApiResponse(responseCode = "401", description = "Not authenticated")
        })
        @PreAuthorize("hasAuthority('SCOPE_OPERATOR')")
        @ResponseStatus(HttpStatus.CREATED)
        public ParkingSpotResponseDTO createSpot(@RequestBody @Valid ParkingSpotCreateDTO dto) {
                return parkingSpotService.createSpot(dto);
        }

        @GetMapping
        @Operation(summary = "Get all parking spots", description = "Get all parking spots")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Parking spots found"),
                        @ApiResponse(responseCode = "401", description = "Not authenticated")
        })
        @PreAuthorize("hasAuthority('SCOPE_OPERATOR')")
        @ResponseStatus(HttpStatus.OK)
        public Page<ParkingSpotResponseDTO> getAllSpots(@PageableDefault(size = 10) Pageable pageable) {
                return parkingSpotService.getAllSpots(pageable);
        }

        @GetMapping("/status/{status}")
        @Operation(summary = "Get all parking spots by status", description = "Get all parking spots by status")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Parking spots found"),
                        @ApiResponse(responseCode = "401", description = "Not authenticated")
        })
        @PreAuthorize("hasAuthority('SCOPE_OPERATOR')")
        @ResponseStatus(HttpStatus.OK)
        public Page<ParkingSpotResponseDTO> searchSpots(@PageableDefault(size = 10) Pageable pageable,
                        @PathVariable SpotStatus status) {
                return parkingSpotService.getAllSpotsByStatus(pageable, status);
        }

        @GetMapping("/type/{type}")
        @Operation(summary = "Get all parking spots by type", description = "Get all parking spots by type")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Parking spots found"),
                        @ApiResponse(responseCode = "401", description = "Not authenticated")
        })
        @PreAuthorize("hasAuthority('SCOPE_OPERATOR')")
        @ResponseStatus(HttpStatus.OK)
        public Page<ParkingSpotResponseDTO> searchSpotsByType(@PageableDefault(size = 10) Pageable pageable,
                        @PathVariable VehicleType type) {
                return parkingSpotService.getAllSpotsByType(pageable, type);
        }

        @GetMapping("/{id}")
        @Operation(summary = "Get parking spot by id", description = "Get parking spot by id")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Parking spot found"),
                        @ApiResponse(responseCode = "401", description = "Not authenticated")
        })
        @PreAuthorize("hasAuthority('SCOPE_OPERATOR')")
        @ResponseStatus(HttpStatus.OK)
        public ParkingSpotResponseDTO getSpotById(@PathVariable String id) {
                return parkingSpotService.getSpotById(id);
        }

        @PutMapping("/{id}")
        @Operation(summary = "Update parking spot", description = "Update parking spot")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Parking spot updated"),
                        @ApiResponse(responseCode = "401", description = "Not authenticated")
        })
        @PreAuthorize("hasAuthority('SCOPE_OPERATOR')")
        @ResponseStatus(HttpStatus.OK)
        public ParkingSpotResponseDTO updateSpot(@PathVariable String id,
                        @RequestBody @Valid ParkingSpotUpdateDto dto) {
                return parkingSpotService.updateSpot(id, dto);
        }

        @PutMapping("/{id}/status/{status}")
        @Operation(summary = "Update parking spot status", description = "Update parking spot status")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Parking spot status updated"),
                        @ApiResponse(responseCode = "401", description = "Not authenticated")
        })
        @PreAuthorize("hasAuthority('SCOPE_OPERATOR')")
        @ResponseStatus(HttpStatus.OK)
        public ParkingSpotResponseDTO updateSpotStatus(@PathVariable String id,
                        @PathVariable SpotStatus status) {
                return parkingSpotService.updateSpotStatus(id, status);
        }

        @DeleteMapping("/{id}")
        @Operation(summary = "Delete parking spot", description = "Delete parking spot")
        @ApiResponses({
                        @ApiResponse(responseCode = "204", description = "Parking spot deleted"),
                        @ApiResponse(responseCode = "401", description = "Not authenticated")
        })
        @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
        @ResponseStatus(HttpStatus.NO_CONTENT)
        public void deleteSpot(@PathVariable String id) {
                parkingSpotService.deleteSpot(id);
        }
}
