package com.example.smartdevelopergrowthplatform.service;

import com.example.smartdevelopergrowthplatform.dto.ProgressResponseDTO;

public interface ProgressService {
    ProgressResponseDTO getUserProgress(Long userId);
}
