package com.bill.parking_control.dtos;

import com.bill.parking_control.persitenses.entities.Vehicle.VehicleType;

public record VehicleResponseDTO(
        String id,
        String licensePlate,
        String brand,
        String model,
        String color,
        VehicleType type,
        String ownerId) {
}
