package com.bill.parking_control.services.impl;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import com.bill.parking_control.dtos.spot.ParkingSpotCreateDTO;
import com.bill.parking_control.dtos.spot.ParkingSpotResponseDTO;
import com.bill.parking_control.persistences.entities.ParkingSpot;
import com.bill.parking_control.persistences.entities.ParkingSpot.SpotStatus;
import com.bill.parking_control.persistences.entities.Vehicle.VehicleType;
import com.bill.parking_control.persistences.repositories.ParkingSpotRepository;
import com.bill.parking_control.services.mappers.ParkingSpotMapper;

@ExtendWith(MockitoExtension.class)
class ParkingSpotServiceTest {

    @Mock
    private ParkingSpotRepository parkingSpotRepository;
    @Mock
    private ParkingSpotMapper parkingSpotMapper;

    @InjectMocks
    private ParkingSpotServiceImpl parkingSpotService;

    @Test
    void createSpot_Success() {
        ParkingSpotCreateDTO dto = new ParkingSpotCreateDTO("A01", VehicleType.CAR, SpotStatus.FREE, true);
        ParkingSpot spot = ParkingSpot.builder().code("A01").build();
        ParkingSpotResponseDTO responseDTO = new ParkingSpotResponseDTO("spot-id", "A01", VehicleType.CAR,
                SpotStatus.FREE, true);

        when(parkingSpotRepository.findByCode(dto.code())).thenReturn(Optional.empty());
        when(parkingSpotMapper.toEntity(dto)).thenReturn(spot);
        when(parkingSpotRepository.save(spot)).thenReturn(spot);
        when(parkingSpotMapper.toResponseDTO(spot)).thenReturn(responseDTO);

        ParkingSpotResponseDTO result = parkingSpotService.createSpot(dto);

        assertNotNull(result);
        verify(parkingSpotRepository).save(spot);
    }

    @Test
    void createSpot_CodeAlreadyInUse() {
        ParkingSpotCreateDTO dto = new ParkingSpotCreateDTO("A01", VehicleType.CAR, SpotStatus.FREE, true);

        when(parkingSpotRepository.findByCode(dto.code())).thenReturn(Optional.of(new ParkingSpot()));

        assertThrows(ResponseStatusException.class, () -> parkingSpotService.createSpot(dto));
    }
}
