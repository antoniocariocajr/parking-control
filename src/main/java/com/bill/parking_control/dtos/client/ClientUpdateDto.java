package com.bill.parking_control.dtos.client;

public record ClientUpdateDto(
        String name,
        String cpf,
        String email,
        String phone) {

}
