package com.epam.project.dao;

import com.epam.project.model.Training;

import java.time.LocalDate;
import java.util.List;

public interface TrainingDao {
    Training save(Training training);

    List<Training> findTraineeTrainingsByCriteria(String traineeUsername, LocalDate fromDate, LocalDate toDate, String trainerUsername, String trainingTypeName);

    List<Training> findTrainerTrainingsByCriteria(String trainerUsername, LocalDate fromDate, LocalDate toDate, String traineeUsername);
}