package com.bill.parking_control.persistences.repositories;

import com.bill.parking_control.persistences.entities.Payment;
import com.bill.parking_control.persistences.entities.Payment.PaymentStatus;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;

@Repository
public interface PaymentRepository extends MongoRepository<Payment, String> {
    Optional<Payment> findBySessionId(String sessionId);

    @Query("{ 'status' : 'PAID', 'paidAt' : { $gte : ?0, $lt : ?1 } }")
    Page<Payment> findPaidBetween(Instant startOfDay, Instant endOfDay, Pageable pageable);

    @Query("{ 'status' : 'PAID' }")
    Page<Payment> findAllPaid(Pageable pageable);

    @Query("{ 'status' : ?0 }")
    Page<Payment> findByPaymentStatus(PaymentStatus status, Pageable pageable);
}
