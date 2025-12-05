package com.bill.parking_control.dtos.spot;

import com.bill.parking_control.persistences.entities.ParkingSpot.SpotStatus;
import com.bill.parking_control.persistences.entities.Vehicle.VehicleType;

public record ParkingSpotUpdateDto(
                String code,
                VehicleType vehicleType,
                SpotStatus spotStatus,
                boolean covered) {

}
