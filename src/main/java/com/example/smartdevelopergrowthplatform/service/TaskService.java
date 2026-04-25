package com.example.smartdevelopergrowthplatform.service;

import com.example.smartdevelopergrowthplatform.dto.TaskRequestDTO;
import com.example.smartdevelopergrowthplatform.dto.TaskResponseDTO;

import java.util.List;

public interface TaskService {
    TaskResponseDTO createTask(TaskRequestDTO taskRequestDTO);

    List<TaskResponseDTO> getTasksByEmail(String email);

    List<TaskResponseDTO> getTasksByUserId(Long userId);

    TaskResponseDTO markTaskAsComplete(Long taskId);

    List<TaskResponseDTO> generateTasksByEmail(String email);

    List<TaskResponseDTO> generateTasksForUser(Long userId);
}
