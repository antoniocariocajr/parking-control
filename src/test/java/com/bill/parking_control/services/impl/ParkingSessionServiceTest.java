package com.bill.parking_control.services.impl;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import com.bill.parking_control.dtos.session.ParkingSessionResponseDTO;
import com.bill.parking_control.dtos.session.ParkingSessionStartDTO;
import com.bill.parking_control.persistences.entities.ParkingSession;
import com.bill.parking_control.persistences.entities.ParkingSpot;
import com.bill.parking_control.persistences.entities.User;
import com.bill.parking_control.persistences.entities.Vehicle;
import com.bill.parking_control.persistences.entities.Vehicle.VehicleType;
import com.bill.parking_control.persistences.repositories.ParkingSessionRepository;
import com.bill.parking_control.persistences.repositories.ParkingSpotRepository;
import com.bill.parking_control.persistences.repositories.UserRepository;
import com.bill.parking_control.persistences.repositories.VehicleRepository;
import com.bill.parking_control.services.mappers.ParkingSessionMapper;

@ExtendWith(MockitoExtension.class)
class ParkingSessionServiceTest {

    @Mock
    private ParkingSessionRepository parkingSessionRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private VehicleRepository vehicleRepository;
    @Mock
    private ParkingSpotRepository parkingSpotRepository;
    @Mock
    private ParkingSessionMapper parkingSessionMapper;

    @InjectMocks
    private ParkingSessionServiceImpl parkingSessionService;

    @Test
    void startSession_Success() {
        ParkingSessionStartDTO dto = new ParkingSessionStartDTO("ABC-1234", "A01", "user-id", Instant.now());

        Vehicle vehicle = Vehicle.builder().licensePlate("ABC-1234").type(VehicleType.CAR).build();
        User operator = User.builder().id("user-id").build();
        ParkingSpot spot = ParkingSpot.builder().code("A01").status(ParkingSpot.SpotStatus.FREE).type(VehicleType.CAR)
                .build();
        ParkingSession session = ParkingSession.builder().build();
        ParkingSessionResponseDTO responseDTO = new ParkingSessionResponseDTO("session-id", "ABC-1234", "A01",
                "user-id", Instant.now(), null, ParkingSession.SessionStatus.ACTIVE, null, null);

        when(vehicleRepository.findByLicensePlate("ABC-1234")).thenReturn(Optional.of(vehicle));
        when(userRepository.findById("user-id")).thenReturn(Optional.of(operator));
        when(parkingSpotRepository.findByCode("A01")).thenReturn(Optional.of(spot));
        when(parkingSessionMapper.toEntity(any(), any(), any(), any())).thenReturn(session);
        when(parkingSessionRepository.save(any())).thenReturn(session);
        when(parkingSessionMapper.toResponseDTO(any())).thenReturn(responseDTO);

        ParkingSessionResponseDTO result = parkingSessionService.startSession(dto);

        assertNotNull(result);
        verify(parkingSessionRepository).save(any());
        verify(parkingSpotRepository).findByCode("A01");
    }

    @Test
    void startSession_VehicleNotFound() {
        ParkingSessionStartDTO dto = new ParkingSessionStartDTO("ABC-1234", "A01", "user-id", Instant.now());

        when(vehicleRepository.findByLicensePlate("ABC-1234")).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> parkingSessionService.startSession(dto));
    }
}
