package com.epam.project.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateTrainerRequest(@NotBlank String username, @NotBlank String firstName, @NotBlank String lastName, @NotBlank String specialization, @NotNull Boolean isActive) {}
