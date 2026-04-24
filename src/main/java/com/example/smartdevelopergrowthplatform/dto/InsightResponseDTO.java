package com.example.smartdevelopergrowthplatform.dto;

import java.util.List;

public class InsightResponseDTO {

    private String insight;
    private int completionPercentage;
    private List<String> weakAreas;

    public InsightResponseDTO() {
    }

    public InsightResponseDTO(String insight, int completionPercentage, List<String> weakAreas) {
        this.insight = insight;
        this.completionPercentage = completionPercentage;
        this.weakAreas = weakAreas;
    }

    public String getInsight() {
        return insight;
    }

    public void setInsight(String insight) {
        this.insight = insight;
    }

    public int getCompletionPercentage() {
        return completionPercentage;
    }

    public void setCompletionPercentage(int completionPercentage) {
        this.completionPercentage = completionPercentage;
    }

    public List<String> getWeakAreas() {
        return weakAreas;
    }

    public void setWeakAreas(List<String> weakAreas) {
        this.weakAreas = weakAreas;
    }
}
