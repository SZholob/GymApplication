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

    private Long id;

    private Long trainerId;

    private Long traineeId;

    private String trainingName;

    private TrainingType trainingType;

    private Date trainingDate;

    private int trainingDuration;

    @Override
    public String toString() {
        return "\n Training [Id = " + id
                + "\n TrainerId = " + trainerId
                + "\n TraineeId = " + traineeId
                + "\n TrainingName= " + trainingName
                + "\n TrainingType= " + trainingType
                + "\n TrainingDate= " + trainingDate
                + "\n TrainingDuration= " + trainingDuration
                + "]\n";
    }
}
