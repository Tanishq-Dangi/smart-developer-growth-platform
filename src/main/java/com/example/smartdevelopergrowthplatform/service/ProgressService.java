package com.example.smartdevelopergrowthplatform.service;

import com.example.smartdevelopergrowthplatform.dto.ProgressResponseDTO;

public interface ProgressService {
    ProgressResponseDTO getProgressByEmail(String email);

    ProgressResponseDTO getUserProgress(Long userId);
}
