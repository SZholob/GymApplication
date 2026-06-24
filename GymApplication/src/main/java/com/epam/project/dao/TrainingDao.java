package com.epam.project.dao;

import com.epam.project.model.Training;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TrainingDao {
    Training save(Training training);

    List<Training> findTraineeTrainingsByCriteria(String traineeUsername, LocalDate fromDate, LocalDate toDate, String trainerUsername, String trainingTypeName);

    List<Training> findTrainerTrainingsByCriteria(String trainerUsername, LocalDate fromDate, LocalDate toDate, String traineeUsername);

    Optional<Training> findById(Long trainingId);

    void deleteById(Long trainingId);
}