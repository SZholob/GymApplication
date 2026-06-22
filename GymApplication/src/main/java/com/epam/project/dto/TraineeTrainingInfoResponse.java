package com.epam.project.dto;

import java.time.LocalDate;

public record TraineeTrainingInfoResponse(String trainingName, LocalDate trainingDate, String trainingType, Integer trainingDuration, String trainerName) {}
