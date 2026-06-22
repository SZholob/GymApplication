package com.epam.project.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record UpdateTraineeRequest(@NotBlank String username, @NotBlank String firstName, @NotBlank String lastName, LocalDate dateOfBirth, String address, @NotNull Boolean isActive) {}
