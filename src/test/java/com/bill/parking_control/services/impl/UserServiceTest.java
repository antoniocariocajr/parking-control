package com.bill.parking_control.services.impl;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import com.bill.parking_control.dtos.user.UserCreateDTO;
import com.bill.parking_control.dtos.user.UserResponseDTO;
import com.bill.parking_control.persistences.entities.User;
import com.bill.parking_control.persistences.entities.User.Role;
import com.bill.parking_control.persistences.repositories.UserRepository;
import com.bill.parking_control.services.mappers.UserMapper;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void createUser_Success() {
        UserCreateDTO dto = new UserCreateDTO("test@example.com", "Test User", "password123", Role.OPERATOR);
        User user = User.builder().email("test@example.com").build();
        UserResponseDTO responseDTO = new UserResponseDTO("user-id", "test@example.com", "Test User", Role.OPERATOR,
                true);

        when(userRepository.findByEmail(dto.email())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(dto.password())).thenReturn("encodedPassword");
        when(userMapper.toEntity(dto, "encodedPassword")).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toDTO(user)).thenReturn(responseDTO);

        UserResponseDTO result = userService.createUser(dto);

        assertNotNull(result);
        verify(userRepository).save(user);
    }

    @Test
    void createUser_EmailAlreadyExists() {
        UserCreateDTO dto = new UserCreateDTO("test@example.com", "Test User", "password123", Role.OPERATOR);

        when(userRepository.findByEmail(dto.email())).thenReturn(Optional.of(new User()));

        assertThrows(ResponseStatusException.class, () -> userService.createUser(dto));
    }
}
