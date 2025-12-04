package com.bill.parking_control.services;

import com.bill.parking_control.dtos.vehicle.VehicleCreateDTO;
import com.bill.parking_control.dtos.vehicle.VehicleResponseDTO;
import com.bill.parking_control.dtos.vehicle.VehicleUpdateDto;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface VehicleService {
    VehicleResponseDTO createVehicle(VehicleCreateDTO dto);

    Page<VehicleResponseDTO> getAllVehicles(Pageable pageable);

    Page<VehicleResponseDTO> getAllVehiclesByClientId(String clientId, Pageable pageable);

    VehicleResponseDTO getVehicleById(String id);

    VehicleResponseDTO getVehicleByLicensePlate(String licensePlate);

    VehicleResponseDTO updateVehicle(String id, VehicleUpdateDto dto);

    VehicleResponseDTO updateVehicleOwner(String id, String ownerId);

    void deleteVehicle(String id);
}
