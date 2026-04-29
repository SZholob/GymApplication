package com.epam.project.dao;

import com.epam.project.model.Trainer;

import java.util.List;

public interface TrainerDao {
    Trainer save(Trainer trainer);

    Trainer findById(Long id);

    List<Trainer> findAll();
}
