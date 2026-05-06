package com.epam.project.model;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "trainings")
public class Training {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "trainee_id", nullable = false)
    private Trainee trainee;

    @ManyToOne
    @JoinColumn(name = "trainer_id", nullable = false)
    private Trainer trainer;

    @Column(name = "training_name", nullable = false)
    private String trainingName;

    @ManyToOne
    @JoinColumn(name = "training_type_id", nullable = false)
    private TrainingType trainingType;

    @Column(name = "training_date", nullable = false)
    private LocalDate trainingDate;

    @Column(name = "training_duration", nullable = false)
    private Integer trainingDuration;

    @Override
    public String toString() {
        return "\n Training [Id = " + id
                + "\n TrainerId = " + trainer.getId()
                + "\n TraineeId = " + trainee.getId()
                + "\n TrainingName= " + trainingName
                + "\n TrainingType= " + trainingType
                + "\n TrainingDate= " + trainingDate
                + "\n TrainingDuration= " + trainingDuration
                + "]\n";
    }
}
