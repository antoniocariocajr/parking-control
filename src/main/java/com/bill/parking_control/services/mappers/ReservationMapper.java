package com.bill.parking_control.services.mappers;

import org.springframework.stereotype.Component;

import com.bill.parking_control.dtos.reservation.ReservationCreateDTO;
import com.bill.parking_control.dtos.reservation.ReservationResponseDTO;
import com.bill.parking_control.persistences.entities.Client;
import com.bill.parking_control.persistences.entities.ParkingSpot;
import com.bill.parking_control.persistences.entities.Reservation;

@Component
public class ReservationMapper {

    public Reservation toEntity(ReservationCreateDTO dto, Client client, ParkingSpot spot) {
        return Reservation.builder()
                .client(client)
                .spot(spot)
                .reservedFrom(dto.reservedFrom())
                .reservedUntil(dto.reservedUntil())
                .build();
    }

    public ReservationResponseDTO toDTO(Reservation entity) {
        return new ReservationResponseDTO(
                entity.getId(),
                entity.getClient() != null ? entity.getClient().getId() : null,
                entity.getSpot() != null ? entity.getSpot().getId() : null,
                entity.getReservedFrom(),
                entity.getReservedUntil(),
                entity.getStatus());
    }
}
