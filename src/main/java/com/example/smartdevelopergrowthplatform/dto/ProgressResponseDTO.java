package com.example.smartdevelopergrowthplatform.dto;

public class ProgressResponseDTO {

    private long totalTasks;
    private long completedTasks;
    private long pendingTasks;
    private int completionPercentage;
    private String insight;

    public ProgressResponseDTO() {
    }

    public ProgressResponseDTO(long totalTasks, long completedTasks, long pendingTasks, int completionPercentage, String insight) {
        this.totalTasks = totalTasks;
        this.completedTasks = completedTasks;
        this.pendingTasks = pendingTasks;
        this.completionPercentage = completionPercentage;
        this.insight = insight;
    }

    public long getTotalTasks() {
        return totalTasks;
    }

    public void setTotalTasks(long totalTasks) {
        this.totalTasks = totalTasks;
    }

    public long getCompletedTasks() {
        return completedTasks;
    }

    public void setCompletedTasks(long completedTasks) {
        this.completedTasks = completedTasks;
    }

    public long getPendingTasks() {
        return pendingTasks;
    }

    public void setPendingTasks(long pendingTasks) {
        this.pendingTasks = pendingTasks;
    }

    public int getCompletionPercentage() {
        return completionPercentage;
    }

    public void setCompletionPercentage(int completionPercentage) {
        this.completionPercentage = completionPercentage;
    }

    public String getInsight() {
        return insight;
    }

    public void setInsight(String insight) {
        this.insight = insight;
    }
}
