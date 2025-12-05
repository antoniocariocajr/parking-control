package com.bill.parking_control.services.mappers;

import org.springframework.stereotype.Component;

import com.bill.parking_control.dtos.client.ClientCreateDTO;
import com.bill.parking_control.dtos.client.ClientResponseDTO;
import com.bill.parking_control.dtos.client.ClientUpdateDto;
import com.bill.parking_control.dtos.vehicle.VehicleResponseDTO;
import com.bill.parking_control.persistences.entities.Client;
import com.bill.parking_control.persistences.entities.Vehicle;

@Component
public class ClientMapper {

    public ClientResponseDTO toClientResponseDTO(Client client) {
        return new ClientResponseDTO(
                client.getId(),
                client.getName(),
                client.getCpf(),
                client.getEmail(),
                client.getPhone(),
                client.getVehicles().stream().map(this::toVehicleResponseDTO).toList());
    }

    public VehicleResponseDTO toVehicleResponseDTO(Vehicle vehicle) {
        return new VehicleResponseDTO(
                vehicle.getId(),
                vehicle.getLicensePlate(),
                vehicle.getBrand(),
                vehicle.getModel(),
                vehicle.getColor(),
                vehicle.getType(),
                vehicle.getOwner().getId());
    }

    public Client toClient(ClientCreateDTO dto) {
        return Client.builder()
                .name(dto.name())
                .cpf(dto.cpf())
                .email(dto.email())
                .phone(dto.phone())
                .build();
    }

    public void updateClient(Client client, ClientUpdateDto dto) {
        if (dto.name() != null) {
            client.setName(dto.name());
        }
        if (dto.cpf() != null) {
            client.setCpf(dto.cpf());
        }
        if (dto.email() != null) {
            client.setEmail(dto.email());
        }
        if (dto.phone() != null) {
            client.setPhone(dto.phone());
        }
    }
}
