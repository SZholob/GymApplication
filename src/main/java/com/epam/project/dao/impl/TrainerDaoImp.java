package com.epam.project.dao.impl;

import com.epam.project.dao.TrainerDao;
import com.epam.project.model.Trainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class TrainerDaoImp implements TrainerDao {

    private static final Logger logger = LoggerFactory.getLogger(TrainerDaoImp.class);

    private Map<Long, Trainer> trainerStorage;

    @Autowired
    public void setTrainerStorage(Map<Long, Trainer> trainerStorage) {
        this.trainerStorage = trainerStorage;
    }

    public Trainer save(Trainer trainer) {
        if (trainer.getId() == null) {
            long maxId = trainerStorage.keySet()
                    .stream()
                    .max(Long::compare)
                    .orElse(0L);
            trainer.setId(maxId + 1);
        }
        trainerStorage.put(trainer.getId(), trainer);
        logger.info("Saved trainer with name {}. Details: {}",
                trainer.getUsername(), trainer.getUsername());
        return trainer;
    }

    public Trainer findById(Long id) {
        Trainer trainer = trainerStorage.get(id);
        Optional.ofNullable(trainer).ifPresentOrElse(
                t -> logger.debug("Found trainer with id {}, user name is: {}", id, t),
                () -> logger.debug("Trainee with id {} not found", id)
        );
        return trainer;
    }

    public List<Trainer> findAll() {
        List<Trainer> trainers = new ArrayList<>(trainerStorage.values());
        logger.debug("Found {} trainers", trainers.size());
        return trainers;
    }
}
