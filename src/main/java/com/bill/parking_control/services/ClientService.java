package com.bill.parking_control.services;

import com.bill.parking_control.dtos.client.ClientCreateDTO;
import com.bill.parking_control.dtos.client.ClientResponseDTO;
import com.bill.parking_control.dtos.client.ClientUpdateDto;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ClientService {
    ClientResponseDTO createClient(ClientCreateDTO dto);

    Page<ClientResponseDTO> getAllClients(Pageable pageable);

    ClientResponseDTO getClientById(String id);

    ClientResponseDTO updateClient(String id, ClientUpdateDto dto);

    void updateClientActiveStatus(String id, boolean isActive);

    void deleteClient(String id);
}
