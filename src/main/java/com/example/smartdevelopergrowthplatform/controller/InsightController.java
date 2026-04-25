package com.example.smartdevelopergrowthplatform.controller;

import com.example.smartdevelopergrowthplatform.dto.InsightResponseDTO;
import com.example.smartdevelopergrowthplatform.service.InsightService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/insight")
@PreAuthorize("isAuthenticated()")
public class InsightController {

    private final InsightService insightService;

    public InsightController(InsightService insightService) {
        this.insightService = insightService;
    }

    @GetMapping
    public ResponseEntity<InsightResponseDTO> getInsight(Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(insightService.getInsightByEmail(email));
    }
}
