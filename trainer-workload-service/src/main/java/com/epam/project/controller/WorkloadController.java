package com.epam.project.controller;

import com.epam.project.dto.TrainerWorkloadResponse;
import com.epam.project.service.TrainerWorkloadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/workloads")
@RequiredArgsConstructor
public class WorkloadController {

    private final TrainerWorkloadService trainerWorkloadService;

    @GetMapping("/{username}")
    public ResponseEntity<TrainerWorkloadResponse> getWorkload(@PathVariable String username) {
        TrainerWorkloadResponse response = trainerWorkloadService.getTrainerWorkload(username);
        return ResponseEntity.ok(response);
    }
}
