package com.epam.project.service;

import com.epam.project.dto.*;
import com.epam.project.model.Month;
import com.epam.project.model.TrainerWorkload;
import com.epam.project.model.Year;
import com.epam.project.repository.TrainerWorkloadRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TrainerWorkloadService {

    private final TrainerWorkloadRepository repository;
    private static final String MDC_TRANSACTION_ID_KEY = "transactionId";

    public void updateWorkload(WorkloadRequest request) {
        String txId = MDC.get(MDC_TRANSACTION_ID_KEY);
        log.info("[TxID: {}] Processing workload event for trainer: {}", txId, request.trainerUsername()); //

        int requestYear = request.trainingDate().getYear();
        int requestMonth = request.trainingDate().getMonthValue();
        int duration = request.trainingDuration();


        Optional<TrainerWorkload> optionalWorkload = repository.findByUsername(request.trainerUsername());

        TrainerWorkload workload;

        if (optionalWorkload.isEmpty()) {

            log.info("[TxID: {}] Trainer not found. Creating new record.", txId);
            workload = TrainerWorkload.builder()
                    .username(request.trainerUsername())
                    .firstName(request.trainerFirstName())
                    .lastName(request.trainerLastName())
                    .isActive(request.isActive())
                    .years(new ArrayList<>())
                    .build();
        } else {
            workload = optionalWorkload.get();
        }


        Year yearWorkload = workload.getYears().stream()
                .filter(y -> y.getYear().equals(requestYear))
                .findFirst()
                .orElseGet(() -> {
                    Year newYear = new Year(requestYear, new ArrayList<>());
                    workload.getYears().add(newYear);
                    return newYear;
                });


        Month monthWorkload = yearWorkload.getMonths().stream()
                .filter(m -> m.getMonth().equals(requestMonth))
                .findFirst()
                .orElseGet(() -> {
                    Month newMonth = new Month(requestMonth, 0);
                    yearWorkload.getMonths().add(newMonth);
                    return newMonth;
                });


        int currentDuration = monthWorkload.getTrainingSummaryDuration();
        if (request.actionType() == ActionType.ADD) {
            monthWorkload.setTrainingSummaryDuration(currentDuration + duration);
        } else if (request.actionType() == ActionType.DELETE) {
            monthWorkload.setTrainingSummaryDuration(Math.max(0, currentDuration - duration));
        }


        repository.save(workload);
        log.info("[TxID: {}] Successfully updated and saved workload document for {}", txId, request.trainerUsername()); //
    }


    public TrainerWorkloadResponse getTrainerWorkload(String username) {
        log.info("Getting a load list for the trainer: {}", username);

        Optional<TrainerWorkload> optionalWorkload = repository.findByUsername(username);

        if (optionalWorkload.isEmpty()) {
            return new TrainerWorkloadResponse(username, "", "", false, List.of());
        }

        TrainerWorkload workload = optionalWorkload.get();

        List<YearWorkload> yearDtos = workload.getYears().stream()
                .map(modelYear -> {
                    List<MonthWorkload> monthDtos = modelYear.getMonths().stream()
                            .map(modelMonth -> new MonthWorkload(
                                    modelMonth.getMonth(),
                                    modelMonth.getTrainingSummaryDuration()))
                            .collect(Collectors.toList());

                    return new YearWorkload(modelYear.getYear(), monthDtos);
                })
                .collect(Collectors.toList());

        return new TrainerWorkloadResponse(
                workload.getUsername(),
                workload.getFirstName(),
                workload.getLastName(),
                workload.getIsActive(),
                yearDtos
        );
    }
}