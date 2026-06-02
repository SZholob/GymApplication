package com.epam.project.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record AddTrainingRequest(@NotBlank String traineeUsername, @NotBlank String trainerUsername, @NotBlank String trainingName, @NotNull LocalDate trainingDate, @NotNull Integer trainingDuration) {}

