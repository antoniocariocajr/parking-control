package com.bill.parking_control.services.impl;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import com.bill.parking_control.dtos.tariff.TariffCreateDTO;
import com.bill.parking_control.dtos.tariff.TariffResponseDTO;
import com.bill.parking_control.persistences.entities.Tariff;
import com.bill.parking_control.persistences.entities.Vehicle.VehicleType;
import com.bill.parking_control.persistences.repositories.TariffRepository;
import com.bill.parking_control.services.mappers.TariffMapper;

@ExtendWith(MockitoExtension.class)
class TariffServiceTest {

    @Mock
    private TariffRepository tariffRepository;
    @Mock
    private TariffMapper tariffMapper;

    @InjectMocks
    private TariffServiceImpl tariffService;

    @Test
    void createTariff_Success() {
        TariffCreateDTO dto = new TariffCreateDTO(VehicleType.CAR, BigDecimal.TEN, BigDecimal.valueOf(100),
                BigDecimal.valueOf(2000), Instant.now(), Instant.now().plusSeconds(30 * 24 * 3600));
        Tariff tariff = Tariff.builder().vehicleType(VehicleType.CAR).build();
        TariffResponseDTO responseDTO = new TariffResponseDTO("tariff-id", VehicleType.CAR, BigDecimal.TEN,
                BigDecimal.valueOf(100), BigDecimal.valueOf(2000), Instant.now(),
                Instant.now().plusSeconds(30 * 24 * 3600), true);

        when(tariffRepository.findByVehicleType(VehicleType.CAR)).thenReturn(Optional.empty());
        when(tariffMapper.mapToEntity(dto)).thenReturn(tariff);
        when(tariffRepository.save(tariff)).thenReturn(tariff);
        when(tariffMapper.mapToDTO(tariff)).thenReturn(responseDTO);

        TariffResponseDTO result = tariffService.createTariff(dto);

        assertNotNull(result);
        verify(tariffRepository).save(tariff);
    }

    @Test
    void createTariff_AlreadyExists() {
        TariffCreateDTO dto = new TariffCreateDTO(VehicleType.CAR, BigDecimal.TEN, BigDecimal.valueOf(100),
                BigDecimal.valueOf(2000), Instant.now(), Instant.now().plusSeconds(30 * 24 * 3600));

        when(tariffRepository.findByVehicleType(VehicleType.CAR)).thenReturn(Optional.of(new Tariff()));

        assertThrows(ResponseStatusException.class, () -> tariffService.createTariff(dto));
    }
}
