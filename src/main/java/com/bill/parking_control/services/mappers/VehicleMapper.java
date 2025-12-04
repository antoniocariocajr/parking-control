package com.bill.parking_control.services.mappers;

import org.springframework.stereotype.Component;

import com.bill.parking_control.dtos.vehicle.VehicleResponseDTO;
import com.bill.parking_control.persitenses.entities.Vehicle;

@Component
public class VehicleMapper {

    public VehicleResponseDTO toVehicleResponseDTO(Vehicle vehicle) {
        return new VehicleResponseDTO(
                vehicle.getId(),
                vehicle.getLicensePlate(),
                vehicle.getBrand(),
                vehicle.getModel(),
                vehicle.getColor(),
                vehicle.getType(),
                vehicle.getOwner().getId());
    }

}
