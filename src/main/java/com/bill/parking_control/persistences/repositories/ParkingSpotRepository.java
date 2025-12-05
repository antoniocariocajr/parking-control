package com.bill.parking_control.persistences.repositories;

import com.bill.parking_control.persistences.entities.ParkingSpot;
import com.bill.parking_control.persistences.entities.ParkingSpot.SpotStatus;
import com.bill.parking_control.persistences.entities.Vehicle.VehicleType;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParkingSpotRepository extends MongoRepository<ParkingSpot, String> {
    Optional<ParkingSpot> findByCode(String code);

    List<ParkingSpot> findByStatus(ParkingSpot.SpotStatus status);

    @Query("{ 'status': 'FREE', 'type': ?0 }")
    Optional<ParkingSpot> findFirstFreeByType(VehicleType type);

    Page<ParkingSpot> findAllByType(Pageable pageable, VehicleType type);

    Page<ParkingSpot> findAllByStatus(Pageable pageable, SpotStatus status);
}
