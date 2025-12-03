package com.bill.parking_control.services.impl;

import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.bill.parking_control.dtos.Auth.LoginRequest;
import com.bill.parking_control.dtos.Auth.LoginResponse;
import com.bill.parking_control.persitenses.entities.User;
import com.bill.parking_control.persitenses.repositories.UserRepository;
import com.bill.parking_control.services.AuthenticationService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtEncoder jwtEncoder;

    @Override
    public LoginResponse authenticate(LoginRequest loginRequest) {
        var user = userRepository.findByEmail(loginRequest.email())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (isLoginCorrect(loginRequest, user.getPassword())) {
            return encode(user);
        }
        throw new BadCredentialsException("user or password is invalid!");
    }

    @Override
    public LoginResponse encode(User user) {
        var now = Instant.now();
        var expiresIn = 600L;
        var scopes = user.getRole();
        var claims = JwtClaimsSet.builder()
                .issuer("cucumber")
                .subject(user.getId().toString())
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiresIn))
                .claim("scope", scopes)
                .build();
        var jwtValue = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

        return new LoginResponse(jwtValue);
    }

    @Override
    public boolean isLoginCorrect(LoginRequest loginRequest, String password) {
        return passwordEncoder.matches(loginRequest.password(), password);
    }
}
