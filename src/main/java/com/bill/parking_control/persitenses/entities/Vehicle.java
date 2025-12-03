package com.bill.parking_control.persitenses.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.NotBlank;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Document(collection = "vehicles")
public class Vehicle {
    @Id
    private String id;

    @NotBlank
    @Indexed(unique = true)
    private String licensePlate;

    @NotBlank
    private String brand;
    @NotBlank
    private String model;
    @NotBlank
    private String color;

    @DBRef
    private Client owner; // dono do veículo

    private VehicleType type; // CAR, MOTORCYCLE, TRUCK

    public enum VehicleType {

    }

}
