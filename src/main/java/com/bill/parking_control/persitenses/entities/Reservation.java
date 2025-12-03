package com.bill.parking_control.persitenses.entities;

import java.time.Instant;
import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document(collection = "reservations")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Reservation {
    @Id
    private String id;

    @DBRef
    private Client client;
    @DBRef
    private ParkingSpot spot;

    private LocalDateTime reservedFrom;
    private LocalDateTime reservedUntil;

    private ReservationStatus status; // ACTIVE, CANCELLED, COMPLETED

    @Builder.Default
    private Instant createdAt = Instant.now();
    @Builder.Default
    private Instant lastModifiedAt = Instant.now();

    public enum ReservationStatus {
        ACTIVE, CANCELLED, COMPLETED
    }
}
