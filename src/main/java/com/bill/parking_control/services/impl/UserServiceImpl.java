package com.bill.parking_control.services.impl;

import com.bill.parking_control.dtos.user.UserCreateDTO;
import com.bill.parking_control.dtos.user.UserResponseDTO;
import com.bill.parking_control.dtos.user.UserUpdateDto;
import com.bill.parking_control.persitenses.entities.User;
import com.bill.parking_control.persitenses.entities.User.Role;
import com.bill.parking_control.persitenses.repositories.UserRepository;
import com.bill.parking_control.services.UserService;
import com.bill.parking_control.services.mappers.UserMapper;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    public UserResponseDTO createUser(UserCreateDTO dto) {
        if (userRepository.findByEmail(dto.email()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already in use");
        }
        User user = userMapper.toEntity(dto, passwordEncoder.encode(dto.password()));
        user = userRepository.save(user);
        return userMapper.toDTO(user);
    }

    @Override
    public Page<UserResponseDTO> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable).map(userMapper::toDTO);
    }

    @Override
    public UserResponseDTO getUserById(String id) {
        return userMapper.toDTO(userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")));
    }

    @Override
    public UserResponseDTO getUserByEmail(String email) {
        return userMapper.toDTO(userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")));
    }

    @Override
    public UserResponseDTO updateUser(String id, UserUpdateDto dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        user.setName(dto.name());
        user.setPassword(passwordEncoder.encode(dto.password()));
        user = userRepository.save(user);
        return userMapper.toDTO(user);
    }

    @Override
    public UserResponseDTO updateUserRole(String id, Role role) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        user.setRole(role);
        user = userRepository.save(user);
        return userMapper.toDTO(user);
    }

    @Override
    public UserResponseDTO updateUserEnabled(String id, boolean enabled) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        user.setEnabled(enabled);
        user = userRepository.save(user);
        return userMapper.toDTO(user);
    }

    @Override
    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }
}
