package com.example.smartdevelopergrowthplatform.controller;

import com.example.smartdevelopergrowthplatform.dto.TaskRequestDTO;
import com.example.smartdevelopergrowthplatform.dto.TaskResponseDTO;
import com.example.smartdevelopergrowthplatform.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/tasks")
@PreAuthorize("isAuthenticated()")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public ResponseEntity<TaskResponseDTO> createTask(@Valid @RequestBody TaskRequestDTO taskRequestDTO) {
        TaskResponseDTO createdTask = taskService.createTask(taskRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTask);
    }

    @GetMapping
    public ResponseEntity<List<TaskResponseDTO>> getMyTasks(Authentication authentication) {
        String email = authentication.getName();
        List<TaskResponseDTO> taskResponseDTOS = taskService.getTasksByEmail(email);
        return ResponseEntity.ok(taskResponseDTOS);
    }

    @PutMapping("/{taskId}/complete")
    public ResponseEntity<TaskResponseDTO> markTaskAsComplete(@PathVariable Long taskId) {
        TaskResponseDTO updatedTask = taskService.markTaskAsComplete(taskId);
        return ResponseEntity.ok(updatedTask);
    }

    @PostMapping("/generate")
    public ResponseEntity<List<TaskResponseDTO>> generateTasksForUser(Authentication authentication) {
        String email = authentication.getName();
        List<TaskResponseDTO> generatedTasks = taskService.generateTasksByEmail(email);
        return ResponseEntity.status(HttpStatus.CREATED).body(generatedTasks);
    }
}
