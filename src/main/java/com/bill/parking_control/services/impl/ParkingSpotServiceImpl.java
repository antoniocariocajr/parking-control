package com.bill.parking_control.services.impl;

import com.bill.parking_control.dtos.spot.ParkingSpotCreateDTO;
import com.bill.parking_control.dtos.spot.ParkingSpotResponseDTO;
import com.bill.parking_control.dtos.spot.ParkingSpotUpdateDto;
import com.bill.parking_control.persistences.entities.ParkingSpot;
import com.bill.parking_control.persistences.entities.ParkingSpot.SpotStatus;
import com.bill.parking_control.persistences.entities.Vehicle.VehicleType;
import com.bill.parking_control.persistences.repositories.ParkingSpotRepository;
import com.bill.parking_control.services.ParkingSpotService;
import com.bill.parking_control.services.mappers.ParkingSpotMapper;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ParkingSpotServiceImpl implements ParkingSpotService {

    private final ParkingSpotRepository parkingSpotRepository;
    private final ParkingSpotMapper parkingSpotMapper;

    @Override
    public ParkingSpotResponseDTO createSpot(ParkingSpotCreateDTO dto) {
        if (parkingSpotRepository.findByCode(dto.code()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Spot code already in use");
        }

        ParkingSpot spot = parkingSpotMapper.toEntity(dto);

        spot = parkingSpotRepository.save(spot);
        return parkingSpotMapper.toResponseDTO(spot);
    }

    @Override
    public Page<ParkingSpotResponseDTO> getAllSpots(Pageable pageable) {
        return parkingSpotRepository.findAll(pageable).map(parkingSpotMapper::toResponseDTO);
    }

    @Override
    public Page<ParkingSpotResponseDTO> getAllSpotsByType(Pageable pageable, VehicleType type) {
        return parkingSpotRepository.findAllByType(pageable, type).map(parkingSpotMapper::toResponseDTO);
    }

    @Override
    public Page<ParkingSpotResponseDTO> getAllSpotsByStatus(Pageable pageable, SpotStatus status) {
        return parkingSpotRepository.findAllByStatus(pageable, status).map(parkingSpotMapper::toResponseDTO);
    }

    @Override
    public ParkingSpotResponseDTO getSpotById(String id) {
        ParkingSpot spot = parkingSpotRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Spot not found"));
        return parkingSpotMapper.toResponseDTO(spot);
    }

    @Override
    public ParkingSpotResponseDTO getSpotByCode(String code) {
        ParkingSpot spot = parkingSpotRepository.findByCode(code)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Spot not found"));
        return parkingSpotMapper.toResponseDTO(spot);
    }

    @Override
    public Optional<ParkingSpotResponseDTO> findFirstFreeByType(VehicleType type) {
        return parkingSpotRepository.findFirstFreeByType(type)
                .map(parkingSpotMapper::toResponseDTO);
    }

    @Override
    public ParkingSpotResponseDTO updateSpot(String id, ParkingSpotUpdateDto dto) {
        ParkingSpot spot = parkingSpotRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Spot not found"));
        parkingSpotMapper.updateEntity(spot, dto);
        spot = parkingSpotRepository.save(spot);
        return parkingSpotMapper.toResponseDTO(spot);
    }

    @Override
    public ParkingSpotResponseDTO updateSpotStatus(String code, SpotStatus status) {
        ParkingSpot spot = parkingSpotRepository.findByCode(code)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Spot not found"));
        spot.setStatus(status);
        spot = parkingSpotRepository.save(spot);
        return parkingSpotMapper.toResponseDTO(spot);
    }

    @Override
    public void deleteSpot(String id) {
        parkingSpotRepository.deleteById(id);
    }
}
