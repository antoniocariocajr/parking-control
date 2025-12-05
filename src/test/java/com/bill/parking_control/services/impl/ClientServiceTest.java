package com.bill.parking_control.services.impl;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import com.bill.parking_control.dtos.client.ClientCreateDTO;
import com.bill.parking_control.dtos.client.ClientResponseDTO;
import com.bill.parking_control.persistences.entities.Client;
import com.bill.parking_control.persistences.repositories.ClientRepository;
import com.bill.parking_control.services.mappers.ClientMapper;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;
    @Mock
    private ClientMapper clientMapper;

    @InjectMocks
    private ClientServiceImpl clientService;

    @Test
    void createClient_Success() {
        ClientCreateDTO dto = new ClientCreateDTO("Client Name", "12345678901", "client@example.com", "123456789");
        Client client = Client.builder().cpf("12345678901").build();
        ClientResponseDTO responseDTO = new ClientResponseDTO("client-id", "Client Name", "12345678901",
                "client@example.com", "123456789", null);

        when(clientRepository.findByCpf(dto.cpf())).thenReturn(Optional.empty());
        when(clientRepository.findByEmail(dto.email())).thenReturn(Optional.empty());
        when(clientRepository.save(any(Client.class))).thenReturn(client);
        when(clientMapper.toClientResponseDTO(client)).thenReturn(responseDTO);

        ClientResponseDTO result = clientService.createClient(dto);

        assertNotNull(result);
        verify(clientRepository).save(any(Client.class));
    }

    @Test
    void createClient_CpfAlreadyExists() {
        ClientCreateDTO dto = new ClientCreateDTO("Client Name", "12345678901", "client@example.com", "123456789");

        when(clientRepository.findByCpf(dto.cpf())).thenReturn(Optional.of(new Client()));

        assertThrows(ResponseStatusException.class, () -> clientService.createClient(dto));
    }
}
