package com.epam.project.dto;
import java.util.List;

public record YearWorkload(
        Integer year,
        List<MonthWorkload> months
) {}