package com.bill.parking_control.dtos.client;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.br.CPF;

public record ClientCreateDTO(
                @NotBlank String name,
                @NotBlank @CPF String cpf,
                @NotBlank @Email String email,
                @NotBlank String phone) {
}
