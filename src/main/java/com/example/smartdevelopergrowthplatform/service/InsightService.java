package com.example.smartdevelopergrowthplatform.service;

import com.example.smartdevelopergrowthplatform.dto.InsightResponseDTO;

public interface InsightService {
    String generateInsight(Long userId);

    InsightResponseDTO getInsight(Long userId);
}
