package com.bill.parking_control.services.impl;

import com.bill.parking_control.dtos.ParkingSessionResponseDTO;
import com.bill.parking_control.dtos.ParkingSessionStartDTO;
import com.bill.parking_control.persitenses.entities.ParkingSession;
import com.bill.parking_control.persitenses.entities.ParkingSpot;
import com.bill.parking_control.persitenses.entities.Tariff;
import com.bill.parking_control.persitenses.entities.User;
import com.bill.parking_control.persitenses.entities.Vehicle;
import com.bill.parking_control.persitenses.repositories.ParkingSessionRepository;
import com.bill.parking_control.persitenses.repositories.ParkingSpotRepository;
import com.bill.parking_control.services.ParkingSessionService;
import com.bill.parking_control.services.ParkingSpotService;
import com.bill.parking_control.services.TariffService;
import com.bill.parking_control.services.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ParkingSessionServiceImpl implements ParkingSessionService {

    private final ParkingSessionRepository parkingSessionRepository;
    private final VehicleService vehicleService;
    private final ParkingSpotService parkingSpotService;
    private final ParkingSpotRepository parkingSpotRepository; // Need repo to save status update
    private final TariffService tariffService;

    @Override
    @Transactional
    public ParkingSessionResponseDTO startSession(ParkingSessionStartDTO dto, User operator) {
        Vehicle vehicle = vehicleService.getEntityByLicensePlate(dto.vehicleLicensePlate());

        ParkingSpot spot;
        if (dto.spotCode() != null && !dto.spotCode().isBlank()) {
            spot = parkingSpotService.getEntityByCode(dto.spotCode());
            if (spot.getStatus() != ParkingSpot.SpotStatus.FREE) {
                throw new IllegalStateException("Spot is not free");
            }
        } else {
            // Find first free spot matching vehicle type (simplified)
            // Ideally we should filter by type, but for now just find any free spot
            spot = parkingSpotRepository.findByStatus(ParkingSpot.SpotStatus.FREE).stream()
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("No free spots available"));
        }

        // Update spot status
        spot.setStatus(ParkingSpot.SpotStatus.OCCUPIED);
        parkingSpotRepository.save(spot);

        ParkingSession session = ParkingSession.builder()
                .vehicle(vehicle)
                .spot(spot)
                .entryTime(LocalDateTime.now())
                .status(ParkingSession.SessionStatus.ACTIVE)
                .operator(operator)
                .build();

        session = parkingSessionRepository.save(session);
        return mapToDTO(session);
    }

    @Override
    @Transactional
    public ParkingSessionResponseDTO endSession(String sessionId, User operator) {
        ParkingSession session = parkingSessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));

        if (session.getStatus() != ParkingSession.SessionStatus.ACTIVE) {
            throw new IllegalStateException("Session is not active");
        }

        LocalDateTime exitTime = LocalDateTime.now();
        session.setExitTime(exitTime);
        session.setStatus(ParkingSession.SessionStatus.FINISHED);

        // Calculate amount
        Tariff tariff = tariffService.getTariffByVehicleType(session.getVehicle().getType());
        session.setHourlyRate(tariff.getHourlyRate());

        long hours = Duration.between(session.getEntryTime(), exitTime).toHours();
        if (hours == 0)
            hours = 1; // Minimum 1 hour

        BigDecimal total = tariff.getHourlyRate().multiply(BigDecimal.valueOf(hours));
        session.setTotalAmount(total);

        // Free the spot
        ParkingSpot spot = session.getSpot();
        spot.setStatus(ParkingSpot.SpotStatus.FREE);
        parkingSpotRepository.save(spot);

        session = parkingSessionRepository.save(session);
        return mapToDTO(session);
    }

    @Override
    public List<ParkingSessionResponseDTO> getAllSessions() {
        return parkingSessionRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ParkingSessionResponseDTO> getActiveSessions() {
        return parkingSessionRepository.findByStatus(ParkingSession.SessionStatus.ACTIVE).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private ParkingSessionResponseDTO mapToDTO(ParkingSession session) {
        return new ParkingSessionResponseDTO(
                session.getId(),
                session.getVehicle().getLicensePlate(),
                session.getSpot().getCode(),
                session.getEntryTime(),
                session.getExitTime(),
                session.getStatus(),
                session.getHourlyRate(),
                session.getTotalAmount());
    }
}
