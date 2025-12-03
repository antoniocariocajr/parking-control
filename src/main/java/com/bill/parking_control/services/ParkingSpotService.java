package com.bill.parking_control.services;

import com.bill.parking_control.dtos.ParkingSpotCreateDTO;
import com.bill.parking_control.dtos.ParkingSpotResponseDTO;
import com.bill.parking_control.persitenses.entities.ParkingSpot;

import java.util.List;

public interface ParkingSpotService {
    ParkingSpotResponseDTO createSpot(ParkingSpotCreateDTO dto);

    List<ParkingSpotResponseDTO> getAllSpots();

    ParkingSpotResponseDTO getSpotById(String id);

    ParkingSpot getEntityById(String id);

    ParkingSpot getEntityByCode(String code);
}
