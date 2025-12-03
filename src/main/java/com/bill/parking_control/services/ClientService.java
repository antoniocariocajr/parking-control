package com.bill.parking_control.services;

import com.bill.parking_control.dtos.client.ClientCreateDTO;
import com.bill.parking_control.dtos.client.ClientResponseDTO;
import com.bill.parking_control.persitenses.entities.Client;

import java.util.List;

public interface ClientService {
    ClientResponseDTO createClient(ClientCreateDTO dto);

    List<ClientResponseDTO> getAllClients();

    ClientResponseDTO getClientById(String id);

    Client getEntityById(String id);
}
