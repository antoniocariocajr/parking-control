package com.bill.parking_control.services.impl;

import com.bill.parking_control.dtos.payment.PaymentResponseDTO;
import com.bill.parking_control.persitenses.entities.ParkingSession;
import com.bill.parking_control.persitenses.entities.ParkingSession.SessionStatus;
import com.bill.parking_control.persitenses.entities.ParkingSpot;
import com.bill.parking_control.persitenses.entities.Payment;
import com.bill.parking_control.persitenses.entities.Payment.PaymentMethod;
import com.bill.parking_control.persitenses.entities.Payment.PaymentStatus;
import com.bill.parking_control.persitenses.entities.Tariff;
import com.bill.parking_control.persitenses.repositories.ParkingSessionRepository;
import com.bill.parking_control.persitenses.repositories.ParkingSpotRepository;
import com.bill.parking_control.persitenses.repositories.PaymentRepository;
import com.bill.parking_control.persitenses.repositories.TariffRepository;
import com.bill.parking_control.services.CheckoutService;
import com.bill.parking_control.services.mappers.PaymentMapper;

import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;

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
                                .findActiveByVehicleTypeAndDate(session.getVehicle().getType(), LocalDateTime.now())
                                .orElseThrow(
                                                () -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                                                "Tarifa não cadastrada para o tipo "
                                                                                + session.getVehicle().getType()));

                BigDecimal amount = calculateAmount(session.getEntryTime(),
                                LocalDateTime.now(),
                                tariff);

                // atualiza sessão
                session.setExitTime(LocalDateTime.now());
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

        private BigDecimal calculateAmount(LocalDateTime entry,
                        LocalDateTime exit,
                        Tariff t) {

                Duration duration = Duration.between(entry, exit);
                long minutes = duration.toMinutes();

                // regra simples: 1ª hora inteira, depois fração de 30 min
                // long hours = (minutes + 59) / 60; // arredonda pra cima
                long halfHours = (minutes + 29) / 30;

                // diária é mais barata?
                if (minutes >= 8 * 60 && t.getDailyRate() != null) {
                        return t.getDailyRate();
                }

                // hora cheia ou fração?
                BigDecimal rate = t.getHourlyRate();
                return rate
                                .multiply(BigDecimal.valueOf(halfHours))
                                .divide(BigDecimal.valueOf(2), 2, RoundingMode.HALF_UP);
        }
}
