package com.bill.parking_control.persitenses.repositories;

import com.bill.parking_control.persitenses.entities.ParkingSpot;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParkingSpotRepository extends MongoRepository<ParkingSpot, String> {
    Optional<ParkingSpot> findByCode(String code);

    List<ParkingSpot> findByStatus(ParkingSpot.SpotStatus status);
}
