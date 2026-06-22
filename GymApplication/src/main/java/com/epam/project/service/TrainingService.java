package com.epam.project.service;

import com.epam.project.model.Training;
import com.epam.project.model.TrainingType;

import java.time.LocalDate;
import java.util.List;

public interface TrainingService {

    Training createTraining(String traineeUsername, String trainerUsername, String trainingName, LocalDate trainingDate, Integer trainingDuration);

    List<TrainingType> getTrainingTypes();

}
