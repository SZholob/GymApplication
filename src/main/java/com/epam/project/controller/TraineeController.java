package com.epam.project.controller;

import com.epam.project.dto.*;
import com.epam.project.model.Trainee;
import com.epam.project.service.TraineeService;
import com.epam.project.service.TrainerService;
import com.epam.project.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@Tag(name = "Trainee Management", description = "Endpoints for managing trainee profiles and their assigned trainers")
@RequestMapping("/api/trainees")
@RequiredArgsConstructor
public class TraineeController {

    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final UserService userService;

    @GetMapping("/{username}")
    @Operation(summary = "Get Trainee Profile", description = "Retrieves the profile information of a trainee by their username")
    public ResponseEntity<TraineeProfileResponse> getProfile(@PathVariable("username") String username) {
        Trainee trainee = traineeService.selectProfile(username);
        return ResponseEntity.ok(mapToResponse(trainee));
    }

    @PutMapping("/{username}")
    @Operation(summary = "Update Trainee Profile", description = "Updates the profile information of a trainee by their username")
    public ResponseEntity<TraineeProfileResponse> updateProfile(@PathVariable String username
            , @Valid @RequestBody UpdateTraineeRequest req) {

        if (!username.equals(req.username())) throw new IllegalArgumentException("Username mismatch");

        Trainee trainee = traineeService.selectProfile(username);
        trainee.getUser().setFirstName(req.firstName());
        trainee.getUser().setLastName(req.lastName());
        trainee.setDateOfBirth(req.dateOfBirth());
        trainee.setAddress(req.address());
        trainee.getUser().setIsActive(req.isActive());

        Trainee updated = traineeService.updateProfile(trainee);
        return ResponseEntity.ok(mapToResponse(updated));
    }

    @DeleteMapping("/{username}")
    @Operation(summary = "Delete Trainee Profile", description = "Deletes a trainee profile by their username")
    public ResponseEntity<String> deleteProfile(@PathVariable String username) {
        traineeService.deleteProfile(username);
        return ResponseEntity.ok("200 OK");
    }

    @PatchMapping("/{username}/status")
    @Operation(summary = "Toggle Trainee Activation", description = "Toggles the activation status of a trainee by their username")
    public ResponseEntity<String> toggleActivation(@PathVariable String username) {
        userService.toggleActivation(username);
        return ResponseEntity.ok("200 OK");
    }

    @GetMapping("/{username}/trainers/unassigned")
    @Operation(summary = "Get Unassigned Trainers", description = "Retrieves a list of active trainers that are not currently assigned to the trainee")
    public ResponseEntity<List<TrainerInfoResponse>> getUnassignedTrainers(@PathVariable String username) {
        List<TrainerInfoResponse> unassigned = trainerService.getUnassignedActiveTrainers(username).stream()
                .map(t -> new TrainerInfoResponse(t.getUser().getUsername(), t.getUser().getFirstName(), t.getUser().getLastName(), t.getSpecialization().getTrainingTypeName()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(unassigned);
    }

    @PutMapping("/{username}/trainers")
    @Operation(summary = "Update Trainee's Trainers", description = "Updates the list of trainers assigned to the trainee by their username")
    public ResponseEntity<List<TrainerInfoResponse>> updateTrainersList(@PathVariable String username, @RequestBody List<String> trainerUsernames) {
        traineeService.updateTraineeTrainersList(username, trainerUsernames);
        Trainee trainee = traineeService.selectProfile(username);
        return ResponseEntity.ok(mapToResponse(trainee).trainers());
    }

    private TraineeProfileResponse mapToResponse(Trainee trainee) {
        List<TrainerInfoResponse> trainers = trainee.getTrainers().stream()
                .map(t -> new TrainerInfoResponse(t.getUser().getUsername(), t.getUser().getFirstName(), t.getUser().getLastName(), t.getSpecialization().getTrainingTypeName()))
                .collect(Collectors.toList());

        return new TraineeProfileResponse(trainee.getUser().getFirstName(), trainee.getUser().getLastName(),
                trainee.getDateOfBirth(), trainee.getAddress(), trainee.getUser().getIsActive(), trainers);
    }
}