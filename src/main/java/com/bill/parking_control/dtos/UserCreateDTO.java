package com.bill.parking_control.dtos;

import com.bill.parking_control.persitenses.entities.User.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserCreateDTO(
        @NotBlank @Email String email,
        @NotBlank String name,
        @NotBlank @Size(min = 6) String password,
        Role role) {
}
