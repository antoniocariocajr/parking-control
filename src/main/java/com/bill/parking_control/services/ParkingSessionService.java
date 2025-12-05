package com.bill.parking_control.services;

import com.bill.parking_control.dtos.session.ParkingSessionResponseDTO;
import com.bill.parking_control.dtos.session.ParkingSessionStartDTO;
import com.bill.parking_control.dtos.session.ParkingSessionUpdateDto;
import com.bill.parking_control.persitenses.entities.ParkingSession.SessionStatus;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ParkingSessionService {
    ParkingSessionResponseDTO startSession(ParkingSessionStartDTO dto);

    ParkingSessionResponseDTO updateSession(String sessionId, ParkingSessionUpdateDto dto);

    ParkingSessionResponseDTO getSessionById(String sessionId);

    Page<ParkingSessionResponseDTO> getAllSessions(Pageable pageable);

    Page<ParkingSessionResponseDTO> getActiveSessions(Pageable pageable, SessionStatus status);

    void cancelSession(String sessionId);
}
