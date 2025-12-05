package com.bill.parking_control.persistences.repositories;

import com.bill.parking_control.persistences.entities.Client;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends MongoRepository<Client, String> {
    Optional<Client> findByCpf(String cpf);

    Optional<Client> findByEmail(String email);
}
