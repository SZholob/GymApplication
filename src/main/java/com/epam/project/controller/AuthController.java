package com.epam.project.controller;

import com.epam.project.dto.RegistrationResponse;
import com.epam.project.dto.TraineeRegistrationRequest;
import com.epam.project.dto.TrainerRegistrationRequest;
import com.epam.project.model.Trainee;
import com.epam.project.model.Trainer;
import com.epam.project.service.AuthenticationService;
import com.epam.project.service.TraineeService;
import com.epam.project.service.TrainerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@Tag(name = "Authentication", description = "Endpoints for login and registration")
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final AuthenticationService authenticationService;

    @PostMapping("/trainee/register")
    @Operation(summary = "Register a new Trainee", description = "Creates a new trainee profile and generates credentials")
    public ResponseEntity<RegistrationResponse> registerTrainee(@Valid @RequestBody TraineeRegistrationRequest request) {
        Trainee trainee = traineeService.createProfile(
                request.firstName(),
                request.lastName(),
                request.dateOfBirth(),
                request.address()
        );
        return ResponseEntity.ok(new RegistrationResponse(trainee.getUser().getUsername(), trainee.getUser().getPassword()));
    }

    @PostMapping("/trainer/register")
    @Operation(summary = "Register a new Trainer", description = "Creates a new trainer profile and generates credentials")
    public ResponseEntity<RegistrationResponse> registerTrainer(@Valid @RequestBody TrainerRegistrationRequest request) {
        Trainer trainer = trainerService.createProfile(
                request.firstName(),
                request.lastName(),
                request.specialization()
        );
        return ResponseEntity.ok(new RegistrationResponse(trainer.getUser().getUsername(), trainer.getUser().getPassword()));
    }

    @GetMapping("/login")
    @Operation(summary = "Login", description = "Authenticates a user and returns a JWT Bearer token")
    public ResponseEntity<Map<String, String>> login(
            @RequestParam("username") String username,
            @RequestParam("password") String password) {

        String jwtToken = authenticationService.authenticate(username, password);
        return ResponseEntity.ok(Map.of("token", jwtToken));
    }
}
