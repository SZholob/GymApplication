package com.epam.project.model;

import com.epam.project.model.enums.TrainingType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Training {
    private long id;

    private long trainerId;

    private long traineeId;

    private String trainingName;

    private TrainingType trainingType;

    private Date trainingDate;

    private int trainingDuration;
}
