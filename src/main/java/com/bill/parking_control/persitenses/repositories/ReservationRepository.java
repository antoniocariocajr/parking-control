package com.bill.parking_control.persitenses.repositories;

import com.bill.parking_control.persitenses.entities.Reservation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends MongoRepository<Reservation, String> {
    List<Reservation> findByClientId(String clientId);
}
