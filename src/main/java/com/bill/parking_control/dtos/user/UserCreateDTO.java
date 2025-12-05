package com.bill.parking_control.dtos.user;

import com.bill.parking_control.persistences.entities.User.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserCreateDTO(
                @NotBlank @Email String email,
                @NotBlank String name,
                @NotBlank @Size(min = 6) String password,
                @NotNull Role role) {
}
