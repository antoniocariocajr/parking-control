package com.bill.parking_control.services;

import com.bill.parking_control.dtos.UserCreateDTO;
import com.bill.parking_control.dtos.UserResponseDTO;
import com.bill.parking_control.persitenses.entities.User;

import java.util.List;

public interface UserService {
    UserResponseDTO createUser(UserCreateDTO dto);

    List<UserResponseDTO> getAllUsers();

    UserResponseDTO getUserById(String id);

    User getEntityById(String id); // Helper for other services
}
