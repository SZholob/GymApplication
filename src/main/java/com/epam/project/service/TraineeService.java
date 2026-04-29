package com.epam.project.service;

import com.epam.project.model.Trainee;

import java.util.Date;

public interface TraineeService {

    Trainee createProfile(String firstName, String lastName, Date dateOfBirth, String address);

    Trainee updateProfile(Trainee trainee);

    void deleteProfile(Long id);

    Trainee selectProfile(Long id);

}
