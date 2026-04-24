package com.example.smartdevelopergrowthplatform.controller;

import com.example.smartdevelopergrowthplatform.dto.ProgressResponseDTO;
import com.example.smartdevelopergrowthplatform.service.ProgressService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/progress")
public class ProgressController {

    private final ProgressService progressService;

    public ProgressController(ProgressService progressService) {
        this.progressService = progressService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ProgressResponseDTO> getUserProgress(@PathVariable Long userId) {
        ProgressResponseDTO progressResponseDTO = progressService.getUserProgress(userId);
        return ResponseEntity.ok(progressResponseDTO);
    }
}
