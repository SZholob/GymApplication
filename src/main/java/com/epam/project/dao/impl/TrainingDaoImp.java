package com.epam.project.dao.impl;

import com.epam.project.dao.TrainingDao;
import com.epam.project.model.Training;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class TrainingDaoImp  implements TrainingDao {

    private static final Logger logger = LoggerFactory.getLogger(TrainingDaoImp.class);

    private Map<Long, Training> trainingStorage;

    @Autowired
    public void setTrainingStorage(Map<Long, Training> trainingStorage) {
        this.trainingStorage = trainingStorage;
    }

    private long idCounter = 1;

    public Training save(Training training) {
        if (training.getId() == 0) {
            training.setId(idCounter++);
        }
        trainingStorage.put(training.getId(), training);
        logger.info("Saved training with id {}. Details: {}",
                training.getId(), training);
        return training;
    }

    public Training findById(Long id) {
        Training training = trainingStorage.get(id);
        if (training != null) {
            logger.debug("Found training with id {}. Details: {}",
                    id, training);
        } else {
            logger.debug("Training with id {} not found", id);
        }
        return training;
    }

    public List<Training> findAll() {
        List<Training> trainings = new ArrayList<>(trainingStorage.values());
        logger.debug("Found {} trainings", trainings.size());
        return trainings;
    }
}
