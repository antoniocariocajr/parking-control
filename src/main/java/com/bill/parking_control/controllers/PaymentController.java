package com.bill.parking_control.controllers;

import java.time.Instant;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.bill.parking_control.dtos.payment.PaymentResponseDTO;
import com.bill.parking_control.persitenses.entities.Payment.PaymentMethod;
import com.bill.parking_control.persitenses.entities.Payment.PaymentStatus;
import com.bill.parking_control.services.PaymentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
@Tag(name = "Payments", description = "Payments management")
@SecurityRequirement(name = "bearerAuth")
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping("/{id}")
    @Operation(summary = "Get payment by id", description = "Get payment by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Payment found"),
            @ApiResponse(responseCode = "401", description = "Not authenticated")
    })
    @PreAuthorize("hasAuthority('SCOPE_OPERATOR')")
    @ResponseStatus(HttpStatus.OK)
    public PaymentResponseDTO findById(@PathVariable String id) {
        return paymentService.findById(id);
    }

    @GetMapping
    @Operation(summary = "Get all payments", description = "Get all payments")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Payments found"),
            @ApiResponse(responseCode = "401", description = "Not authenticated")
    })
    @PreAuthorize("hasAuthority('SCOPE_OPERATOR')")
    @ResponseStatus(HttpStatus.OK)
    public Page<PaymentResponseDTO> findAll(Pageable pageable) {
        return paymentService.findAll(pageable);
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get payments by status", description = "Get payments by status")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Payments found"),
            @ApiResponse(responseCode = "401", description = "Not authenticated")
    })
    @PreAuthorize("hasAuthority('SCOPE_OPERATOR')")
    @ResponseStatus(HttpStatus.OK)
    public Page<PaymentResponseDTO> findByPaymentStatus(Pageable pageable,
            @PathVariable PaymentStatus status) {
        return paymentService.findByPaymentStatus(pageable, status);
    }

    @GetMapping("/paid-between")
    @Operation(summary = "Get payments by paid between", description = "Get payments by paid between")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Payments found"),
            @ApiResponse(responseCode = "401", description = "Not authenticated")
    })
    @PreAuthorize("hasAuthority('SCOPE_OPERATOR')")
    @ResponseStatus(HttpStatus.OK)
    public Page<PaymentResponseDTO> findPaidBetween(Pageable pageable, @RequestParam Instant startOfDay,
            @RequestParam Instant endOfDay) {
        return paymentService.findPaidBetween(startOfDay, endOfDay, pageable);
    }

    @PostMapping("/{id}/confirm")
    @Operation(summary = "Confirm payment", description = "Confirm payment")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Payment confirmed"),
            @ApiResponse(responseCode = "401", description = "Not authenticated")
    })
    @PreAuthorize("hasAuthority('SCOPE_OPERATOR')")
    @ResponseStatus(HttpStatus.OK)
    public PaymentResponseDTO confirmPayment(@PathVariable String id,
            @RequestParam String transactionId) {
        return paymentService.confirmPayment(id, transactionId);
    }

    @PostMapping("/{id}/refund")
    @Operation(summary = "Refund payment", description = "Refund payment")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Payment refunded"),
            @ApiResponse(responseCode = "401", description = "Not authenticated")
    })
    @PreAuthorize("hasAuthority('SCOPE_OPERATOR')")
    @ResponseStatus(HttpStatus.OK)
    public PaymentResponseDTO refundPayment(@PathVariable String id) {
        return paymentService.refundPayment(id);
    }

    @PostMapping("/{id}/update-method")
    @Operation(summary = "Update payment method", description = "Update payment method")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Payment method updated"),
            @ApiResponse(responseCode = "401", description = "Not authenticated")
    })
    @PreAuthorize("hasAuthority('SCOPE_OPERATOR')")
    @ResponseStatus(HttpStatus.OK)
    public PaymentResponseDTO updatePaymentMethod(@PathVariable String id,
            @RequestParam PaymentMethod method) {
        return paymentService.updatePaymentMethod(id, method);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete payment", description = "Delete payment")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Payment deleted"),
            @ApiResponse(responseCode = "401", description = "Not authenticated")
    })
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePayment(@PathVariable String id) {
        paymentService.deletePayment(id);
    }
}
