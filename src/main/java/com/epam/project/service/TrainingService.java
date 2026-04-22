package com.epam.project.service;

import com.epam.project.model.Training;
import com.epam.project.model.enums.TrainingType;

import java.util.Date;

public interface TrainingService {

    Training createTraining(Long trainerId, Long traineeId, String trainingName, TrainingType trainingType, Date trainingDate, int trainingDuration);

    Training selectTraining(Long id);
}
