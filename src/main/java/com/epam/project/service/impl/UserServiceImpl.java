package com.epam.project.service.impl;

import com.epam.project.dao.UserDao;
import com.epam.project.model.User;
import com.epam.project.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserDao userDao;

    @Override
    public void changePassword(String username, String newPassword) {
        User user = userDao.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + username));
        user.setPassword(newPassword);
        userDao.save(user);
        logger.info("Password changed successfully for user: {}", username);
    }

    @Override
    public void toggleActivation(String username) {
        User user = userDao.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + username));
        Boolean currentStatus = user.getIsActive();
        user.setIsActive(!currentStatus);
        userDao.save(user);
        logger.info("Activation toggled for user: {}. New status: {}", username, !currentStatus);
    }
}