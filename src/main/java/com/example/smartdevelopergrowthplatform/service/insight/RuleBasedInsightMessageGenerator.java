package com.example.smartdevelopergrowthplatform.service.insight;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class RuleBasedInsightMessageGenerator implements InsightMessageGenerator {

    @Override
    public String generateMessage(int completionPercentage, List<String> weakAreas) {
        List<String> messages = new ArrayList<>();

        if (completionPercentage < 50) {
            messages.add("You are inconsistent. Focus on completing daily tasks.");
        }
        if (weakAreas.contains("DSA")) {
            messages.add("You need to improve problem solving skills. Solve at least 2 problems daily.");
        }
        if (weakAreas.contains("BACKEND")) {
            messages.add("Build more backend projects to strengthen your development skills.");
        }
        if (completionPercentage > 80) {
            messages.add("Great consistency! Try increasing difficulty level.");
        }

        if (messages.isEmpty()) {
            return "You are making steady progress. Keep completing tasks consistently.";
        }

        return String.join(" ", messages);
    }
}
