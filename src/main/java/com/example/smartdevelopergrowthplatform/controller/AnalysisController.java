package com.example.smartdevelopergrowthplatform.controller;

import com.example.smartdevelopergrowthplatform.dto.WeaknessDetailDTO;
import com.example.smartdevelopergrowthplatform.service.WeaknessService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/analysis")
public class AnalysisController {

    private final WeaknessService weaknessService;

    public AnalysisController(WeaknessService weaknessService) {
        this.weaknessService = weaknessService;
    }

    @GetMapping("/weakness/{userId}")
    public ResponseEntity<Map<String, WeaknessDetailDTO>> analyzeWeakness(@PathVariable Long userId) {
        return ResponseEntity.ok(weaknessService.analyzeWeakness(userId));
    }
}
