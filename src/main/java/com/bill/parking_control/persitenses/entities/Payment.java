package com.bill.parking_control.persitenses.entities;

import java.math.BigDecimal;
import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Document(collection = "payments")
public class Payment {
    @Id
    private String id;

    @DBRef
    private ParkingSession session; // ver abaixo
    private BigDecimal amount;

    private PaymentMethod method; // CASH, CREDIT, DEBIT, PIX
    private PaymentStatus status; // PENDING, PAID, REFUNDED

    private Instant paidAt;
    private String transactionId; // para integração com gateway

    @Builder.Default
    private Instant createdAt = Instant.now();
    @Builder.Default
    private Instant lastModifiedAt = Instant.now();

    public enum PaymentMethod {
        CASH, CREDIT, DEBIT, PIX
    }

    public enum PaymentStatus {
        PENDING, PAID, REFUNDED
    }
}
