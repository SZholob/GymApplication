package com.epam.project.dao.impl;

import com.epam.project.dao.TrainingDao;
import com.epam.project.model.Training;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
@Transactional
@RequiredArgsConstructor
public class TrainingDaoImp implements TrainingDao {

    private static final Logger logger = LoggerFactory.getLogger(TrainingDaoImp.class);

    private final SessionFactory sessionFactory;

    @Override
    public Training save(Training training) {
        sessionFactory.getCurrentSession().persist(training);
        logger.info("Saved training: {}", training.getTrainingName());
        return training;
    }

    @Override
    public List<Training> findTraineeTrainingsByCriteria(String traineeUsername, LocalDate fromDate, LocalDate toDate, String trainerUsername, String trainingTypeName) {
        CriteriaBuilder cb = sessionFactory.getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<Training> cq = cb.createQuery(Training.class);
        Root<Training> root = cq.from(Training.class);
        List<Predicate> predicates = new ArrayList<>();

        predicates.add(cb.equal(root.get("trainee").get("user").get("username"), traineeUsername));

        if (fromDate != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("trainingDate"), fromDate));
        }
        if (toDate != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("trainingDate"), toDate));
        }
        if (trainerUsername != null && !trainerUsername.isEmpty()) {
            predicates.add(cb.equal(root.get("trainer").get("user").get("username"), trainerUsername));
        }
        if (trainingTypeName != null && !trainingTypeName.isEmpty()) {
            predicates.add(cb.equal(root.get("trainingType").get("trainingTypeName"), trainingTypeName));
        }

        cq.where(predicates.toArray(new Predicate[0]));

        return sessionFactory.getCurrentSession().createQuery(cq).getResultList();
    }

    @Override
    public List<Training> findTrainerTrainingsByCriteria(String trainerUsername, LocalDate fromDate, LocalDate toDate, String traineeUsername) {
        CriteriaBuilder cb = sessionFactory.getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<Training> cq = cb.createQuery(Training.class);
        Root<Training> root = cq.from(Training.class);
        List<Predicate> predicates = new ArrayList<>();

        predicates.add(cb.equal(root.get("trainer").get("user").get("username"), trainerUsername));

        if (fromDate != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("trainingDate"), fromDate));
        }
        if (toDate != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("trainingDate"), toDate));
        }
        if (traineeUsername != null && !traineeUsername.isEmpty()) {
            predicates.add(cb.equal(root.get("trainee").get("user").get("username"), traineeUsername));
        }

        cq.where(predicates.toArray(new Predicate[0]));

        return sessionFactory.getCurrentSession().createQuery(cq).getResultList();
    }
}