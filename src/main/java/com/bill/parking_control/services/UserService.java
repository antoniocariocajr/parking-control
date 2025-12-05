package com.bill.parking_control.services;

import com.bill.parking_control.dtos.user.UserCreateDTO;
import com.bill.parking_control.dtos.user.UserResponseDTO;
import com.bill.parking_control.dtos.user.UserUpdateDto;
import com.bill.parking_control.persistences.entities.User.Role;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    UserResponseDTO createUser(UserCreateDTO dto);

    Page<UserResponseDTO> getAllUsers(Pageable pageable);

    UserResponseDTO getUserById(String id);

    UserResponseDTO getUserByEmail(String email);

    UserResponseDTO updateUser(String id, UserUpdateDto dto);

    UserResponseDTO updateUserRole(String id, Role role);

    UserResponseDTO updateUserEnabled(String id, boolean enabled);

    void deleteUser(String id);
}
