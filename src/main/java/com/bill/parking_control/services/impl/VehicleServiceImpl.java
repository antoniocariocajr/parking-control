package com.bill.parking_control.services.impl;

import com.bill.parking_control.dtos.VehicleCreateDTO;
import com.bill.parking_control.dtos.VehicleResponseDTO;
import com.bill.parking_control.persitenses.entities.Client;
import com.bill.parking_control.persitenses.entities.Vehicle;
import com.bill.parking_control.persitenses.repositories.ClientRepository;
import com.bill.parking_control.persitenses.repositories.VehicleRepository;
import com.bill.parking_control.services.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VehicleServiceImpl implements VehicleService {

    private final VehicleRepository vehicleRepository;
    private final ClientRepository clientRepository;

    @Override
    @Transactional
    public VehicleResponseDTO createVehicle(VehicleCreateDTO dto) {
        if (vehicleRepository.findByLicensePlate(dto.licensePlate()).isPresent()) {
            throw new IllegalArgumentException("License plate already in use");
        }

        Vehicle vehicle = Vehicle.builder()
                .licensePlate(dto.licensePlate())
                .brand(dto.brand())
                .model(dto.model())
                .color(dto.color())
                .type(dto.type())
                .build();

        if (dto.ownerId() != null) {
            Client owner = clientRepository.findById(dto.ownerId())
                    .orElseThrow(() -> new RuntimeException("Owner not found"));
            vehicle.setOwner(owner);
            vehicle = vehicleRepository.save(vehicle);

            owner.getVehicles().add(vehicle);
            clientRepository.save(owner);
        } else {
            vehicle = vehicleRepository.save(vehicle);
        }

        return mapToDTO(vehicle);
    }

    @Override
    public List<VehicleResponseDTO> getAllVehicles() {
        return vehicleRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public VehicleResponseDTO getVehicleById(String id) {
        return mapToDTO(getEntityById(id));
    }

    @Override
    public Vehicle getEntityById(String id) {
        return vehicleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));
    }

    @Override
    public Vehicle getEntityByLicensePlate(String licensePlate) {
        return vehicleRepository.findByLicensePlate(licensePlate)
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));
    }

    private VehicleResponseDTO mapToDTO(Vehicle vehicle) {
        return new VehicleResponseDTO(
                vehicle.getId(),
                vehicle.getLicensePlate(),
                vehicle.getBrand(),
                vehicle.getModel(),
                vehicle.getColor(),
                vehicle.getType(),
                vehicle.getOwner() != null ? vehicle.getOwner().getId() : null);
    }
}
