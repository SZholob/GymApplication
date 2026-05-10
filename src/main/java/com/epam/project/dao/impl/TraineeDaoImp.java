package com.epam.project.dao.impl;

import com.epam.project.dao.TraineeDao;
import com.epam.project.model.Trainee;
import lombok.RequiredArgsConstructor;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
@Repository
@RequiredArgsConstructor
public class TraineeDaoImp implements TraineeDao {

    private static final Logger logger = LoggerFactory.getLogger(TraineeDaoImp.class);

    private static final String FIND_BY_USERNAME_QUERY = "FROM Trainee t JOIN FETCH t.user u WHERE u.username = :username";
    private static final String FIND_ALL_QUERY = "FROM Trainee";

    private final SessionFactory sessionFactory;

    @Override
    public Trainee save(Trainee trainee) {
        Trainee mergedTrainee = sessionFactory.getCurrentSession().merge(trainee);
        logger.info("Saved trainee with username: {}", mergedTrainee.getUser().getUsername());
        return mergedTrainee;
    }

    @Override
    public Optional<Trainee> findByUsername(String username) {
        return sessionFactory.getCurrentSession()
                .createQuery(FIND_BY_USERNAME_QUERY, Trainee.class)
                .setParameter("username", username)
                .uniqueResultOptional();
    }

    @Override
    public List<Trainee> findAll() {
        return sessionFactory.getCurrentSession()
                .createQuery(FIND_ALL_QUERY, Trainee.class)
                .getResultList();
    }

    @Override
    public void deleteByUsername(String username) {
        Optional<Trainee> traineeOpt = findByUsername(username);
        traineeOpt.ifPresentOrElse(
                trainee -> {
                    sessionFactory.getCurrentSession().remove(trainee);
                    logger.info("Deleted trainee with username: {}", username);
                },
                () -> logger.warn("Trainee with username {} not found for deletion", username)
        );
    }
}