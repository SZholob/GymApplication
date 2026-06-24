package com.epam.project.service;

import com.epam.project.dto.*;
import com.epam.project.model.TrainerWorkload;
import com.epam.project.repository.TrainerWorkloadRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TrainerWorkloadService {

    private final TrainerWorkloadRepository repository;

    @Transactional
    public void updateWorkload(WorkloadRequest request) {
        log.info("Received a request to update hours for the trainer: {}", request.trainerUsername());

        int year = request.trainingDate().getYear();
        int month = request.trainingDate().getMonthValue();

        Optional<TrainerWorkload> optionalWorkload = repository.findByUsernameAndYearAndMonth(
                request.trainerUsername(), year, month
        );

        if (request.actionType() == ActionType.ADD) {
            if (optionalWorkload.isPresent()) {

                TrainerWorkload workload = optionalWorkload.get();
                workload.setTrainingSummaryDuration(workload.getTrainingSummaryDuration() + request.trainingDuration());
                repository.save(workload);
                log.info("Added {} minutes/hours to existing entry.", request.trainingDuration());
            } else {

                TrainerWorkload workload = new TrainerWorkload();
                workload.setUsername(request.trainerUsername());
                workload.setFirstName(request.trainerFirstName());
                workload.setLastName(request.trainerLastName());
                workload.setIsActive(request.isActive());
                workload.setYear(year);
                workload.setMonth(month);
                workload.setTrainingSummaryDuration(request.trainingDuration());
                repository.save(workload);
                log.info("New load record created on {}/{}", month, year);
            }
        } else if (request.actionType() == ActionType.DELETE) {
            if (optionalWorkload.isPresent()) {

                TrainerWorkload workload = optionalWorkload.get();
                int newDuration = workload.getTrainingSummaryDuration() - request.trainingDuration();

                workload.setTrainingSummaryDuration(Math.max(0, newDuration));
                repository.save(workload);
                log.info("{} minutes/hours have been subtracted from the recording.", request.trainingDuration());
            } else {
                log.warn("Attempting to delete hours for trainer {}, but no record found for {}/{}.",
                        request.trainerUsername(), month, year);
            }
        }
    }
    public TrainerWorkloadResponse getTrainerWorkload(String username) {
        log.info("Отримання списку навантаження для тренера: {}", username);

        List<TrainerWorkload> workloads = repository.findAllByUsername(username);

        if (workloads.isEmpty()) {
            return new TrainerWorkloadResponse(username, "", "", false, List.of());
        }

        TrainerWorkload firstRecord = workloads.get(0);


        Map<Integer, List<TrainerWorkload>> byYear = workloads.stream()
                .collect(Collectors.groupingBy(TrainerWorkload::getYear));


        List<YearWorkload> yearsList = byYear.entrySet().stream()
                .map(yearEntry -> {
                    List<MonthWorkload> months = yearEntry.getValue().stream()
                            .map(w -> new MonthWorkload(w.getMonth(), w.getTrainingSummaryDuration()))
                            .collect(Collectors.toList());
                    return new YearWorkload(yearEntry.getKey(), months);
                })
                .collect(Collectors.toList());

        return new TrainerWorkloadResponse(
                firstRecord.getUsername(),
                firstRecord.getFirstName(),
                firstRecord.getLastName(),
                firstRecord.getIsActive(),
                yearsList
        );
    }
}