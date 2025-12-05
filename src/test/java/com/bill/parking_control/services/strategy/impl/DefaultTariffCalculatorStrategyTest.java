package com.bill.parking_control.services.strategy.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.time.Instant;

import org.junit.jupiter.api.Test;

import com.bill.parking_control.persistences.entities.Tariff;
import com.bill.parking_control.persistences.entities.Vehicle.VehicleType;

class DefaultTariffCalculatorStrategyTest {

    private final DefaultTariffCalculatorStrategy strategy = new DefaultTariffCalculatorStrategy();

    @Test
    void calculate_FirstHour() {
        Instant entry = Instant.now().minusSeconds(45 * 60);
        Instant exit = Instant.now();
        Tariff tariff = Tariff.builder()
                .vehicleType(VehicleType.CAR)
                .hourlyRate(BigDecimal.valueOf(10))
                .build();

        BigDecimal amount = strategy.calculate(entry, exit, tariff);

        // 45 min -> 1 hour (2 half hours) -> 10 * 2 / 2 = 10
        assertEquals(BigDecimal.valueOf(10).setScale(2), amount);
    }

    @Test
    void calculate_TwoHours() {
        Instant entry = Instant.now().minusSeconds(2 * 3600);
        Instant exit = Instant.now();
        Tariff tariff = Tariff.builder()
                .vehicleType(VehicleType.CAR)
                .hourlyRate(BigDecimal.valueOf(10))
                .build();

        BigDecimal amount = strategy.calculate(entry, exit, tariff);

        // 120 min -> 4 half hours -> 10 * 4 / 2 = 20
        assertEquals(BigDecimal.valueOf(20).setScale(2), amount);
    }

    @Test
    void calculate_DailyRate() {
        Instant entry = Instant.now().minusSeconds(10 * 3600);
        Instant exit = Instant.now();
        Tariff tariff = Tariff.builder()
                .vehicleType(VehicleType.CAR)
                .hourlyRate(BigDecimal.valueOf(10))
                .dailyRate(BigDecimal.valueOf(50))
                .build();

        BigDecimal amount = strategy.calculate(entry, exit, tariff);

        // 10 hours > 8 hours -> daily rate
        assertEquals(BigDecimal.valueOf(50), amount);
    }
}
