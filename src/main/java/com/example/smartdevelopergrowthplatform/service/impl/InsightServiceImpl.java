package com.example.smartdevelopergrowthplatform.service.impl;

import com.example.smartdevelopergrowthplatform.dto.InsightResponseDTO;
import com.example.smartdevelopergrowthplatform.dto.ProgressResponseDTO;
import com.example.smartdevelopergrowthplatform.dto.WeaknessDetailDTO;
import com.example.smartdevelopergrowthplatform.entity.User;
import com.example.smartdevelopergrowthplatform.exception.ResourceNotFoundException;
import com.example.smartdevelopergrowthplatform.repository.UserRepository;
import com.example.smartdevelopergrowthplatform.service.InsightService;
import com.example.smartdevelopergrowthplatform.service.ProgressService;
import com.example.smartdevelopergrowthplatform.service.WeaknessService;
import com.example.smartdevelopergrowthplatform.service.insight.InsightMessageGenerator;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class InsightServiceImpl implements InsightService {

    private final ProgressService progressService;
    private final WeaknessService weaknessService;
    private final InsightMessageGenerator insightMessageGenerator;
    private final UserRepository userRepository;

    public InsightServiceImpl(
            ProgressService progressService,
            WeaknessService weaknessService,
            InsightMessageGenerator insightMessageGenerator,
            UserRepository userRepository
    ) {
        this.progressService = progressService;
        this.weaknessService = weaknessService;
        this.insightMessageGenerator = insightMessageGenerator;
        this.userRepository = userRepository;
    }

    @Override
    public String generateInsightByEmail(String email) {
        User user = findUserByEmail(email);
        return generateInsight(user.getId());
    }

    @Override
    public InsightResponseDTO getInsightByEmail(String email) {
        User user = findUserByEmail(email);
        return getInsight(user.getId());
    }

    @Override
    public String generateInsight(Long userId) {
        ProgressResponseDTO progress = progressService.getUserProgress(userId);
        List<String> weakAreas = getWeakAreas(weaknessService.analyzeWeakness(userId));
        return insightMessageGenerator.generateMessage(progress.getCompletionPercentage(), weakAreas);
    }

    @Override
    public InsightResponseDTO getInsight(Long userId) {
        ProgressResponseDTO progress = progressService.getUserProgress(userId);
        Map<String, WeaknessDetailDTO> weaknessData = weaknessService.analyzeWeakness(userId);
        List<String> weakAreas = getWeakAreas(weaknessData);
        String insight = insightMessageGenerator.generateMessage(progress.getCompletionPercentage(), weakAreas);

        return new InsightResponseDTO(
                insight,
                progress.getCompletionPercentage(),
                weakAreas
        );
    }

    private List<String> getWeakAreas(Map<String, WeaknessDetailDTO> weaknessData) {
        return weaknessData.entrySet().stream()
                .filter(entry -> "WEAK".equalsIgnoreCase(entry.getValue().getStatus()))
                .map(Map.Entry::getKey)
                .sorted()
                .toList();
    }

    private User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
    }
}
