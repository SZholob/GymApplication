package com.epam.project.service;

import com.epam.project.dto.ActionType;
import com.epam.project.dto.WorkloadRequest;
import com.epam.project.model.TrainerWorkload;
import com.epam.project.repository.TrainerWorkloadRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TrainerWorkloadService {

    private final TrainerWorkloadRepository repository;

    @Transactional
    public void updateWorkload(WorkloadRequest request) {
        log.info("Отримано запит на оновлення годин для тренера: {}", request.getTrainerUsername());

        int year = request.getTrainingDate().getYear();
        int month = request.getTrainingDate().getMonthValue();

        Optional<TrainerWorkload> optionalWorkload = repository.findByUsernameAndYearAndMonth(
                request.getTrainerUsername(), year, month
        );

        if (request.getActionType() == ActionType.ADD) {
            if (optionalWorkload.isPresent()) {

                TrainerWorkload workload = optionalWorkload.get();
                workload.setTrainingSummaryDuration(workload.getTrainingSummaryDuration() + request.getTrainingDuration());
                repository.save(workload);
                log.info("Додано {} хвилин/годин існуючому запису.", request.getTrainingDuration());
            } else {

                TrainerWorkload workload = new TrainerWorkload();
                workload.setUsername(request.getTrainerUsername());
                workload.setFirstName(request.getTrainerFirstName());
                workload.setLastName(request.getTrainerLastName());
                workload.setIsActive(request.getIsActive());
                workload.setYear(year);
                workload.setMonth(month);
                workload.setTrainingSummaryDuration(request.getTrainingDuration());
                repository.save(workload);
                log.info("Створено новий запис навантаження на {}/{}", month, year);
            }
        } else if (request.getActionType() == ActionType.DELETE) {
            if (optionalWorkload.isPresent()) {

                TrainerWorkload workload = optionalWorkload.get();
                int newDuration = workload.getTrainingSummaryDuration() - request.getTrainingDuration();

                workload.setTrainingSummaryDuration(Math.max(0, newDuration));
                repository.save(workload);
                log.info("Віднято {} хвилин/годин із запису.", request.getTrainingDuration());
            } else {
                log.warn("Спроба видалити години для тренера {}, але запис за {}/{} не знайдено.",
                        request.getTrainerUsername(), month, year);
            }
        }
    }
}