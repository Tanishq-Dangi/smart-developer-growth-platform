package com.example.smartdevelopergrowthplatform.dto;

public class WeaknessDetailDTO {

    private String status;
    private String suggestion;

    public WeaknessDetailDTO() {
    }

    public WeaknessDetailDTO(String status, String suggestion) {
        this.status = status;
        this.suggestion = suggestion;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSuggestion() {
        return suggestion;
    }

    public void setSuggestion(String suggestion) {
        this.suggestion = suggestion;
    }
}
