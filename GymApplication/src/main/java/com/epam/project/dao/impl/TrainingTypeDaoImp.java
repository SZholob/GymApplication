package com.epam.project.dao.impl;

import com.epam.project.dao.TrainingTypeDao;
import com.epam.project.model.TrainingType;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TrainingTypeDaoImp implements TrainingTypeDao {

    private final EntityManager entityManager;

    @Override
    public List<TrainingType> findAll() {
        return entityManager.unwrap(Session.class)
                .createQuery("FROM TrainingType", TrainingType.class)
                .getResultList();
    }

    @Override
    public Optional<TrainingType> findByTypeName(String typeName) {
        return entityManager.unwrap(Session.class)
                .createQuery("FROM TrainingType WHERE trainingTypeName = :typeName", TrainingType.class)
                .setParameter("typeName", typeName)
                .uniqueResultOptional();
    }

    @Override
    public TrainingType save(TrainingType trainingType) {
        return entityManager.unwrap(Session.class).merge(trainingType);
    }
}