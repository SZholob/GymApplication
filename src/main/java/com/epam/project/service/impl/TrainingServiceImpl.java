package com.epam.project.service.impl;

import com.epam.project.dao.TrainingDao;
import com.epam.project.model.Training;
import com.epam.project.service.TrainingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TrainingServiceImpl implements TrainingService {

    private static final Logger logger = LoggerFactory.getLogger(TrainingServiceImpl.class);

    private TrainingDao trainingDao;

    @Autowired
    public void setTrainingDao(TrainingDao trainingDao) {
        this.trainingDao = trainingDao;
    }

    public Training createTraining(Training training) {
        Training saved = trainingDao.save(training);
        logger.info("Created training with id {}. Details: {}",
                saved.getId(), saved);
        return saved;
    }

    public Training selectTraining(Long id) {
        Training training = trainingDao.findById(id);
        if (training != null) {
            logger.debug("Selected training with id {}. Details: {}",
                    id, training);
        }
        return training;
    }
}
