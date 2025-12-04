package com.bill.parking_control.persitenses.repositories;

import com.bill.parking_control.persitenses.entities.Tariff;
import com.bill.parking_control.persitenses.entities.Vehicle;
import com.bill.parking_control.persitenses.entities.Vehicle.VehicleType;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface TariffRepository extends MongoRepository<Tariff, String> {
    Optional<Tariff> findByVehicleType(Vehicle.VehicleType vehicleType);

    @Query("{ 'vehicleType' : ?0, 'active' : true, 'validFrom' : { $lte : ?1 }, 'validUntil' : { $gte : ?1 } }")
    Optional<Tariff> findActiveByVehicleTypeAndDate(VehicleType type, LocalDateTime date);
}
