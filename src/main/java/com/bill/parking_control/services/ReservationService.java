package com.bill.parking_control.services;

import java.time.Instant;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.bill.parking_control.dtos.reservation.ReservationCreateDTO;
import com.bill.parking_control.dtos.reservation.ReservationResponseDTO;
import com.bill.parking_control.persistences.entities.Reservation.ReservationStatus;

public interface ReservationService {
    ReservationResponseDTO create(ReservationCreateDTO dto);

    ReservationResponseDTO findById(String id);

    Page<ReservationResponseDTO> findAll(Pageable pageable);

    Page<ReservationResponseDTO> findAllByClient(Pageable pageable, String clientId);

    Page<ReservationResponseDTO> findAllBySpot(Pageable pageable, String spotId);

    Page<ReservationResponseDTO> findAllByStatus(Pageable pageable, ReservationStatus status);

    Page<ReservationResponseDTO> findAllByReservedFromBetween(Pageable pageable, Instant from, Instant to);

    void cancelReservation(String id);
}
