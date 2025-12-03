package com.bill.parking_control.services;

import com.bill.parking_control.dtos.ParkingSessionResponseDTO;
import com.bill.parking_control.dtos.ParkingSessionStartDTO;
import com.bill.parking_control.persitenses.entities.User;

import java.util.List;

public interface ParkingSessionService {
    ParkingSessionResponseDTO startSession(ParkingSessionStartDTO dto, User operator);

    ParkingSessionResponseDTO endSession(String sessionId, User operator);

    List<ParkingSessionResponseDTO> getAllSessions();

    List<ParkingSessionResponseDTO> getActiveSessions();
}
