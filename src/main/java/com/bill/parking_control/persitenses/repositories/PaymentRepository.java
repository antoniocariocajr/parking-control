package com.bill.parking_control.persitenses.repositories;

import com.bill.parking_control.persitenses.entities.Payment;
import com.bill.parking_control.persitenses.entities.Payment.PaymentStatus;

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

    @Query("{ 'status' : 'PAID', 'paidAt' : {  : ?0,  : ?1 } }")
    Page<Payment> findPaidBetween(Instant startOfDay, Instant endOfDay, Pageable pageable);

    @Query("{ 'status' : 'PAID' }")
    Page<Payment> findAllPaid(Pageable pageable);

    Page<Payment> findByPaymentStatus(Pageable pageable, PaymentStatus status);
}
