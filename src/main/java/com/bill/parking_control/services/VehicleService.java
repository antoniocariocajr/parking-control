package com.bill.parking_control.services;

import com.bill.parking_control.dtos.VehicleCreateDTO;
import com.bill.parking_control.dtos.VehicleResponseDTO;
import com.bill.parking_control.persitenses.entities.Vehicle;

import java.util.List;

public interface VehicleService {
    VehicleResponseDTO createVehicle(VehicleCreateDTO dto);

    List<VehicleResponseDTO> getAllVehicles();

    VehicleResponseDTO getVehicleById(String id);

    Vehicle getEntityById(String id);

    Vehicle getEntityByLicensePlate(String licensePlate);
}
