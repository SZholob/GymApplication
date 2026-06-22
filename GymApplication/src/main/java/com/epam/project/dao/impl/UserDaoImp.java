package com.epam.project.dao.impl;

import com.epam.project.dao.UserDao;
import com.epam.project.model.User;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserDaoImp implements UserDao {

    private final EntityManager entityManager;

    @Override
    public Optional<User> findByUsername(String username) {
        return entityManager.unwrap(Session.class)
                .createQuery("FROM User WHERE username = :username", User.class)
                .setParameter("username", username)
                .uniqueResultOptional();
    }

    @Override
    public User save(User user) {
        return entityManager.unwrap(Session.class).merge(user);
    }

    @Override
    public List<String> findUsernamesByPrefix(String prefix) {
        return entityManager.unwrap(Session.class)
                .createQuery("SELECT username FROM User WHERE username LIKE :prefix", String.class)
                .setParameter("prefix", prefix + "%")
                .getResultList();
    }
}