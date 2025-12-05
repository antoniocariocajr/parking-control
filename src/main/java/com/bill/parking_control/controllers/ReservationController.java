package com.bill.parking_control.controllers;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.bill.parking_control.dtos.reservation.ReservationCreateDTO;
import com.bill.parking_control.dtos.reservation.ReservationResponseDTO;
import com.bill.parking_control.persitenses.entities.Reservation.ReservationStatus;
import com.bill.parking_control.services.ReservationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/reservations")
@RequiredArgsConstructor
@Tag(name = "Reservations", description = "Reservations management")
@SecurityRequirement(name = "bearerAuth")
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping
    @Operation(summary = "Create a new reservation", description = "Create a new reservation")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Reservation created"),
            @ApiResponse(responseCode = "401", description = "Not authenticated"),
            @ApiResponse(responseCode = "404", description = "Reservation not found")
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('SCOPE_OPERATOR')")
    public ReservationResponseDTO create(@RequestBody @Valid ReservationCreateDTO dto) {
        return reservationService.create(dto);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get reservation by id", description = "Get reservation by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reservation found"),
            @ApiResponse(responseCode = "401", description = "Not authenticated"),
            @ApiResponse(responseCode = "404", description = "Reservation not found")
    })
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('SCOPE_OPERATOR')")
    public ReservationResponseDTO findById(@PathVariable String id) {
        return reservationService.findById(id);
    }

    @GetMapping
    @Operation(summary = "Get all reservations", description = "Get all reservations")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reservations found"),
            @ApiResponse(responseCode = "401", description = "Not authenticated"),
            @ApiResponse(responseCode = "404", description = "Reservations not found")
    })
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('SCOPE_OPERATOR')")
    public Page<ReservationResponseDTO> findAll(Pageable pageable) {
        return reservationService.findAll(pageable);
    }

    @GetMapping("/client/{clientId}")
    @Operation(summary = "Get all reservations by client", description = "Get all reservations by client")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reservations found"),
            @ApiResponse(responseCode = "401", description = "Not authenticated"),
            @ApiResponse(responseCode = "404", description = "Reservations not found")
    })
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('SCOPE_OPERATOR')")
    public Page<ReservationResponseDTO> findAllByClient(Pageable pageable,
            @PathVariable String clientId) {
        return reservationService.findAllByClient(pageable, clientId);
    }

    @GetMapping("/spot/{spotId}")
    @Operation(summary = "Get all reservations by spot", description = "Get all reservations by spot")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reservations found"),
            @ApiResponse(responseCode = "401", description = "Not authenticated"),
            @ApiResponse(responseCode = "404", description = "Reservations not found")
    })
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('SCOPE_OPERATOR')")
    public Page<ReservationResponseDTO> findAllBySpot(Pageable pageable, @PathVariable String spotId) {
        return reservationService.findAllBySpot(pageable, spotId);
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get all reservations by status", description = "Get all reservations by status")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reservations found"),
            @ApiResponse(responseCode = "401", description = "Not authenticated"),
            @ApiResponse(responseCode = "404", description = "Reservations not found")
    })
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('SCOPE_OPERATOR')")
    public Page<ReservationResponseDTO> findAllByStatus(Pageable pageable,
            @PathVariable String status) {
        return reservationService.findAllByStatus(pageable, ReservationStatus.valueOf(status));
    }

    @GetMapping("/from/{from}/to/{to}")
    @Operation(summary = "Get all reservations by reserved from between", description = "Get all reservations by reserved from between")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reservations found"),
            @ApiResponse(responseCode = "401", description = "Not authenticated"),
            @ApiResponse(responseCode = "404", description = "Reservations not found")
    })
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('SCOPE_OPERATOR')")
    public Page<ReservationResponseDTO> findAllByReservedFromBetween(Pageable pageable,
            @PathVariable LocalDateTime from, @PathVariable LocalDateTime to) {
        return reservationService.findAllByReservedFromBetween(pageable, from, to);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Cancel reservation", description = "Cancel reservation")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Reservation cancelled"),
            @ApiResponse(responseCode = "401", description = "Not authenticated"),
            @ApiResponse(responseCode = "404", description = "Reservation not found")
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('SCOPE_OPERATOR')")
    public void cancelReservation(@PathVariable String id) {
        reservationService.cancelReservation(id);
    }
}
