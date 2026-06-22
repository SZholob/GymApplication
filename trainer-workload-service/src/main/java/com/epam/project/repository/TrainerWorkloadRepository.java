package com.epam.project.repository;

import com.epam.project.model.TrainerWorkload;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface TrainerWorkloadRepository extends JpaRepository<TrainerWorkload, Long> {
    Optional<TrainerWorkload> findByUsernameAndYearAndMonth(String username, Integer year, Integer month);

    List<TrainerWorkload> findAllByUsername(String username);
}
