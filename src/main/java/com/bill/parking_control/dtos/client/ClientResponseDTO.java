package com.bill.parking_control.dtos.client;

import java.util.List;

import com.bill.parking_control.dtos.vehicle.VehicleResponseDTO;

public record ClientResponseDTO(
        String id,
        String name,
        String cpf,
        String email,
        String phone,
        List<VehicleResponseDTO> vehicles) {
}
