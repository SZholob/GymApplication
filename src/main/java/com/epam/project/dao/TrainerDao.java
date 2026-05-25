package com.epam.project.dao;

import com.epam.project.model.Trainer;

import java.util.List;
import java.util.Optional;

public interface TrainerDao {

    Trainer save(Trainer trainer);

    Optional<Trainer> findByUsername(String username);

    List<Trainer> findAll();

    List<Trainer> findUnassignedActiveTrainers(String traineeUsername);
}
