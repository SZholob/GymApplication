package com.epam.project.dao.impl;

import com.epam.project.dao.TraineeDao;
import com.epam.project.model.Trainee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class TraineeDaoImp implements TraineeDao {

    private static final Logger logger = LoggerFactory.getLogger(TraineeDaoImp.class);


    private Map<Long, Trainee> traineeStorage;

    @Autowired
    public void setTraineeStorage(Map<Long, Trainee> traineeStorage) {
        this.traineeStorage = traineeStorage;
    }

    private long idCounter = 1;

    public Trainee save(Trainee trainee) {
        if (trainee.getId() == null) {
            trainee.setId(idCounter++);
        }
        traineeStorage.put(trainee.getId(), trainee);
        logger.info("Saved trainee with name {}. Details: {}",
                trainee.getUsername(), trainee);
        return trainee;
    }

    public Trainee findById(Long id) {
        Trainee trainee = traineeStorage.get(id);
        if (trainee != null) {
            logger.debug("Found trainee with id {}, user name is: {}", id,  trainee.getUsername());
        } else {
            logger.debug("Trainee with id {} not found", id);
        }
        return trainee;
    }

    public List<Trainee> findAll() {
        List<Trainee> trainees = new ArrayList<>(traineeStorage.values());
        logger.debug("Found {} trainees", trainees.size());
        return trainees;
    }

    public void delete(Long id) {
        Trainee removed = traineeStorage.remove(id);
        if (removed != null) {
            logger.info("Deleted trainee with id {}, user name is: {}", id, removed.getUsername());
        } else {
            logger.warn("Trainee with id {} not found for deletion", id);
        }
    }
}
