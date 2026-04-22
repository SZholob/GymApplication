package com.epam.project.service.impl;

import com.epam.project.dao.TrainingDao;
import com.epam.project.model.Training;
import com.epam.project.model.enums.TrainingType;
import com.epam.project.service.TrainingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TrainingServiceImpl implements TrainingService {

    private static final Logger logger = LoggerFactory.getLogger(TrainingServiceImpl.class);

    private TrainingDao trainingDao;

    @Autowired
    public void setTrainingDao(TrainingDao trainingDao) {
        this.trainingDao = trainingDao;
    }

    @Override
    public Training createTraining(Long trainerId, Long traineeId, String trainingName, TrainingType trainingType, Date trainingDate, int trainingDuration) {
        Training training = new Training();
        training.setTrainerId(trainerId);
        training.setTraineeId(traineeId);
        training.setTrainingName(trainingName);
        training.setTrainingType(trainingType);
        training.setTrainingDate(trainingDate);
        training.setTrainingDuration(trainingDuration);

        Training savedTraining = trainingDao.save(training);
        logger.debug("Created new training with id {}. Details: {}",
                savedTraining.getId(), savedTraining);
        return savedTraining;
    }

    @Override
    public Training selectTraining(Long id) {
        Training training = trainingDao.findById(id);
        if (training != null) {
            logger.debug("Selected training with id {}. Details: {}",
                    id, training);
        }
        return training;
    }
}
