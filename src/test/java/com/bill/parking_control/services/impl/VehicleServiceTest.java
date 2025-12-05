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

import com.bill.parking_control.dtos.vehicle.VehicleCreateDTO;
import com.bill.parking_control.dtos.vehicle.VehicleResponseDTO;
import com.bill.parking_control.persistences.entities.Client;
import com.bill.parking_control.persistences.entities.Vehicle;
import com.bill.parking_control.persistences.entities.Vehicle.VehicleType;
import com.bill.parking_control.persistences.repositories.ClientRepository;
import com.bill.parking_control.persistences.repositories.VehicleRepository;
import com.bill.parking_control.services.mappers.VehicleMapper;

@ExtendWith(MockitoExtension.class)
class VehicleServiceTest {

    @Mock
    private VehicleRepository vehicleRepository;
    @Mock
    private ClientRepository clientRepository;
    @Mock
    private VehicleMapper vehicleMapper;

    @InjectMocks
    private VehicleServiceImpl vehicleService;

    @Test
    void createVehicle_Success() {
        VehicleCreateDTO dto = new VehicleCreateDTO("ABC-1234", "Brand", "Model", "Color", VehicleType.CAR, "owner-id");
        Client owner = Client.builder().id("owner-id").build();
        Vehicle vehicle = Vehicle.builder().licensePlate("ABC-1234").build();
        VehicleResponseDTO responseDTO = new VehicleResponseDTO("vehicle-id", "ABC-1234", "Brand", "Model", "Color",
                VehicleType.CAR, "owner-id");

        when(vehicleRepository.findByLicensePlate("ABC-1234")).thenReturn(Optional.empty());
        when(clientRepository.findById("owner-id")).thenReturn(Optional.of(owner));
        when(vehicleMapper.toEntity(dto, owner)).thenReturn(vehicle);
        when(vehicleRepository.save(vehicle)).thenReturn(vehicle);
        when(vehicleMapper.toVehicleResponseDTO(vehicle)).thenReturn(responseDTO);

        VehicleResponseDTO result = vehicleService.createVehicle(dto);

        assertNotNull(result);
        verify(vehicleRepository).save(vehicle);
    }

    @Test
    void createVehicle_LicensePlateInUse() {
        VehicleCreateDTO dto = new VehicleCreateDTO("ABC-1234", "Brand", "Model", "Color", VehicleType.CAR, "owner-id");

        when(vehicleRepository.findByLicensePlate("ABC-1234")).thenReturn(Optional.of(new Vehicle()));

        assertThrows(ResponseStatusException.class, () -> vehicleService.createVehicle(dto));
    }
}
