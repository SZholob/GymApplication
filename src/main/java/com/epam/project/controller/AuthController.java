package com.epam.project.controller;

import com.epam.project.dto.RegistrationResponse;
import com.epam.project.dto.TraineeRegistrationRequest;
import com.epam.project.dto.TrainerRegistrationRequest;
import com.epam.project.model.Trainee;
import com.epam.project.model.Trainer;
import com.epam.project.service.AuthenticationService;
import com.epam.project.service.TraineeService;
import com.epam.project.service.TrainerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final AuthenticationService authenticationService;

    @PostMapping("/trainee/register")
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
    public ResponseEntity<RegistrationResponse> registerTrainer(@Valid @RequestBody TrainerRegistrationRequest request) {
        Trainer trainer = trainerService.createProfile(
                request.firstName(),
                request.lastName(),
                request.specialization()
        );
        return ResponseEntity.ok(new RegistrationResponse(trainer.getUser().getUsername(), trainer.getUser().getPassword()));
    }

    @GetMapping("/login")
    public ResponseEntity<String> login(@RequestParam String username, @RequestParam String password) {
        if (authenticationService.authenticate(username, password)) {
            return ResponseEntity.ok("200 OK");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
    }
}
