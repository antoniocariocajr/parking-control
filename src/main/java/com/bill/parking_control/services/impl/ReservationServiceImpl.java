package com.bill.parking_control.services.impl;

import java.time.Instant;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.bill.parking_control.dtos.reservation.ReservationCreateDTO;
import com.bill.parking_control.dtos.reservation.ReservationResponseDTO;
import com.bill.parking_control.persistences.entities.Client;
import com.bill.parking_control.persistences.entities.ParkingSpot;
import com.bill.parking_control.persistences.entities.Reservation;
import com.bill.parking_control.persistences.repositories.ClientRepository;
import com.bill.parking_control.persistences.repositories.ParkingSpotRepository;
import com.bill.parking_control.persistences.repositories.ReservationRepository;
import com.bill.parking_control.services.ReservationService;
import com.bill.parking_control.services.mappers.ReservationMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final ClientRepository clientRepository;
    private final ParkingSpotRepository parkingSpotRepository;
    private final ReservationMapper reservationMapper;

    @Override
    public ReservationResponseDTO create(ReservationCreateDTO dto) {
        Client client = clientRepository.findById(dto.clientId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Client not found"));

        ParkingSpot spot = parkingSpotRepository.findById(dto.spotId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Spot not found"));

        if (spot.getStatus() != ParkingSpot.SpotStatus.FREE) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Spot is not free");
        }
        if (reservationRepository.existsBySpotAndReservedFromBetween(spot, dto.reservedFrom(), dto.reservedUntil())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Spot is already reserved");
        }
        if (reservationRepository.existsActiveReservationByClientId(client.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Client already has an active reservation");
        }
        spot.setStatus(ParkingSpot.SpotStatus.RESERVED);
        spot = parkingSpotRepository.save(spot);
        Reservation reservation = reservationMapper.toEntity(dto, client, spot);
        reservation = reservationRepository.save(reservation);
        return reservationMapper.toDTO(reservation);
    }

    @Override
    public ReservationResponseDTO findById(String id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reservation not found"));
        return reservationMapper.toDTO(reservation);
    }

    @Override
    public Page<ReservationResponseDTO> findAll(Pageable pageable) {
        return reservationRepository.findAll(pageable)
                .map(reservationMapper::toDTO);
    }

    @Override
    public Page<ReservationResponseDTO> findAllByClient(Pageable pageable, String clientId) {
        return reservationRepository.findByClientId(clientId, pageable)
                .map(reservationMapper::toDTO);
    }

    @Override
    public Page<ReservationResponseDTO> findAllBySpot(Pageable pageable, String spotId) {
        return reservationRepository.findBySpotId(spotId, pageable)
                .map(reservationMapper::toDTO);
    }

    @Override
    public Page<ReservationResponseDTO> findAllByStatus(Pageable pageable, Reservation.ReservationStatus status) {
        return reservationRepository.findByStatus(status, pageable)
                .map(reservationMapper::toDTO);
    }

    @Override
    public Page<ReservationResponseDTO> findAllByReservedFromBetween(Pageable pageable, Instant from,
            Instant to) {
        return reservationRepository.findByReservedFromBetween(from, to, pageable)
                .map(reservationMapper::toDTO);
    }

    @Override
    public void cancelReservation(String id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reservation not found"));

        reservation.setStatus(Reservation.ReservationStatus.CANCELLED);
        reservation.getSpot().setStatus(ParkingSpot.SpotStatus.FREE);
        reservationRepository.save(reservation);
        parkingSpotRepository.save(reservation.getSpot());
    }
}
