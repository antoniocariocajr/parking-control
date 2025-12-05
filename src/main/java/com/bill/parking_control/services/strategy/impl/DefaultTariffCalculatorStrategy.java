package com.bill.parking_control.services.strategy.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.Instant;

import org.springframework.stereotype.Component;

import com.bill.parking_control.persistences.entities.Tariff;
import com.bill.parking_control.services.strategy.TariffCalculatorStrategy;

@Component
public class DefaultTariffCalculatorStrategy implements TariffCalculatorStrategy {

    @Override
    public BigDecimal calculate(Instant entry, Instant exit, Tariff tariff) {
        Duration duration = Duration.between(entry, exit);
        long minutes = duration.toMinutes();

        // regra simples: 1ª hora inteira, depois fração de 30 min
        long halfHours = (minutes + 29) / 30;

        // diária é mais barata?
        if (minutes >= 8 * 60 && tariff.getDailyRate() != null) {
            return tariff.getDailyRate();
        }

        // hora cheia ou fração?
        BigDecimal rate = tariff.getHourlyRate();
        return rate
                .multiply(BigDecimal.valueOf(halfHours))
                .divide(BigDecimal.valueOf(2), 2, RoundingMode.HALF_UP);
    }
}
