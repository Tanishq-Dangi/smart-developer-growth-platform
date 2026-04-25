package com.example.smartdevelopergrowthplatform.controller;

import com.example.smartdevelopergrowthplatform.dto.WeaknessDetailDTO;
import com.example.smartdevelopergrowthplatform.service.WeaknessService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/analysis")
@PreAuthorize("isAuthenticated()")
public class AnalysisController {

    private final WeaknessService weaknessService;

    public AnalysisController(WeaknessService weaknessService) {
        this.weaknessService = weaknessService;
    }

    @GetMapping("/weakness")
    public ResponseEntity<Map<String, WeaknessDetailDTO>> analyzeWeakness(Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(weaknessService.analyzeWeaknessByEmail(email));
    }
}
