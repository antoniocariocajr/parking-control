package com.bill.parking_control.persistences.repositories;

import com.bill.parking_control.persistences.entities.Vehicle;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface VehicleRepository extends MongoRepository<Vehicle, String> {
    Optional<Vehicle> findByLicensePlate(String licensePlate);

    Page<Vehicle> findByOwnerId(String ownerId, Pageable pageable);
}
