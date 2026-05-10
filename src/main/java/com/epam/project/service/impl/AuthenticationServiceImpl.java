package com.epam.project.service.impl;

import com.epam.project.dao.UserDao;
import com.epam.project.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationServiceImpl.class);
    private final UserDao userDao;

    @Transactional
    @Override
    public boolean authenticate(String username, String password) {
        return userDao.findByUsername(username)
                .map(user -> {
                    boolean isAuthenticated = user.getPassword().equals(password);
                    if (isAuthenticated) {
                        logger.info("User '{}' authenticated successfully.", username);
                    } else {
                        logger.warn("Authentication failed for user '{}': Incorrect password.", username);
                    }
                    return isAuthenticated;
                })
                .orElseGet(() -> {
                    logger.warn("Authentication failed: User '{}' not found.", username);
                    return false;
                });
    }
}
