package com.vsolabs.syngular.auth.controller;

import com.vsolabs.syngular.auth.dto.UserRegisterRequest;
import com.vsolabs.syngular.auth.dto.UserResponse;
import com.vsolabs.syngular.auth.model.User;
import com.vsolabs.syngular.auth.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody UserRegisterRequest request) {

        if (userService.existsByEmail(request.getEmail())) {
            throw new RuntimeException("El correo ya está registrado en el sistema.");
        }

        // Mapea DTO a Entidad
        User newUser = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .passwordHash(request.getPassword())
                .build();

        User savedUser = userService.registerNewUser(newUser, request.getRoleName());

        // Mapea Entidad a DTO de respuesta
        UserResponse response = UserResponse.builder()
                .id(savedUser.getId())
                .username(savedUser.getUsername())
                .email(savedUser.getEmail())
                .roleName(savedUser.getRole().getName())
                .isActive(savedUser.getIsActive())
                .build();
        
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}