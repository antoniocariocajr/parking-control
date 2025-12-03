package com.bill.parking_control.dtos;

import java.util.List;

public record ClientResponseDTO(
        String id,
        String name,
        String cpf,
        String email,
        String phone,
        List<VehicleResponseDTO> vehicles) {
}
