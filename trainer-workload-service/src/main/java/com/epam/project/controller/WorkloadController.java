package com.epam.project.controller;

import com.epam.project.dto.WorkloadRequest;
import com.epam.project.service.TrainerWorkloadService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/workloads")
@RequiredArgsConstructor
public class WorkloadController {

    private final TrainerWorkloadService trainerWorkloadService;

    @PostMapping
    public ResponseEntity<Void> updateWorkload(@Valid @RequestBody WorkloadRequest request) {
        trainerWorkloadService.updateWorkload(request);
        return ResponseEntity.ok().build();
    }
}
