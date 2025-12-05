package com.bill.parking_control.persistences.repositories;

import com.bill.parking_control.persistences.entities.ParkingSpot;
import com.bill.parking_control.persistences.entities.Reservation;
import com.bill.parking_control.persistences.entities.Reservation.ReservationStatus;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface ReservationRepository extends MongoRepository<Reservation, String> {
    Page<Reservation> findByClientId(String clientId, Pageable pageable);

    boolean existsBySpotAndReservedFromBetween(ParkingSpot spot,
            LocalDateTime reservedFrom,
            LocalDateTime reservedUntil);

    boolean existsActiveReservationByClientId(String id);

    Page<Reservation> findBySpotId(String spotId, Pageable pageable);

    Page<Reservation> findByStatus(ReservationStatus status, Pageable pageable);

    Page<Reservation> findByReservedFromBetween(LocalDateTime from, LocalDateTime to, Pageable pageable);
}
