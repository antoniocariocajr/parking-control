package com.bill.parking_control.services.mappers;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.bill.parking_control.dtos.session.ParkingSessionResponseDTO;
import com.bill.parking_control.dtos.session.ParkingSessionUpdateDto;
import com.bill.parking_control.persitenses.entities.ParkingSession;
import com.bill.parking_control.persitenses.entities.ParkingSpot;
import com.bill.parking_control.persitenses.entities.User;
import com.bill.parking_control.persitenses.entities.Vehicle;

@Component
public class ParkingSessionMapper {
    public ParkingSessionResponseDTO toResponseDTO(ParkingSession parkingSession) {
        return new ParkingSessionResponseDTO(
                parkingSession.getId(),
                parkingSession.getVehicle().getLicensePlate(),
                parkingSession.getSpot().getCode(),
                parkingSession.getOperator().getId(),
                parkingSession.getEntryTime(),
                parkingSession.getExitTime(),
                parkingSession.getStatus(),
                parkingSession.getHourlyRate(),
                parkingSession.getTotalAmount());
    }

    public ParkingSession toEntity(
            Vehicle vehicle,
            ParkingSpot parkingSpot,
            User operator,
            LocalDateTime entryTime) {

        entryTime = entryTime != null ? entryTime : LocalDateTime.now();
        return ParkingSession.builder()
                .vehicle(vehicle)
                .spot(parkingSpot)
                .operator(operator)
                .entryTime(entryTime)
                .build();
    }

    public void updateEntity(
            ParkingSession parkingSession,
            Vehicle vehicle,
            ParkingSpot parkingSpot,
            User operator,
            ParkingSessionUpdateDto parkingSessionUpdateDto) {
        if (vehicle != null) {
            parkingSession.setVehicle(vehicle);
        }
        if (parkingSpot != null) {
            parkingSession.setSpot(parkingSpot);
        }
        if (operator != null) {
            parkingSession.setOperator(operator);
        }
        if (parkingSessionUpdateDto.entryTime() != null) {
            parkingSession.setEntryTime(parkingSessionUpdateDto.entryTime());
        }
    }

    public Page<ParkingSessionResponseDTO> toResponseDTO(List<ParkingSession> parkingSessions, Pageable pageable) {
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), parkingSessions.size());
        List<ParkingSessionResponseDTO> content = parkingSessions.subList(start, end).stream()
                .map(this::toResponseDTO).toList();
        return new PageImpl<>(content, pageable, parkingSessions.size());
    }
}
