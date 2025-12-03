package com.bill.parking_control.persitenses.repositories;

import com.bill.parking_control.persitenses.entities.Tariff;
import com.bill.parking_control.persitenses.entities.Vehicle;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TariffRepository extends MongoRepository<Tariff, String> {
    Optional<Tariff> findByVehicleType(Vehicle.VehicleType vehicleType);
}
