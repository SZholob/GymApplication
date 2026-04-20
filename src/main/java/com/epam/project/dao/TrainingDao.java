package com.epam.project.dao;

import com.epam.project.model.Training;

import java.util.List;

public interface TrainingDao {
    Training save(Training training);

    Training findById(Long id);

    List<Training> findAll();
}
