package com.bill.parking_control.dtos.spot;

import com.bill.parking_control.persitenses.entities.ParkingSpot.SpotStatus;
import com.bill.parking_control.persitenses.entities.Vehicle.VehicleType;

public record ParkingSpotResponseDTO(
        String id,
        String code,
        VehicleType type,
        SpotStatus status,
        boolean covered) {
}
