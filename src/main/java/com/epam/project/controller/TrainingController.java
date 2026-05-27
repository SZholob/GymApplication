package com.epam.project.controller;

import com.epam.project.dao.TrainingTypeDao;
import com.epam.project.dto.*;
import com.epam.project.model.Training;
import com.epam.project.service.TraineeService;
import com.epam.project.service.TrainerService;
import com.epam.project.service.TrainingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Tag(name = "TrainingController", description = "Endpoints for managing trainings, including creation and retrieval of training information for trainees and trainers.")
@RequestMapping("/api/trainings")
@RequiredArgsConstructor
public class TrainingController {

    private final TrainingService trainingService;
    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final TrainingTypeDao trainingTypeDao;

    @PostMapping
    @Operation(summary = "Create a new training session", description = "Creates a new training session between a trainee and a trainer with the specified details.")
    public ResponseEntity<String> addTraining(@Valid @RequestBody AddTrainingRequest req) {
        trainingService.createTraining(req.traineeUsername(), req.trainerUsername(),
                req.trainingName(), req.trainingDate(), req.trainingDuration());
        return ResponseEntity.ok("200 OK");
    }

    @GetMapping("/trainee/{username}")
    @Operation(summary = "Get trainings for a trainee", description = "Retrieves a list of training sessions for a specific trainee, with optional filters for date range, trainer name, and training type.")
    public ResponseEntity<List<TraineeTrainingInfoResponse>> getTraineeTrainings(
            @PathVariable String username,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate periodFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate periodTo,
            @RequestParam(required = false) String trainerName,
            @RequestParam(required = false) String trainingType) {

        List<Training> trainings = traineeService.getTraineeTrainingsList(username, periodFrom, periodTo, trainerName, trainingType);
        List<TraineeTrainingInfoResponse> response = trainings.stream()
                .map(t -> new TraineeTrainingInfoResponse(t.getTrainingName(), t.getTrainingDate(), t.getTrainingType().getTrainingTypeName(), t.getTrainingDuration(), t.getTrainer().getUser().getUsername()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/trainer/{username}")
    @Operation(summary = "Get trainings for a trainer", description = "Retrieves a list of training sessions for a specific trainer, with optional filters for date range, trainee name, and training type.")
    public ResponseEntity<List<TrainerTrainingInfoResponse>> getTrainerTrainings(
            @PathVariable String username,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate periodFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate periodTo,
            @RequestParam(required = false) String traineeName) {

        List<Training> trainings = trainerService.getTrainerTrainingsList(username, periodFrom, periodTo, traineeName);
        List<TrainerTrainingInfoResponse> response = trainings.stream()
                .map(t -> new TrainerTrainingInfoResponse(t.getTrainingName(), t.getTrainingDate(), t.getTrainingType().getTrainingTypeName(), t.getTrainingDuration(), t.getTrainee().getUser().getUsername()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/types")
    @Operation(summary = "Get all training types", description = "Retrieves a list of all available training types.")
    public ResponseEntity<List<TrainingTypeResponse>> getTrainingTypes() {
        List<TrainingTypeResponse> types = trainingTypeDao.findAll().stream()
                .map(t -> new TrainingTypeResponse(t.getId(), t.getTrainingTypeName()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(types);
    }
}