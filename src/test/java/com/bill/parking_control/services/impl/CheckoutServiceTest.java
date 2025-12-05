package com.bill.parking_control.services.impl;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import com.bill.parking_control.dtos.payment.PaymentResponseDTO;
import com.bill.parking_control.persistences.entities.ParkingSession;
import com.bill.parking_control.persistences.entities.ParkingSpot;
import com.bill.parking_control.persistences.entities.Payment;
import com.bill.parking_control.persistences.entities.Payment.PaymentMethod;
import com.bill.parking_control.persistences.entities.Tariff;
import com.bill.parking_control.persistences.entities.Vehicle;
import com.bill.parking_control.persistences.entities.Vehicle.VehicleType;
import com.bill.parking_control.persistences.repositories.ParkingSessionRepository;
import com.bill.parking_control.persistences.repositories.ParkingSpotRepository;
import com.bill.parking_control.persistences.repositories.PaymentRepository;
import com.bill.parking_control.persistences.repositories.TariffRepository;
import com.bill.parking_control.services.mappers.PaymentMapper;

@ExtendWith(MockitoExtension.class)
class CheckoutServiceTest {

    @Mock
    private ParkingSessionRepository sessionRepository;
    @Mock
    private TariffRepository tariffRepository;
    @Mock
    private PaymentRepository paymentRepository;
    @Mock
    private ParkingSpotRepository parkingSpotRepository;
    @Mock
    private PaymentMapper paymentMapper;

    @InjectMocks
    private CheckoutServiceImpl checkoutService;

    @Test
    void checkout_Success() {
        String sessionId = "session-id";
        PaymentMethod paymentMethod = PaymentMethod.CREDIT;
        Vehicle vehicle = Vehicle.builder().type(VehicleType.CAR).build();
        ParkingSpot spot = ParkingSpot.builder().build();
        ParkingSession session = ParkingSession.builder()
                .id(sessionId)
                .vehicle(vehicle)
                .spot(spot)
                .entryTime(LocalDateTime.now().minusHours(1))
                .status(ParkingSession.SessionStatus.ACTIVE)
                .build();
        Tariff tariff = Tariff.builder().hourlyRate(BigDecimal.TEN).build();
        Payment payment = Payment.builder().build();
        PaymentResponseDTO responseDTO = new PaymentResponseDTO("payment-id", sessionId, BigDecimal.TEN, paymentMethod,
                Payment.PaymentStatus.PENDING, null, null);

        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));
        when(tariffRepository.findActiveByVehicleTypeAndDate(any(), any())).thenReturn(Optional.of(tariff));
        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);
        when(paymentMapper.toDTO(payment)).thenReturn(responseDTO);

        PaymentResponseDTO result = checkoutService.checkout(sessionId, paymentMethod);

        assertNotNull(result);
        verify(sessionRepository).save(session);
        verify(parkingSpotRepository).save(spot);
        verify(paymentRepository).save(any(Payment.class));
    }

    @Test
    void checkout_SessionNotFound() {
        String sessionId = "session-id";
        PaymentMethod paymentMethod = PaymentMethod.CREDIT;

        when(sessionRepository.findById(sessionId)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> checkoutService.checkout(sessionId, paymentMethod));
    }
}
