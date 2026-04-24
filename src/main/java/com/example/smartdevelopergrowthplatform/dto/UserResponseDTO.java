package com.example.smartdevelopergrowthplatform.dto;

public class UserResponseDTO {

    private Long id;
    private String name;
    private String email;
    private String goal;
    private String level;

    public UserResponseDTO() {
    }

    public UserResponseDTO(Long id, String name, String email, String goal, String level) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.goal = goal;
        this.level = level;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGoal() {
        return goal;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
}
