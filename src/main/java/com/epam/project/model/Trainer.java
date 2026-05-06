package com.epam.project.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Trainer{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "specialization_id", nullable = false)
    private TrainingType specialization;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToMany(mappedBy = "trainers", fetch = FetchType.LAZY)
    private List<Trainee> trainees;

    @OneToMany(mappedBy = "trainer")
    private List<Training> trainings;

    @Override
    public String toString() {
        return "\n Trainer [UserId = " + user.getId()
                + "\n FirstName = " + user.getFirstName()
                + "\n LastName = " + user.getLastName()
                + "\n UserName= " + user.getUsername()
                + "\n specialization= " + specialization
                + "]\n";
    }
}
