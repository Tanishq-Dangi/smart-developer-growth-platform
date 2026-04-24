package com.example.smartdevelopergrowthplatform.controller;

import com.example.smartdevelopergrowthplatform.dto.TaskRequestDTO;
import com.example.smartdevelopergrowthplatform.dto.TaskResponseDTO;
import com.example.smartdevelopergrowthplatform.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/tasks")
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

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TaskResponseDTO>> getTasksByUserId(@PathVariable Long userId) {
        List<TaskResponseDTO> taskResponseDTOS = taskService.getTasksByUserId(userId);
        return ResponseEntity.ok(taskResponseDTOS);
    }

    @PutMapping("/{taskId}/complete")
    public ResponseEntity<TaskResponseDTO> markTaskAsDone(@PathVariable Long taskId, @RequestParam Long userId) {
        TaskResponseDTO updatedTask = taskService.markTaskAsDone(taskId, userId);
        return ResponseEntity.ok(updatedTask);
    }

    @PostMapping("/generate/{userId}")
    public ResponseEntity<List<TaskResponseDTO>> generateTasksForUser(@PathVariable Long userId) {
        List<TaskResponseDTO> generatedTasks = taskService.generateTasksForUser(userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(generatedTasks);
    }
}
