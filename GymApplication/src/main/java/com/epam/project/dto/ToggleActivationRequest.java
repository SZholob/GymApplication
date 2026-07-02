package com.epam.project.dto;

import jakarta.validation.constraints.NotNull;

public record ToggleActivationRequest(@NotNull Boolean isActive) {}
