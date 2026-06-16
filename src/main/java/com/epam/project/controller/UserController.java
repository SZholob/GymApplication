package com.epam.project.controller;

import com.epam.project.dto.ChangePasswordRequest;
import com.epam.project.service.AuthenticationService;
import com.epam.project.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@Tag(name = "UserController", description = "Endpoints for user-related operations, including password management and authentication.")
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final AuthenticationService authenticationService;

    @PutMapping("/password")
    @Operation(summary = "Change user password", description = "Allows a user to change their password by providing their username, old password, and new password.")
    public ResponseEntity<String> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        if (authenticationService.authenticate(request.username(), request.oldPassword()) == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid old password");
        }
        userService.changePassword(request.username(), request.newPassword());
        return ResponseEntity.ok("200 OK");
    }
}
