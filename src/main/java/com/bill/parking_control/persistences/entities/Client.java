package com.bill.parking_control.persistences.entities;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.validator.constraints.br.CPF;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Document(collection = "clients")
public class Client {
    @Id
    private String id;

    @NotBlank
    private String name;
    @CPF
    @Indexed(unique = true)
    private String cpf;
    @Email
    @Indexed(unique = true)
    private String email;
    private String phone;

    @DBRef
    @Builder.Default
    private List<Vehicle> vehicles = new ArrayList<>();

    @Builder.Default
    private boolean isActive = true;

    @Builder.Default
    @CreatedDate
    private Instant createdAt = Instant.now();
    @Builder.Default
    @LastModifiedDate
    private Instant lastModifiedAt = Instant.now();
}
