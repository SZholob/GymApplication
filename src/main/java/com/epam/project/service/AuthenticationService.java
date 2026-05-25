package com.epam.project.service;

import org.springframework.transaction.annotation.Transactional;

public interface AuthenticationService {
    @Transactional
    boolean authenticate(String username, String password);
}
