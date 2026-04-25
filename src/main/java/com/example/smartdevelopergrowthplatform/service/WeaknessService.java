package com.example.smartdevelopergrowthplatform.service;

import com.example.smartdevelopergrowthplatform.dto.WeaknessDetailDTO;

import java.util.Map;

public interface WeaknessService {
    Map<String, WeaknessDetailDTO> analyzeWeaknessByEmail(String email);

    Map<String, WeaknessDetailDTO> analyzeWeakness(Long userId);
}
