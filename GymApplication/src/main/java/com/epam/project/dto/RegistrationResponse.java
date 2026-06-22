package com.epam.project.dto;

public record RegistrationResponse(
        String username,
        String password,
        String token
) {}