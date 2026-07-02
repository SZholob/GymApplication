package com.epam.project.dto;

import java.time.LocalDate;

public record TrainerTrainingInfoResponse(String trainingName, LocalDate trainingDate, String trainingType, Integer trainingDuration, String traineeName) {}
