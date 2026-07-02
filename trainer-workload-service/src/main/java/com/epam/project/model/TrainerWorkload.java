package com.epam.project.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "trainer_workloads")
@Data
@NoArgsConstructor
public class TrainerWorkload {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private Boolean isActive;

    @Column(name = "workload_year", nullable = false)
    private Integer year;

    @Column(name = "workload_month", nullable = false)
    private Integer month;

    @Column(nullable = false)
    private Integer trainingSummaryDuration;
}