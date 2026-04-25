package com.example.smartdevelopergrowthplatform.controller;

import com.example.smartdevelopergrowthplatform.dto.ProgressResponseDTO;
import com.example.smartdevelopergrowthplatform.service.ProgressService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/progress")
@PreAuthorize("isAuthenticated()")
public class ProgressController {

    private final ProgressService progressService;

    public ProgressController(ProgressService progressService) {
        this.progressService = progressService;
    }

    @GetMapping
    public ResponseEntity<ProgressResponseDTO> getUserProgress(Authentication authentication) {
        String email = authentication.getName();
        ProgressResponseDTO progressResponseDTO = progressService.getProgressByEmail(email);
        return ResponseEntity.ok(progressResponseDTO);
    }
}
