package com.bill.parking_control.persistences.entities;

import java.math.BigDecimal;
import java.time.Instant;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Document(collection = "parking_sessions")
public class ParkingSession {
    @Id
    private String id;

    @DBRef
    private Vehicle vehicle;
    @DBRef
    private ParkingSpot spot; // vaga física

    private Instant entryTime;
    private Instant exitTime;

    @Builder.Default
    private SessionStatus status = SessionStatus.ACTIVE; // ACTIVE, FINISHED, CANCELLED

    @DBRef
    private User operator; // quem liberou entrada

    private BigDecimal hourlyRate; // tarifa vigente
    private BigDecimal totalAmount;

    @Builder.Default
    private Instant createdAt = Instant.now();
    @Builder.Default
    private Instant lastModifiedAt = Instant.now();

    public enum SessionStatus {
        ACTIVE, FINISHED, CANCELLED
    }
}