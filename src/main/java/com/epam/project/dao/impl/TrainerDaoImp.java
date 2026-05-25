package com.epam.project.dao.impl;

import com.epam.project.dao.TrainerDao;
import com.epam.project.model.Trainer;
import lombok.RequiredArgsConstructor;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
@Repository
@RequiredArgsConstructor
public class TrainerDaoImp implements TrainerDao {

    private static final Logger logger = LoggerFactory.getLogger(TrainerDaoImp.class);

    private static final String FIND_BY_USERNAME_QUERY = "FROM Trainer t JOIN FETCH t.user u WHERE u.username = :username";
    private static final String FIND_UNASSIGNED_ACTIVE_TRAINERS_QUERY = "SELECT t FROM Trainer t WHERE t.user.isActive = true AND t NOT IN " +
            "(SELECT tr FROM Trainee trainee JOIN trainee.trainers tr WHERE trainee.user.username = :username)";
    private static final String FIND_ALL_QUERY = "FROM Trainer";

    private final SessionFactory sessionFactory;

    public Trainer save(Trainer trainer) {
        Trainer mergedTrainer = sessionFactory.getCurrentSession().merge(trainer);
        logger.info("Saved trainer with username: {}", mergedTrainer.getUser().getUsername());
        return mergedTrainer;
    }

    public Optional<Trainer> findByUsername(String username) {
        return sessionFactory.getCurrentSession()
                .createQuery(FIND_BY_USERNAME_QUERY, Trainer.class)
                .setParameter("username", username)
                .uniqueResultOptional();
    }

    public List<Trainer> findAll() {
        return sessionFactory.getCurrentSession()
                .createQuery(FIND_ALL_QUERY, Trainer.class)
                .getResultList();
    }

    @Override
    public List<Trainer> findUnassignedActiveTrainers(String traineeUsername) {
        return sessionFactory.getCurrentSession()
                .createQuery(FIND_UNASSIGNED_ACTIVE_TRAINERS_QUERY, Trainer.class)
                .setParameter("username", traineeUsername)
                .getResultList();
    }
}
