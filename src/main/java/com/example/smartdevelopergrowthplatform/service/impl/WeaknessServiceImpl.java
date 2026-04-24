package com.example.smartdevelopergrowthplatform.service.impl;

import com.example.smartdevelopergrowthplatform.dto.WeaknessDetailDTO;
import com.example.smartdevelopergrowthplatform.entity.Task;
import com.example.smartdevelopergrowthplatform.entity.TaskStatus;
import com.example.smartdevelopergrowthplatform.exception.ResourceNotFoundException;
import com.example.smartdevelopergrowthplatform.repository.TaskRepository;
import com.example.smartdevelopergrowthplatform.repository.UserRepository;
import com.example.smartdevelopergrowthplatform.service.WeaknessService;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class WeaknessServiceImpl implements WeaknessService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public WeaknessServiceImpl(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Map<String, WeaknessDetailDTO> analyzeWeakness(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        List<Task> tasks = taskRepository.findByUserId(userId);

        Map<String, List<Task>> tasksByCategory = tasks.stream()
                .collect(Collectors.groupingBy(task -> normalizeCategory(task.getCategory())));

        return tasksByCategory.entrySet().stream()
                .sorted(Map.Entry.comparingByKey(Comparator.naturalOrder()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> buildWeaknessDetail(entry.getKey(), entry.getValue()),
                        (existing, replacement) -> existing,
                        LinkedHashMap::new
                ));
    }

    private WeaknessDetailDTO buildWeaknessDetail(String category, List<Task> tasks) {
        long doneCount = tasks.stream()
                .filter(task -> task.getStatus() == TaskStatus.DONE)
                .count();
        long pendingCount = tasks.size() - doneCount;

        String status = pendingCount > doneCount ? "WEAK" : "GOOD";
        String suggestion = buildSuggestion(category, status);

        return new WeaknessDetailDTO(status, suggestion);
    }

    private String buildSuggestion(String category, String status) {
        if ("GOOD".equals(status)) {
            return "Keep up the momentum";
        }

        if ("DSA".equals(category)) {
            return "Focus more on problem solving";
        }
        if ("BACKEND".equals(category)) {
            return "Build more projects";
        }
        if ("SYSTEM_DESIGN".equals(category)) {
            return "Practice architecture case studies";
        }
        return "Increase consistency in this category";
    }

    private String normalizeCategory(String category) {
        if (category == null || category.isBlank()) {
            return "GENERAL";
        }
        return category.trim().toUpperCase(Locale.ROOT);
    }
}
