package com.bill.parking_control.persitenses.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.bill.parking_control.persitenses.entities.Vehicle.VehicleType;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Document(collection = "tariffs")
public class Tariff {
    @Id
    private String id;

    private VehicleType vehicleType;
    private BigDecimal hourlyRate;
    private BigDecimal dailyRate;
    private BigDecimal monthlyRate;

    private LocalDateTime validFrom;
    private LocalDateTime validUntil;
}
