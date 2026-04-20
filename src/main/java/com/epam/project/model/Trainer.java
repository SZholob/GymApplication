package com.epam.project.model;

import com.epam.project.model.enums.TrainingType;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Trainer extends User{

    private TrainingType specialization;

    public Trainer(Long id, String firstName, String lastName, String username, String password, Boolean isActive, TrainingType specialization) {
        super(id, firstName, lastName, username, password, isActive);
        this.specialization = specialization;
    }
}
