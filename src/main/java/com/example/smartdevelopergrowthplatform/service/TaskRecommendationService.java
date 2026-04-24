package com.example.smartdevelopergrowthplatform.service;

import java.util.List;

public interface TaskRecommendationService {
    List<String> generateTasks(String goal, String level);
}
