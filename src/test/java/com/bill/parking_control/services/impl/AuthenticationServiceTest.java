package com.bill.parking_control.services.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.web.server.ResponseStatusException;

import com.bill.parking_control.dtos.Auth.LoginRequest;
import com.bill.parking_control.dtos.Auth.LoginResponse;
import com.bill.parking_control.persistences.entities.User;
import com.bill.parking_control.persistences.entities.User.Role;
import com.bill.parking_control.persistences.repositories.UserRepository;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private BCryptPasswordEncoder passwordEncoder;
    @Mock
    private JwtEncoder jwtEncoder;

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    @Test
    void authenticate_Success() {
        LoginRequest loginRequest = new LoginRequest("test@example.com", "password");
        User user = User.builder().id("user-id").email("test@example.com").password("encodedPassword")
                .role(Role.OPERATOR).build();
        Jwt jwt = org.mockito.Mockito.mock(Jwt.class);

        when(userRepository.findByEmail(loginRequest.email())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(loginRequest.password(), user.getPassword())).thenReturn(true);
        when(jwtEncoder.encode(any())).thenReturn(jwt);
        when(jwt.getTokenValue()).thenReturn("token");

        LoginResponse response = authenticationService.authenticate(loginRequest);

        assertNotNull(response);
        assertEquals("token", response.token());
    }

    @Test
    void authenticate_UserNotFound() {
        LoginRequest loginRequest = new LoginRequest("test@example.com", "password");

        when(userRepository.findByEmail(loginRequest.email())).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> authenticationService.authenticate(loginRequest));
    }

    @Test
    void authenticate_BadCredentials() {
        LoginRequest loginRequest = new LoginRequest("test@example.com", "wrongPassword");
        User user = User.builder().email("test@example.com").password("encodedPassword").build();

        when(userRepository.findByEmail(loginRequest.email())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(loginRequest.password(), user.getPassword())).thenReturn(false);

        assertThrows(BadCredentialsException.class, () -> authenticationService.authenticate(loginRequest));
    }
}
