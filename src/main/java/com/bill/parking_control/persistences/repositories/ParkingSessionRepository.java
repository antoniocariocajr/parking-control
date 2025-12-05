package com.bill.parking_control.persistences.repositories;

import com.bill.parking_control.persistences.entities.ParkingSession;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParkingSessionRepository extends MongoRepository<ParkingSession, String> {
    List<ParkingSession> findByStatus(ParkingSession.SessionStatus status);

    List<ParkingSession> findByVehicleId(String vehicleId);
}
