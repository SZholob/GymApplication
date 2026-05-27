package com.epam.project.dto;

import java.util.List;

public record TrainerProfileResponse(String firstName, String lastName, String specialization, Boolean isActive, List<TraineeInfoResponse> trainees) {}
