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

import com.bill.parking_control.dtos.reservation.ReservationCreateDTO;
import com.bill.parking_control.dtos.reservation.ReservationResponseDTO;
import com.bill.parking_control.persistences.entities.Client;
import com.bill.parking_control.persistences.entities.ParkingSpot;
import com.bill.parking_control.persistences.entities.Reservation;
import com.bill.parking_control.persistences.repositories.ClientRepository;
import com.bill.parking_control.persistences.repositories.ParkingSpotRepository;
import com.bill.parking_control.persistences.repositories.ReservationRepository;
import com.bill.parking_control.services.mappers.ReservationMapper;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;
    @Mock
    private ClientRepository clientRepository;
    @Mock
    private ParkingSpotRepository parkingSpotRepository;
    @Mock
    private ReservationMapper reservationMapper;

    @InjectMocks
    private ReservationServiceImpl reservationService;

    @Test
    void create_Success() {
        ReservationCreateDTO dto = new ReservationCreateDTO("client-id", "spot-id", Instant.now(),
                Instant.now().plusSeconds(3600));
        Client client = Client.builder().id("client-id").build();
        ParkingSpot spot = ParkingSpot.builder().id("spot-id").status(ParkingSpot.SpotStatus.FREE).build();
        Reservation reservation = Reservation.builder().build();
        ReservationResponseDTO responseDTO = new ReservationResponseDTO("res-id", "client-id", "spot-id", null, null,
                null);

        when(clientRepository.findById("client-id")).thenReturn(Optional.of(client));
        when(parkingSpotRepository.findById("spot-id")).thenReturn(Optional.of(spot));
        when(reservationRepository.existsBySpotAndReservedFromBetween(any(), any(), any())).thenReturn(false);
        when(reservationRepository.existsActiveReservationByClientId("client-id")).thenReturn(false);
        when(parkingSpotRepository.save(spot)).thenReturn(spot);
        when(reservationMapper.toEntity(dto, client, spot)).thenReturn(reservation);
        when(reservationRepository.save(reservation)).thenReturn(reservation);
        when(reservationMapper.toDTO(reservation)).thenReturn(responseDTO);

        ReservationResponseDTO result = reservationService.create(dto);

        assertNotNull(result);
        verify(reservationRepository).save(reservation);
    }

    @Test
    void create_SpotNotFree() {
        ReservationCreateDTO dto = new ReservationCreateDTO("client-id", "spot-id", Instant.now(),
                Instant.now().plusSeconds(3600));
        Client client = Client.builder().id("client-id").build();
        ParkingSpot spot = ParkingSpot.builder().id("spot-id").status(ParkingSpot.SpotStatus.OCCUPIED).build();

        when(clientRepository.findById("client-id")).thenReturn(Optional.of(client));
        when(parkingSpotRepository.findById("spot-id")).thenReturn(Optional.of(spot));

        assertThrows(ResponseStatusException.class, () -> reservationService.create(dto));
    }
}
