package com.example.smartdevelopergrowthplatform.service.impl;

import com.example.smartdevelopergrowthplatform.service.TaskRecommendationService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskRecommendationServiceImpl implements TaskRecommendationService {

    @Override
    public List<String> generateTasks(String goal, String level) {
        String normalizedGoal = goal == null ? "" : goal.trim();
        String normalizedLevel = level == null ? "" : level.trim();

        if (normalizedGoal.equalsIgnoreCase("Backend") && normalizedLevel.equalsIgnoreCase("Beginner")) {
            return List.of(
                    "Learn Java basics",
                    "Solve 2 array problems",
                    "Build REST API"
            );
        }

        if (normalizedLevel.equalsIgnoreCase("Intermediate")) {
            return List.of(
                    "Solve 3 medium DSA problems",
                    "Learn Spring Boot",
                    "Build CRUD project"
            );
        }

        if (normalizedLevel.equalsIgnoreCase("Advanced")) {
            return List.of(
                    "System design basics",
                    "Build scalable service",
                    "Optimize queries"
            );
        }

        return List.of(
                "Review core programming fundamentals",
                "Solve 2 easy DSA problems",
                "Build a small learning project"
        );
    }
}
