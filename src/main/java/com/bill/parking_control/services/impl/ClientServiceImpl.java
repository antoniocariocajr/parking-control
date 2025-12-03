package com.bill.parking_control.services.impl;

import com.bill.parking_control.dtos.ClientCreateDTO;
import com.bill.parking_control.dtos.ClientResponseDTO;
import com.bill.parking_control.dtos.VehicleResponseDTO;
import com.bill.parking_control.persitenses.entities.Client;
import com.bill.parking_control.persitenses.entities.Vehicle;
import com.bill.parking_control.persitenses.repositories.ClientRepository;
import com.bill.parking_control.services.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;

    @Override
    public ClientResponseDTO createClient(ClientCreateDTO dto) {
        if (clientRepository.findByCpf(dto.cpf()).isPresent()) {
            throw new IllegalArgumentException("CPF already in use");
        }
        if (clientRepository.findByEmail(dto.email()).isPresent()) {
            throw new IllegalArgumentException("Email already in use");
        }

        Client client = Client.builder()
                .name(dto.name())
                .cpf(dto.cpf())
                .email(dto.email())
                .phone(dto.phone())
                .build();

        client = clientRepository.save(client);
        return mapToDTO(client);
    }

    @Override
    public List<ClientResponseDTO> getAllClients() {
        return clientRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ClientResponseDTO getClientById(String id) {
        return mapToDTO(getEntityById(id));
    }

    @Override
    public Client getEntityById(String id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client not found"));
    }

    private ClientResponseDTO mapToDTO(Client client) {
        List<VehicleResponseDTO> vehicleDTOs = client.getVehicles() != null
                ? client.getVehicles().stream().map(this::mapVehicleToDTO).collect(Collectors.toList())
                : Collections.emptyList();

        return new ClientResponseDTO(
                client.getId(),
                client.getName(),
                client.getCpf(),
                client.getEmail(),
                client.getPhone(),
                vehicleDTOs);
    }

    private VehicleResponseDTO mapVehicleToDTO(Vehicle vehicle) {
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
