package com.bill.parking_control.services.impl;

import com.bill.parking_control.dtos.session.ParkingSessionResponseDTO;
import com.bill.parking_control.dtos.session.ParkingSessionStartDTO;
import com.bill.parking_control.dtos.session.ParkingSessionUpdateDto;
import com.bill.parking_control.persitenses.entities.ParkingSession;
import com.bill.parking_control.persitenses.entities.ParkingSession.SessionStatus;
import com.bill.parking_control.persitenses.entities.ParkingSpot;
import com.bill.parking_control.persitenses.entities.User;
import com.bill.parking_control.persitenses.entities.Vehicle;
import com.bill.parking_control.persitenses.repositories.ParkingSessionRepository;
import com.bill.parking_control.persitenses.repositories.ParkingSpotRepository;
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
import java.util.List;

@Service
@RequiredArgsConstructor
public class ParkingSessionServiceImpl implements ParkingSessionService {

    private final ParkingSessionRepository parkingSessionRepository;
    private final UserRepository userRepository;
    private final VehicleRepository vehicleRepository;
    private final ParkingSpotRepository parkingSpotRepository;
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
    public ParkingSessionResponseDTO getSessionById(String sessionId) {
        ParkingSession session = parkingSessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Session not found"));
        return parkingSessionMapper.toResponseDTO(session);
    }

    @Override
    public Page<ParkingSessionResponseDTO> getAllSessions(Pageable pageable) {
        return parkingSessionRepository.findAll(pageable).map(parkingSessionMapper::toResponseDTO);
    }

    @Override
    public Page<ParkingSessionResponseDTO> getActiveSessions(Pageable pageable, SessionStatus status) {
        List<ParkingSession> sessions = parkingSessionRepository.findByStatus(status);
        return parkingSessionMapper.toResponseDTO(sessions, pageable);
    }

    @Override
    public void cancelSession(String sessionId) {
        ParkingSession session = parkingSessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Session not found"));
        if (session.getStatus() == ParkingSession.SessionStatus.ACTIVE) {
            session.setStatus(ParkingSession.SessionStatus.CANCELLED);
            parkingSessionRepository.save(session);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Session is not active");
        }
    }
}
