package com.example.smartdevelopergrowthplatform.service.impl;

import com.example.smartdevelopergrowthplatform.dto.TaskRequestDTO;
import com.example.smartdevelopergrowthplatform.dto.TaskResponseDTO;
import com.example.smartdevelopergrowthplatform.entity.Task;
import com.example.smartdevelopergrowthplatform.entity.TaskStatus;
import com.example.smartdevelopergrowthplatform.entity.User;
import com.example.smartdevelopergrowthplatform.exception.ResourceNotFoundException;
import com.example.smartdevelopergrowthplatform.repository.TaskRepository;
import com.example.smartdevelopergrowthplatform.repository.UserRepository;
import com.example.smartdevelopergrowthplatform.service.TaskRecommendationService;
import com.example.smartdevelopergrowthplatform.service.TaskService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final TaskRecommendationService taskRecommendationService;

    public TaskServiceImpl(
            TaskRepository taskRepository,
            UserRepository userRepository,
            TaskRecommendationService taskRecommendationService
    ) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.taskRecommendationService = taskRecommendationService;
    }

    @Override
    public TaskResponseDTO createTask(TaskRequestDTO taskRequestDTO) {
        User user = userRepository.findById(taskRequestDTO.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + taskRequestDTO.getUserId()));

        Task task = new Task();
        task.setTitle(taskRequestDTO.getTitle());
        task.setDescription(taskRequestDTO.getDescription());
        task.setCategory(resolveCategory(taskRequestDTO.getCategory(), taskRequestDTO.getTitle()));
        task.setUser(user);

        Task savedTask = taskRepository.save(task);
        return mapToTaskResponseDTO(savedTask);
    }

    @Override
    public List<TaskResponseDTO> getTasksByUserId(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        return taskRepository.findByUserId(userId).stream()
                .map(this::mapToTaskResponseDTO)
                .toList();
    }

    @Override
    public TaskResponseDTO markTaskAsComplete(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + taskId));

        if (task.getStatus() == TaskStatus.PENDING) {
            task.setStatus(TaskStatus.DONE);
            task = taskRepository.save(task);
        }

        return mapToTaskResponseDTO(task);
    }

    @Override
    public List<TaskResponseDTO> generateTasksForUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        List<String> recommendations = taskRecommendationService.generateTasks(user.getGoal(), user.getLevel());
        LocalDateTime startOfDay = LocalDateTime.now().toLocalDate().atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1).minusNanos(1);

        Set<String> existingTitlesToday = new HashSet<>();
        taskRepository.findByUserIdAndCreatedAtBetween(userId, startOfDay, endOfDay).stream()
                .map(Task::getTitle)
                .map(this::normalizeTitle)
                .forEach(existingTitlesToday::add);

        List<Task> tasksToCreate = recommendations.stream()
                .filter(title -> !existingTitlesToday.contains(normalizeTitle(title)))
                .map(title -> {
                    Task task = new Task();
                    task.setTitle(title);
                    task.setDescription("Auto-generated recommendation task");
                    task.setCategory(categorizeByTitle(title));
                    task.setUser(user);
                    return task;
                })
                .toList();

        if (tasksToCreate.isEmpty()) {
            return List.of();
        }

        return taskRepository.saveAll(tasksToCreate).stream()
                .map(this::mapToTaskResponseDTO)
                .toList();
    }

    private String normalizeTitle(String title) {
        if (title == null) {
            return "";
        }
        return title.trim().toLowerCase(Locale.ROOT);
    }

    private String resolveCategory(String category, String title) {
        if (category != null && !category.isBlank()) {
            return category.trim().toUpperCase(Locale.ROOT);
        }
        return categorizeByTitle(title);
    }

    private String categorizeByTitle(String title) {
        String normalizedTitle = normalizeTitle(title);

        if (normalizedTitle.contains("system design")) {
            return "SYSTEM_DESIGN";
        }
        if (normalizedTitle.contains("dsa") || normalizedTitle.contains("array") || normalizedTitle.contains("problem")) {
            return "DSA";
        }
        if (normalizedTitle.contains("rest api")
                || normalizedTitle.contains("spring")
                || normalizedTitle.contains("crud")
                || normalizedTitle.contains("backend")
                || normalizedTitle.contains("service")
                || normalizedTitle.contains("query")) {
            return "BACKEND";
        }
        return "GENERAL";
    }

    private TaskResponseDTO mapToTaskResponseDTO(Task task) {
        return new TaskResponseDTO(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getCategory(),
                task.getStatus(),
                task.getCreatedAt(),
                task.getUser().getId()
        );
    }
}
