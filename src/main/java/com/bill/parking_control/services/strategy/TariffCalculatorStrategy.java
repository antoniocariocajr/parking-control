package com.bill.parking_control.services.strategy;

import java.math.BigDecimal;
import java.time.Instant;

import com.bill.parking_control.persistences.entities.Tariff;

public interface TariffCalculatorStrategy {
    BigDecimal calculate(Instant entry, Instant exit, Tariff tariff);
}
