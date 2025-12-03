package com.bill.parking_control.services;

import com.bill.parking_control.dtos.Auth.LoginRequest;
import com.bill.parking_control.dtos.Auth.LoginResponse;
import com.bill.parking_control.persitenses.entities.User;

public interface AuthenticationService {

    LoginResponse authenticate(LoginRequest request);

    LoginResponse encode(User user);

    boolean isLoginCorrect(LoginRequest loginRequest, String password);
}
