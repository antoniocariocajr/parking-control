package com.bill.parking_control.services.impl;

import com.bill.parking_control.dtos.ParkingSpotCreateDTO;
import com.bill.parking_control.dtos.ParkingSpotResponseDTO;
import com.bill.parking_control.persitenses.entities.ParkingSpot;
import com.bill.parking_control.persitenses.repositories.ParkingSpotRepository;
import com.bill.parking_control.services.ParkingSpotService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ParkingSpotServiceImpl implements ParkingSpotService {

    private final ParkingSpotRepository parkingSpotRepository;

    @Override
    public ParkingSpotResponseDTO createSpot(ParkingSpotCreateDTO dto) {
        if (parkingSpotRepository.findByCode(dto.code()).isPresent()) {
            throw new IllegalArgumentException("Spot code already in use");
        }

        ParkingSpot spot = ParkingSpot.builder()
                .code(dto.code())
                .type(dto.type())
                .status(ParkingSpot.SpotStatus.FREE)
                .covered(dto.covered())
                .build();

        spot = parkingSpotRepository.save(spot);
        return mapToDTO(spot);
    }

    @Override
    public List<ParkingSpotResponseDTO> getAllSpots() {
        return parkingSpotRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ParkingSpotResponseDTO getSpotById(String id) {
        return mapToDTO(getEntityById(id));
    }

    @Override
    public ParkingSpot getEntityById(String id) {
        return parkingSpotRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Spot not found"));
    }

    @Override
    public ParkingSpot getEntityByCode(String code) {
        return parkingSpotRepository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("Spot not found"));
    }

    private ParkingSpotResponseDTO mapToDTO(ParkingSpot spot) {
        return new ParkingSpotResponseDTO(
                spot.getId(),
                spot.getCode(),
                spot.getType(),
                spot.getStatus(),
                spot.isCovered());
    }
}
