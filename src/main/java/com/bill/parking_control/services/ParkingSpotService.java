package com.bill.parking_control.services;

import com.bill.parking_control.dtos.spot.ParkingSpotCreateDTO;
import com.bill.parking_control.dtos.spot.ParkingSpotResponseDTO;
import com.bill.parking_control.dtos.spot.ParkingSpotUpdateDto;
import com.bill.parking_control.persitenses.entities.ParkingSpot.SpotStatus;
import com.bill.parking_control.persitenses.entities.Vehicle.VehicleType;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ParkingSpotService {
    ParkingSpotResponseDTO createSpot(ParkingSpotCreateDTO dto);

    Page<ParkingSpotResponseDTO> getAllSpots(Pageable pageable);

    Page<ParkingSpotResponseDTO> getAllSpotsByType(Pageable pageable, VehicleType type);

    Page<ParkingSpotResponseDTO> getAllSpotsByStatus(Pageable pageable, SpotStatus status);

    ParkingSpotResponseDTO getSpotById(String id);

    ParkingSpotResponseDTO getSpotByCode(String code);

    Optional<ParkingSpotResponseDTO> findFirstFreeByType(VehicleType type);

    ParkingSpotResponseDTO updateSpot(String id, ParkingSpotUpdateDto dto);

    ParkingSpotResponseDTO updateSpotStatus(String code, SpotStatus status);

    void deleteSpot(String id);
}
