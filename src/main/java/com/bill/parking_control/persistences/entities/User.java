package com.bill.parking_control.persistences.entities;

import java.time.Instant;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Document(collection = "users")
public class User {
    @Id
    private String id;

    @Email
    @Indexed(unique = true)
    private String email;
    @NotBlank
    private String name;
    @Size(min = 6)
    private String password; // BCrypt

    private Role role; // ADMIN, OPERATOR, CLIENT

    @Builder.Default
    private boolean enabled = true;
    @Builder.Default
    @CreatedDate
    private Instant createdAt = Instant.now();
    @Builder.Default
    @LastModifiedDate
    private Instant lastModifiedAt = Instant.now();

    public enum Role {
        ADMIN, OPERATOR, CLIENT
    }
}
