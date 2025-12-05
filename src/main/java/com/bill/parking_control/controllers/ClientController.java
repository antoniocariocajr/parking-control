package com.bill.parking_control.controllers;

import com.bill.parking_control.dtos.client.ClientCreateDTO;
import com.bill.parking_control.dtos.client.ClientResponseDTO;
import com.bill.parking_control.dtos.client.ClientUpdateDto;
import com.bill.parking_control.services.ClientService;

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
@RequestMapping("/clients")
@RequiredArgsConstructor
@Tag(name = "Clients", description = "Clients management")
@SecurityRequirement(name = "bearerAuth")
public class ClientController {

    private final ClientService clientService;

    @PostMapping
    @Operation(summary = "Create a new client", description = "Create a new client")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Client created"),
            @ApiResponse(responseCode = "401", description = "Not authenticated"),
            @ApiResponse(responseCode = "404", description = "Client not found")
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('SCOPE_OPERATOR')")
    public ClientResponseDTO createClient(@RequestBody @Valid ClientCreateDTO dto) {
        return clientService.createClient(dto);
    }

    @GetMapping
    @Operation(summary = "Get all clients", description = "Get all clients")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Clients found"),
            @ApiResponse(responseCode = "401", description = "Not authenticated"),
            @ApiResponse(responseCode = "404", description = "Clients not found")
    })
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('SCOPE_OPERATOR')")
    public Page<ClientResponseDTO> getAllClients(Pageable pageable) {
        return clientService.getAllClients(pageable);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get client by id", description = "Get client by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Client found"),
            @ApiResponse(responseCode = "401", description = "Not authenticated"),
            @ApiResponse(responseCode = "404", description = "Client not found")
    })
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('SCOPE_OPERATOR')")
    public ClientResponseDTO getClientById(@PathVariable String id) {
        return clientService.getClientById(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update client", description = "Update client")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Client updated"),
            @ApiResponse(responseCode = "401", description = "Not authenticated"),
            @ApiResponse(responseCode = "404", description = "Client not found")
    })
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('SCOPE_OPERATOR')")
    public ClientResponseDTO updateClient(@PathVariable String id,
            @RequestBody @Valid ClientUpdateDto dto) {
        return clientService.updateClient(id, dto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete client", description = "Delete client")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Client deleted"),
            @ApiResponse(responseCode = "401", description = "Not authenticated"),
            @ApiResponse(responseCode = "404", description = "Client not found")
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public void deleteClient(@PathVariable String id) {
        clientService.deleteClient(id);
    }

    @PatchMapping("/{id}/active")
    @Operation(summary = "Update client active status", description = "Update client active status")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Client active status updated"),
            @ApiResponse(responseCode = "401", description = "Not authenticated"),
            @ApiResponse(responseCode = "404", description = "Client not found")
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public void updateClientActiveStatus(@PathVariable String id,
            @RequestBody @Valid boolean isActive) {
        clientService.updateClientActiveStatus(id, isActive);
    }
}
