package com.bill.parking_control.dtos;

import com.bill.parking_control.persitenses.entities.User.Role;

public record UserResponseDTO(
        String id,
        String email,
        String name,
        Role role,
        boolean enabled) {
}
