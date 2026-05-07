package com.epam.project.dao;

import com.epam.project.model.User;
import java.util.Optional;

public interface UserDao {
    Optional<User> findByUsername(String username);
    User save(User user);
}