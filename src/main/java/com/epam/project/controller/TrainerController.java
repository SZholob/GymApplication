package com.epam.project.controller;

import com.epam.project.dto.*;
import com.epam.project.model.Trainer;
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
@Tag(name = "Trainer Management", description = "Endpoints for managing trainer profiles and their assigned trainees")
@RequestMapping("/api/trainers")
@RequiredArgsConstructor
public class TrainerController {

    private final TrainerService trainerService;
    private final UserService userService;

    @GetMapping("/{username}")
    @Operation(summary = "Get Trainer Profile", description = "Retrieves the profile information of a trainer by their username")
    public ResponseEntity<TrainerProfileResponse> getProfile(@PathVariable String username) {
        Trainer trainer = trainerService.selectProfile(username);
        return ResponseEntity.ok(mapToResponse(trainer));
    }

    @PutMapping("/{username}")
    @Operation(summary = "Update Trainer Profile", description = "Updates the profile information of a trainer by their username")
    public ResponseEntity<TrainerProfileResponse> updateProfile(@PathVariable String username, @Valid @RequestBody UpdateTrainerRequest req) {
        Trainer trainer = trainerService.selectProfile(username);
        trainer.getUser().setFirstName(req.firstName());
        trainer.getUser().setLastName(req.lastName());
        trainer.getUser().setIsActive(req.isActive());

        Trainer updated = trainerService.updateProfile(trainer);
        return ResponseEntity.ok(mapToResponse(updated));
    }

    @PatchMapping("/{username}/status")
    @Operation(summary = "Toggle Trainer Activation", description = "Toggles the activation status of a trainer by their username")
    public ResponseEntity<String> toggleActivation(@PathVariable String username, @Valid @RequestBody ToggleActivationRequest req) {
        Trainer trainer = trainerService.selectProfile(username);
        if (!trainer.getUser().getIsActive().equals(req.isActive())) {
            userService.toggleActivation(username);
        }
        return ResponseEntity.ok("200 OK");
    }

    private TrainerProfileResponse mapToResponse(Trainer trainer) {
        List<TraineeInfoResponse> trainees = trainer.getTrainees().stream()
                .map(t -> new TraineeInfoResponse(t.getUser().getUsername(), t.getUser().getFirstName(), t.getUser().getLastName()))
                .collect(Collectors.toList());

        return new TrainerProfileResponse(trainer.getUser().getFirstName(), trainer.getUser().getLastName(),
                trainer.getSpecialization().getTrainingTypeName(), trainer.getUser().getIsActive(), trainees);
    }
}