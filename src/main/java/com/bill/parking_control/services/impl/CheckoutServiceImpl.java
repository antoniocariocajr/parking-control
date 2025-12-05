package com.bill.parking_control.services.impl;

import com.bill.parking_control.dtos.payment.PaymentResponseDTO;
import com.bill.parking_control.persistences.entities.ParkingSession;
import com.bill.parking_control.persistences.entities.ParkingSession.SessionStatus;
import com.bill.parking_control.persistences.entities.ParkingSpot;
import com.bill.parking_control.persistences.entities.Payment;
import com.bill.parking_control.persistences.entities.Payment.PaymentMethod;
import com.bill.parking_control.persistences.entities.Payment.PaymentStatus;
import com.bill.parking_control.persistences.entities.Tariff;
import com.bill.parking_control.persistences.repositories.ParkingSessionRepository;
import com.bill.parking_control.persistences.repositories.ParkingSpotRepository;
import com.bill.parking_control.persistences.repositories.PaymentRepository;
import com.bill.parking_control.persistences.repositories.TariffRepository;
import com.bill.parking_control.services.CheckoutService;
import com.bill.parking_control.services.mappers.PaymentMapper;
import com.bill.parking_control.services.strategy.TariffCalculatorStrategy;

import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class CheckoutServiceImpl implements CheckoutService {

        private final ParkingSessionRepository sessionRepository;
        private final TariffRepository tariffRepository;
        private final PaymentRepository paymentRepository;
        private final ParkingSpotRepository parkingSpotRepository;
        private final PaymentMapper paymentMapper;
        private final TariffCalculatorStrategy tariffCalculatorStrategy;

        @Transactional
        @Override
        public PaymentResponseDTO checkout(String sessionId, PaymentMethod paymentMethod) {

                ParkingSession session = sessionRepository.findById(sessionId)
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                                "Sessão não encontrada"));

                if (session.getStatus() != SessionStatus.ACTIVE) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                        "Sessão já foi finalizada ou cancelada");
                }

                Tariff tariff = tariffRepository
                                .findActiveByVehicleTypeAndDate(session.getVehicle().getType(), Instant.now())
                                .orElseThrow(
                                                () -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                                                "Tarifa não cadastrada para o tipo "
                                                                                + session.getVehicle().getType()));

                BigDecimal amount = tariffCalculatorStrategy.calculate(session.getEntryTime(),
                                Instant.now(),
                                tariff);

                // atualiza sessão
                session.setExitTime(Instant.now());
                session.setStatus(SessionStatus.FINISHED);
                session.setTotalAmount(amount);
                sessionRepository.save(session);

                // cria pagamento
                Payment payment = Payment.builder()
                                .session(session)
                                .amount(amount)
                                .method(paymentMethod)
                                .status(PaymentStatus.PENDING)
                                .build();
                // Free the spot
                ParkingSpot spot = session.getSpot();
                spot.setStatus(ParkingSpot.SpotStatus.FREE);
                parkingSpotRepository.save(spot);

                return paymentMapper.toDTO(paymentRepository.save(payment));
        }
}
