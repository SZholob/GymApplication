package com.epam.project.dto;
import java.util.List;

public record TrainerWorkloadResponse(
        String username,
        String firstName,
        String lastName,
        Boolean isActive,
        List<YearWorkload> years
) {}