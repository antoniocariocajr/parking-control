package com.bill.parking_control.persitenses.entities;

import java.time.Instant;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document(collection = "parking_spots")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ParkingSpot {
    @Id
    private String id;

    @NotBlank
    @Indexed(unique = true)
    private String code; // A01, B13…
    private SpotType type; // CAR, MOTORCYCLE, TRUCK
    private SpotStatus status; // FREE, OCCUPIED, RESERVED, BLOCKED

    private boolean covered; // coberta ou não

    @Builder.Default
    private Instant createdAt = Instant.now();
    @Builder.Default
    private Instant lastModifiedAt = Instant.now();

    public enum SpotType {
        CAR, MOTORCYCLE, TRUCK
    }

    public enum SpotStatus {
        FREE, OCCUPIED, RESERVED, BLOCKED
    }
}
