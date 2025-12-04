package com.bill.parking_control.dtos.spot;

import com.bill.parking_control.persitenses.entities.ParkingSpot.SpotStatus;
import com.bill.parking_control.persitenses.entities.Vehicle.VehicleType;

public record ParkingSpotUpdateDto(
        String code,
        VehicleType vehicleType,
        SpotStatus spotStatus,
        boolean covered) {

}
