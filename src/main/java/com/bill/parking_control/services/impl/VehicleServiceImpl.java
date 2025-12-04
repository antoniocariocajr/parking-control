package com.bill.parking_control.services.impl;

import com.bill.parking_control.dtos.vehicle.VehicleCreateDTO;
import com.bill.parking_control.dtos.vehicle.VehicleResponseDTO;
import com.bill.parking_control.dtos.vehicle.VehicleUpdateDto;
import com.bill.parking_control.persitenses.entities.Client;
import com.bill.parking_control.persitenses.entities.Vehicle;
import com.bill.parking_control.persitenses.repositories.ClientRepository;
import com.bill.parking_control.persitenses.repositories.VehicleRepository;
import com.bill.parking_control.services.VehicleService;
import com.bill.parking_control.services.mappers.VehicleMapper;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class VehicleServiceImpl implements VehicleService {

    private final VehicleRepository vehicleRepository;
    private final ClientRepository clientRepository;
    private final VehicleMapper vehicleMapper;

    @Override
    @Transactional
    public VehicleResponseDTO createVehicle(VehicleCreateDTO dto) {

        if (vehicleRepository.findByLicensePlate(dto.licensePlate()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "License plate already in use");
        }
        Client owner = clientRepository.findById(dto.ownerId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Owner not found"));

        Vehicle vehicle = vehicleMapper.toEntity(dto, owner);

        vehicle = vehicleRepository.save(vehicle);

        return vehicleMapper.toVehicleResponseDTO(vehicle);
    }

    @Override
    public Page<VehicleResponseDTO> getAllVehicles(Pageable pageable) {
        return vehicleRepository.findAll(pageable)
                .map(vehicleMapper::toVehicleResponseDTO);
    }

    @Override
    public Page<VehicleResponseDTO> getAllVehiclesByClientId(String clientId, Pageable pageable) {
        return vehicleRepository.findByOwnerId(clientId, pageable)
                .map(vehicleMapper::toVehicleResponseDTO);
    }

    @Override
    public VehicleResponseDTO getVehicleById(String id) {
        return vehicleMapper.toVehicleResponseDTO(vehicleRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Vehicle not found")));
    }

    @Override
    public VehicleResponseDTO getVehicleByLicensePlate(String licensePlate) {
        return vehicleMapper.toVehicleResponseDTO(vehicleRepository.findByLicensePlate(licensePlate)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Vehicle not found")));
    }

    @Override
    public VehicleResponseDTO updateVehicle(String id, VehicleUpdateDto dto) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Vehicle not found"));
        if (dto.licensePlate() != null && !dto.licensePlate().equals(vehicle.getLicensePlate())) {
            if (vehicleRepository.findByLicensePlate(dto.licensePlate()).isPresent()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "License plate already in use");
            }
        }
        vehicleMapper.updateVehicle(vehicle, dto);
        vehicle = vehicleRepository.save(vehicle);
        return vehicleMapper.toVehicleResponseDTO(vehicle);
    }

    @Override
    public VehicleResponseDTO updateVehicleOwner(String id, String ownerId) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Vehicle not found"));
        Client owner = clientRepository.findById(ownerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Owner not found"));
        vehicle.setOwner(owner);
        vehicle = vehicleRepository.save(vehicle);
        return vehicleMapper.toVehicleResponseDTO(vehicle);
    }

    @Override
    public void deleteVehicle(String id) {
        vehicleRepository.deleteById(id);
    }

}
