package com.bill.parking_control.services.impl;

import com.bill.parking_control.dtos.client.ClientCreateDTO;
import com.bill.parking_control.dtos.client.ClientResponseDTO;
import com.bill.parking_control.dtos.client.ClientUpdateDto;
import com.bill.parking_control.persitenses.entities.Client;
import com.bill.parking_control.persitenses.repositories.ClientRepository;
import com.bill.parking_control.services.ClientService;
import com.bill.parking_control.services.mappers.ClientMapper;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;

    @Override
    @Transactional
    public ClientResponseDTO createClient(ClientCreateDTO dto) {
        if (clientRepository.findByCpf(dto.cpf()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CPF already in use");
        }
        if (clientRepository.findByEmail(dto.email()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already in use");
        }

        Client client = Client.builder()
                .name(dto.name())
                .cpf(dto.cpf())
                .email(dto.email())
                .phone(dto.phone())
                .build();

        client = clientRepository.save(client);
        return clientMapper.toClientResponseDTO(client);
    }

    @Override
    public Page<ClientResponseDTO> getAllClients(Pageable pageable) {
        return clientRepository.findAll(pageable).map(clientMapper::toClientResponseDTO);
    }

    @Override
    public ClientResponseDTO getClientById(String id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Client not found"));
        return clientMapper.toClientResponseDTO(client);
    }

    @Override
    @Transactional
    public ClientResponseDTO updateClient(String id, ClientUpdateDto dto) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Client not found"));
        clientMapper.updateClient(client, dto);
        client = clientRepository.save(client);
        return clientMapper.toClientResponseDTO(client);
    }

    @Override
    @Transactional
    public void updateClientActiveStatus(String id, boolean isActive) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Client not found"));
        client.setActive(isActive);
        clientRepository.save(client);
    }

    @Override
    @Transactional
    public void deleteClient(String id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Client not found"));
        clientRepository.delete(client);
    }

}
