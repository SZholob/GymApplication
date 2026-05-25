package com.epam.project.service;

import com.epam.project.model.Training;

import java.time.LocalDate;

public interface TrainingService {

    Training createTraining(String traineeUsername, String trainerUsername, String trainingName, LocalDate trainingDate, Integer trainingDuration);

}
