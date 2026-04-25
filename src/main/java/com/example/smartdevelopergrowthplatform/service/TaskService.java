package com.example.smartdevelopergrowthplatform.service;

import com.example.smartdevelopergrowthplatform.dto.TaskRequestDTO;
import com.example.smartdevelopergrowthplatform.dto.TaskResponseDTO;

import java.util.List;

public interface TaskService {
    TaskResponseDTO createTask(TaskRequestDTO taskRequestDTO);

    List<TaskResponseDTO> getTasksByUserId(Long userId);

    TaskResponseDTO markTaskAsComplete(Long taskId);

    List<TaskResponseDTO> generateTasksForUser(Long userId);
}
