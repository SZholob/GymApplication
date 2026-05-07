package com.epam.project.dao;
import com.epam.project.model.TrainingType;
import java.util.Optional;

public interface TrainingTypeDao {
    Optional<TrainingType> findByTypeName(String typeName);
    TrainingType save(TrainingType trainingType);
}