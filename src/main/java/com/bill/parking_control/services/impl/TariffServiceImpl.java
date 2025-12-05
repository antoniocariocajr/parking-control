package com.bill.parking_control.services.impl;

import com.bill.parking_control.dtos.tariff.TariffCreateDTO;
import com.bill.parking_control.dtos.tariff.TariffResponseDTO;
import com.bill.parking_control.dtos.tariff.TariffUpdateDto;
import com.bill.parking_control.persistences.entities.Tariff;
import com.bill.parking_control.persistences.entities.Vehicle;
import com.bill.parking_control.persistences.repositories.TariffRepository;
import com.bill.parking_control.services.TariffService;
import com.bill.parking_control.services.mappers.TariffMapper;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class TariffServiceImpl implements TariffService {

    private final TariffRepository tariffRepository;
    private final TariffMapper tariffMapper;

    @Override
    public TariffResponseDTO createTariff(TariffCreateDTO dto) {

        if (tariffRepository.findByVehicleType(dto.vehicleType()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Tariff already exists for vehicle type: " + dto.vehicleType());
        }

        Tariff tariff = tariffMapper.mapToEntity(dto);
        tariff = tariffRepository.save(tariff);
        return tariffMapper.mapToDTO(tariff);
    }

    @Override
    public Page<TariffResponseDTO> getAllTariffs(Pageable pageable) {
        return tariffRepository.findAll(pageable).map(tariffMapper::mapToDTO);
    }

    @Override
    public TariffResponseDTO getTariffByVehicleType(Vehicle.VehicleType type) {
        Tariff tariff = tariffRepository.findByVehicleType(type)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Tariff not found for vehicle type: " + type));
        return tariffMapper.mapToDTO(tariff);
    }

    @Override
    public TariffResponseDTO getTariffById(String id) {
        Tariff tariff = tariffRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tariff not found for id: " + id));
        return tariffMapper.mapToDTO(tariff);
    }

    @Override
    public TariffResponseDTO updateTariff(String id, TariffUpdateDto dto) {
        Tariff tariff = tariffRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tariff not found for id: " + id));
        if (dto.vehicleType() != tariff.getVehicleType() && dto.vehicleType() != null) {
            if (tariffRepository.findByVehicleType(dto.vehicleType()).isPresent()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Tariff already exists for vehicle type: " + dto.vehicleType());
            }
        }
        tariffMapper.updateEntity(tariff, dto);
        tariff = tariffRepository.save(tariff);
        return tariffMapper.mapToDTO(tariff);
    }

    @Override
    public void deleteTariff(String id) {
        Tariff tariff = tariffRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tariff not found for id: " + id));
        tariffRepository.delete(tariff);
    }

}
