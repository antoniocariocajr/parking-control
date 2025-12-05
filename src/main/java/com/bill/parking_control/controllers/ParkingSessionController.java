package com.bill.parking_control.controllers;

import com.bill.parking_control.dtos.payment.PaymentResponseDTO;
import com.bill.parking_control.dtos.session.ParkingSessionResponseDTO;
import com.bill.parking_control.dtos.session.ParkingSessionStartDTO;
import com.bill.parking_control.persitenses.entities.ParkingSession.SessionStatus;
import com.bill.parking_control.persitenses.entities.Payment.PaymentMethod;
import com.bill.parking_control.services.CheckoutService;
import com.bill.parking_control.services.ParkingSessionService;

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
@RequestMapping("/sessions")
@RequiredArgsConstructor
@Tag(name = "Parking Sessions", description = "Parking Sessions management")
@SecurityRequirement(name = "bearerAuth")
public class ParkingSessionController {

    private final ParkingSessionService parkingSessionService;
    private final CheckoutService checkoutService;

    @PostMapping("/checkin")
    @Operation(summary = "Start a parking session", description = "Start a parking session in the parking lot")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Parking session started"),
            @ApiResponse(responseCode = "401", description = "Not authenticated"),
            @ApiResponse(responseCode = "404", description = "Entity not found"),
            @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('SCOPE_OPERATOR')")
    public ParkingSessionResponseDTO startSession(
            @RequestBody @Valid ParkingSessionStartDTO dto) {
        return parkingSessionService.startSession(dto);
    }

    @GetMapping
    @Operation(summary = "Get all parking sessions", description = "Get all parking sessions")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Parking sessions found"),
            @ApiResponse(responseCode = "401", description = "Not authenticated")
    })
    @PreAuthorize("hasAuthority('SCOPE_OPERATOR')")
    @ResponseStatus(HttpStatus.OK)
    public Page<ParkingSessionResponseDTO> getAllSessions(@PageableDefault Pageable pageable) {
        return parkingSessionService.getAllSessions(pageable);
    }

    @GetMapping("/{status}")
    @Operation(summary = "Get all active parking sessions", description = "Get all active parking sessions")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Parking sessions found"),
            @ApiResponse(responseCode = "401", description = "Not authenticated")
    })
    @PreAuthorize("hasAuthority('SCOPE_OPERATOR')")
    @ResponseStatus(HttpStatus.OK)
    public Page<ParkingSessionResponseDTO> getActiveSessions(@PageableDefault Pageable pageable,
            @PathVariable SessionStatus status) {
        return parkingSessionService.getActiveSessions(pageable, status);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get parking session by id", description = "Get parking session by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Parking session found"),
            @ApiResponse(responseCode = "401", description = "Not authenticated")
    })
    @PreAuthorize("hasAuthority('SCOPE_OPERATOR')")
    @ResponseStatus(HttpStatus.OK)
    public ParkingSessionResponseDTO getSessionById(@PathVariable String id) {
        return parkingSessionService.getSessionById(id);
    }

    @PostMapping("/{id}/checkout/{paymentMethod}")
    @Operation(summary = "Checkout a parking session", description = "Checkout a parking session")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Parking session checked out"),
            @ApiResponse(responseCode = "401", description = "Not authenticated")
    })
    @PreAuthorize("hasAuthority('SCOPE_OPERATOR')")
    @ResponseStatus(HttpStatus.OK)
    public PaymentResponseDTO checkoutSession(
            @PathVariable String id,
            @PathVariable PaymentMethod paymentMethod) {
        return checkoutService.checkout(id, paymentMethod);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Cancel a parking session", description = "Cancel a parking session")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Parking session cancelled"),
            @ApiResponse(responseCode = "401", description = "Not authenticated")
    })
    @PreAuthorize("hasAuthority('SCOPE_OPERATOR')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancelSession(@PathVariable String id) {
        parkingSessionService.cancelSession(id);
    }
}
