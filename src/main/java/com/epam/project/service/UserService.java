package com.epam.project.service;

public interface UserService {
    void changePassword(String username, String newPassword);

    void toggleActivation(String username);
}
