package com.bill.parking_control.services.mappers;

import org.springframework.stereotype.Component;

import com.bill.parking_control.dtos.vehicle.VehicleCreateDTO;
import com.bill.parking_control.dtos.vehicle.VehicleResponseDTO;
import com.bill.parking_control.dtos.vehicle.VehicleUpdateDto;
import com.bill.parking_control.persistences.entities.Client;
import com.bill.parking_control.persistences.entities.Vehicle;

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

    public Vehicle toEntity(VehicleCreateDTO vehicleCreateDTO, Client owner) {
        return Vehicle.builder()
                .licensePlate(vehicleCreateDTO.licensePlate())
                .brand(vehicleCreateDTO.brand())
                .model(vehicleCreateDTO.model())
                .color(vehicleCreateDTO.color())
                .type(vehicleCreateDTO.type())
                .owner(owner)
                .build();
    }

    public void updateVehicle(Vehicle vehicle, VehicleUpdateDto vehicleUpdateDto) {
        if (vehicleUpdateDto.licensePlate() != null) {
            vehicle.setLicensePlate(vehicleUpdateDto.licensePlate());
        }
        if (vehicleUpdateDto.brand() != null) {
            vehicle.setBrand(vehicleUpdateDto.brand());
        }
        if (vehicleUpdateDto.model() != null) {
            vehicle.setModel(vehicleUpdateDto.model());
        }
        if (vehicleUpdateDto.color() != null) {
            vehicle.setColor(vehicleUpdateDto.color());
        }
        if (vehicleUpdateDto.type() != null) {
            vehicle.setType(vehicleUpdateDto.type());
        }
    }
}
