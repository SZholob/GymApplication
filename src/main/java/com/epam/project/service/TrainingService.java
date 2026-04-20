package com.epam.project.service;

import com.epam.project.model.Training;

public interface TrainingService {

    Training createTraining(Training training);

    Training selectTraining(Long id);
}
