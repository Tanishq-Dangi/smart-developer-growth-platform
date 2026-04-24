package com.example.smartdevelopergrowthplatform.controller;

import com.example.smartdevelopergrowthplatform.dto.InsightResponseDTO;
import com.example.smartdevelopergrowthplatform.service.InsightService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/insight")
public class InsightController {

    private final InsightService insightService;

    public InsightController(InsightService insightService) {
        this.insightService = insightService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<InsightResponseDTO> getInsight(@PathVariable Long userId) {
        return ResponseEntity.ok(insightService.getInsight(userId));
    }
}
