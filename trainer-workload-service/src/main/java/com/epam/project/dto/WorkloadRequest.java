package com.epam.project.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class WorkloadRequest {

    @NotBlank
    private String trainerUsername;

    @NotBlank
    private String trainerFirstName;

    @NotBlank
    private String trainerLastName;

    @NotNull
    private Boolean isActive;

    @NotNull
    private LocalDate trainingDate;

    @NotNull
    private Integer trainingDuration;

    @NotNull
    private ActionType actionType;
}