package com.epam.project.dao;

import com.epam.project.model.Trainee;

import java.util.List;

public interface TraineeDao {

    Trainee save(Trainee trainee);

    Trainee findById(Long id);

    List<Trainee> findAll();

    void delete(Long id);

}