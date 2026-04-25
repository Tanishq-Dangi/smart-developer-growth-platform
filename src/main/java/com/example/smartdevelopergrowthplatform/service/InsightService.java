package com.example.smartdevelopergrowthplatform.service;

import com.example.smartdevelopergrowthplatform.dto.InsightResponseDTO;

public interface InsightService {
    String generateInsightByEmail(String email);

    InsightResponseDTO getInsightByEmail(String email);

    String generateInsight(Long userId);

    InsightResponseDTO getInsight(Long userId);
}
