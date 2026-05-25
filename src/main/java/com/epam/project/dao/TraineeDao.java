package com.epam.project.dao;

import com.epam.project.model.Trainee;

import java.util.List;
import java.util.Optional;

public interface TraineeDao {

    Trainee save(Trainee trainee);

    Optional<Trainee> findByUsername(String username);

    List<Trainee> findAll();

    void deleteByUsername(String username);
}