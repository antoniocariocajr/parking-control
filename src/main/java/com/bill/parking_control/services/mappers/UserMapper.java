package com.bill.parking_control.services.mappers;

import org.springframework.stereotype.Component;

import com.bill.parking_control.dtos.user.UserCreateDTO;
import com.bill.parking_control.dtos.user.UserResponseDTO;
import com.bill.parking_control.persitenses.entities.User;

@Component
public class UserMapper {
    public UserResponseDTO toDTO(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.getRole(),
                user.isEnabled());
    }

    public User toEntity(UserCreateDTO dto, String passwordEncoded) {
        return User.builder()
                .email(dto.email())
                .name(dto.name())
                .password(passwordEncoded)
                .role(dto.role())
                .build();
    }
}
