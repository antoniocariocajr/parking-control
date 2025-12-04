package com.bill.parking_control.services.impl;

import com.bill.parking_control.dtos.session.ParkingSessionResponseDTO;
import com.bill.parking_control.dtos.session.ParkingSessionStartDTO;
import com.bill.parking_control.dtos.session.ParkingSessionUpdateDto;
import com.bill.parking_control.persitenses.entities.ParkingSession;
import com.bill.parking_control.persitenses.entities.ParkingSpot;
import com.bill.parking_control.persitenses.entities.Tariff;
import com.bill.parking_control.persitenses.entities.User;
import com.bill.parking_control.persitenses.entities.Vehicle;
import com.bill.parking_control.persitenses.repositories.ParkingSessionRepository;
import com.bill.parking_control.persitenses.repositories.ParkingSpotRepository;
import com.bill.parking_control.persitenses.repositories.TariffRepository;
import com.bill.parking_control.persitenses.repositories.UserRepository;
import com.bill.parking_control.persitenses.repositories.VehicleRepository;
import com.bill.parking_control.services.ParkingSessionService;
import com.bill.parking_control.services.mappers.ParkingSessionMapper;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ParkingSessionServiceImpl implements ParkingSessionService {

    private final ParkingSessionRepository parkingSessionRepository;
    private final UserRepository userRepository;
    private final VehicleRepository vehicleRepository;
    private final ParkingSpotRepository parkingSpotRepository;
    private final TariffRepository tariffRepository;
    private final ParkingSessionMapper parkingSessionMapper;

    @Override
    @Transactional
    public ParkingSessionResponseDTO startSession(ParkingSessionStartDTO dto) {
        Vehicle vehicle = vehicleRepository.findByLicensePlate(dto.vehicleLicensePlate())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Vehicle not found"));
        User operator = userRepository.findById(dto.operatorId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Operator not found"));
        ParkingSpot spot;
        if (dto.spotCode() != null && !dto.spotCode().isBlank()) {
            spot = parkingSpotRepository.findByCode(dto.spotCode())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Spot not found"));
            if (spot.getStatus() != ParkingSpot.SpotStatus.FREE) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Spot is not free");
            }
            if (spot.getType() != vehicle.getType()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Spot type does not match vehicle type");
            }
        } else {
            // Find first free spot matching vehicle type (simplified)
            // Ideally we should filter by type, but for now just find any free spot
            spot = parkingSpotRepository.findFirstFreeByType(vehicle.getType())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "No free spots available"));
        }

        // Update spot status
        spot.setStatus(ParkingSpot.SpotStatus.OCCUPIED);

        ParkingSession session = parkingSessionMapper.toEntity(vehicle, spot, operator, dto.entryTime());
        session = parkingSessionRepository.save(session);
        return parkingSessionMapper.toResponseDTO(session);
    }

    @Override
    @Transactional
    public ParkingSessionResponseDTO updateSession(String sessionId, ParkingSessionUpdateDto dto) {
        ParkingSession session = parkingSessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Session not found"));
        Vehicle vehicle = null;
        if (dto.vehicleLicensePlate() != null && !dto.vehicleLicensePlate().isBlank()) {
            vehicle = vehicleRepository.findByLicensePlate(dto.vehicleLicensePlate())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Vehicle not found"));
        }
        ParkingSpot spot = null;
        if (dto.spotCode() != null && !dto.spotCode().isBlank()) {
            spot = parkingSpotRepository.findByCode(dto.spotCode())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Spot not found"));
        }
        User operator = null;
        if (dto.operatorId() != null && !dto.operatorId().isBlank()) {
            operator = userRepository.findById(dto.operatorId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Operator not found"));
        }
        parkingSessionMapper.updateEntity(session, vehicle, spot, operator, dto);
        session = parkingSessionRepository.save(session);
        return parkingSessionMapper.toResponseDTO(session);
    }

    @Override
    @Transactional
    public ParkingSessionResponseDTO endSession(String sessionId) {
        ParkingSession session = parkingSessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Session not found"));
        if (session.getStatus() != ParkingSession.SessionStatus.ACTIVE) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Session is not active");
        }
        session.setExitTime(LocalDateTime.now());
        session.setStatus(ParkingSession.SessionStatus.FINISHED);

        // Calculate amount
        Tariff tariff = tariffRepository.findByVehicleType(session.getVehicle().getType())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tariff not found"));
        session.setHourlyRate(tariff.getHourlyRate());

        long hours = Duration.between(session.getEntryTime(), session.getExitTime()).toHours();
        if (hours == 0)
            hours = 1; // Minimum 1 hour

        BigDecimal total = tariff.getHourlyRate().multiply(BigDecimal.valueOf(hours));
        session.setTotalAmount(total);

        // Free the spot
        ParkingSpot spot = session.getSpot();
        spot.setStatus(ParkingSpot.SpotStatus.FREE);
        parkingSpotRepository.save(spot);

        session = parkingSessionRepository.save(session);
        return parkingSessionMapper.toResponseDTO(session);
    }

    @Override
    public Page<ParkingSessionResponseDTO> getAllSessions(Pageable pageable) {
        return parkingSessionRepository.findAll(pageable).map(parkingSessionMapper::toResponseDTO);
    }

    @Override
    public Page<ParkingSessionResponseDTO> getActiveSessions(Pageable pageable) {
        List<ParkingSession> sessions = parkingSessionRepository.findByStatus(ParkingSession.SessionStatus.ACTIVE);
        return parkingSessionMapper.toResponseDTO(sessions, pageable);
    }

}
