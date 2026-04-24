package com.example.smartdevelopergrowthplatform.service.insight;

import java.util.List;

public interface InsightMessageGenerator {
    String generateMessage(int completionPercentage, List<String> weakAreas);
}
