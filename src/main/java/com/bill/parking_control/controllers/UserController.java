package com.bill.parking_control.controllers;

import com.bill.parking_control.dtos.user.UserCreateDTO;
import com.bill.parking_control.dtos.user.UserResponseDTO;
import com.bill.parking_control.dtos.user.UserUpdateDto;
import com.bill.parking_control.persitenses.entities.User.Role;
import com.bill.parking_control.services.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "Users management")
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    private final UserService userService;

    @PostMapping
    @Operation(summary = "Create a new user", description = "Create a new user")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User created"),
            @ApiResponse(responseCode = "401", description = "Not authenticated"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public UserResponseDTO createUser(@RequestBody @Valid UserCreateDTO dto) {
        return userService.createUser(dto);
    }

    @GetMapping
    @Operation(summary = "Get all users", description = "Get all users")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Users found"),
            @ApiResponse(responseCode = "401", description = "Not authenticated"),
            @ApiResponse(responseCode = "404", description = "Users not found")
    })
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('SCOPE_OPERATOR')")
    public Page<UserResponseDTO> getAllUsers(Pageable pageable) {
        return userService.getAllUsers(pageable);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user by id", description = "Get user by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User found"),
            @ApiResponse(responseCode = "401", description = "Not authenticated"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('SCOPE_OPERATOR')")
    public UserResponseDTO getUserById(@PathVariable String id) {
        return userService.getUserById(id);
    }

    @GetMapping("/email/{email}")
    @Operation(summary = "Get user by email", description = "Get user by email")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User found"),
            @ApiResponse(responseCode = "401", description = "Not authenticated"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('SCOPE_OPERATOR')")
    public UserResponseDTO getUserByEmail(@PathVariable String email) {
        return userService.getUserByEmail(email);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update user", description = "Update user")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User updated"),
            @ApiResponse(responseCode = "401", description = "Not authenticated"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public UserResponseDTO updateUser(@PathVariable String id, @RequestBody @Valid UserUpdateDto dto) {
        return userService.updateUser(id, dto);
    }

    @PutMapping("/{id}/role")
    @Operation(summary = "Update user role", description = "Update user role")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User role updated"),
            @ApiResponse(responseCode = "401", description = "Not authenticated"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public UserResponseDTO updateUserRole(@PathVariable String id, @RequestBody @Valid Role role) {
        return userService.updateUserRole(id, role);
    }

    @PutMapping("/{id}/enabled")
    @Operation(summary = "Update user enabled", description = "Update user enabled")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User enabled updated"),
            @ApiResponse(responseCode = "401", description = "Not authenticated"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public UserResponseDTO updateUserEnabled(@PathVariable String id,
            @RequestBody @Valid boolean enabled) {
        return userService.updateUserEnabled(id, enabled);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user", description = "Delete user")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "User deleted"),
            @ApiResponse(responseCode = "401", description = "Not authenticated"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public void deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
    }
}
