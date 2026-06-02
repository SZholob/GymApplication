package com.epam.project.dao;
import com.epam.project.dto.TrainingTypeResponse;
import com.epam.project.model.TrainingType;

import java.util.List;
import java.util.Optional;

public interface TrainingTypeDao {
    List<TrainingType> findAll();
    Optional<TrainingType> findByTypeName(String typeName);
    TrainingType save(TrainingType trainingType);
}