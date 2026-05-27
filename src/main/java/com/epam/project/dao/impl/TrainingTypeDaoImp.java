package com.epam.project.dao.impl;

import com.epam.project.dao.TrainingTypeDao;
import com.epam.project.model.TrainingType;
import lombok.RequiredArgsConstructor;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TrainingTypeDaoImp implements TrainingTypeDao {

    private final SessionFactory sessionFactory;

    @Override
    public List<TrainingType> findAll() {
        return sessionFactory.getCurrentSession()
                .createQuery("FROM TrainingType", TrainingType.class)
                .getResultList();
    }

    @Override
    public Optional<TrainingType> findByTypeName(String typeName) {
        return sessionFactory.getCurrentSession()
                .createQuery("FROM TrainingType WHERE trainingTypeName = :typeName", TrainingType.class)
                .setParameter("typeName", typeName)
                .uniqueResultOptional();
    }

    @Override
    public TrainingType save(TrainingType trainingType) {
        return sessionFactory.getCurrentSession().merge(trainingType);
    }
}