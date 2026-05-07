package com.epam.project.dao.impl;

import com.epam.project.dao.UserDao;
import com.epam.project.model.User;
import lombok.RequiredArgsConstructor;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserDaoImp implements UserDao {

    private final SessionFactory sessionFactory;

    @Override
    public Optional<User> findByUsername(String username) {
        return sessionFactory.getCurrentSession()
                .createQuery("FROM User WHERE username = :username", User.class)
                .setParameter("username", username)
                .uniqueResultOptional();
    }

    @Override
    public User save(User user) {
        sessionFactory.getCurrentSession().persist(user);
        return user;
    }
}