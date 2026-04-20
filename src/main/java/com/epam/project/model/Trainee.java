package com.epam.project.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class Trainee extends User{

    private Date dateOfBirth;

    private String address;

    public Trainee(Long id, String firstName, String lastName, String username, String password, Boolean isActive, Date dateOfBirth, String address) {
        super(id, firstName, lastName, username, password, isActive);
        this.dateOfBirth = dateOfBirth;
        this.address = address;
    }
}
