package com.bill.parking_control.dtos.user;

import com.bill.parking_control.persistences.entities.User.Role;

public record UserResponseDTO(
        String id,
        String email,
        String name,
        Role role,
        boolean enabled) {
}
