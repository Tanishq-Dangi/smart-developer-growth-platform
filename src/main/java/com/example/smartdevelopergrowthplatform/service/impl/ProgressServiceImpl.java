package com.example.smartdevelopergrowthplatform.service.impl;

import com.example.smartdevelopergrowthplatform.dto.ProgressResponseDTO;
import com.example.smartdevelopergrowthplatform.entity.TaskStatus;
import com.example.smartdevelopergrowthplatform.entity.User;
import com.example.smartdevelopergrowthplatform.exception.ResourceNotFoundException;
import com.example.smartdevelopergrowthplatform.repository.TaskRepository;
import com.example.smartdevelopergrowthplatform.repository.UserRepository;
import com.example.smartdevelopergrowthplatform.service.ProgressService;
import org.springframework.stereotype.Service;

@Service
public class ProgressServiceImpl implements ProgressService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public ProgressServiceImpl(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ProgressResponseDTO getProgressByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
        return getUserProgress(user.getId());
    }

    @Override
    public ProgressResponseDTO getUserProgress(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        long totalTasks = taskRepository.countByUserId(userId);
        long completedTasks = taskRepository.countByUserIdAndStatus(userId, TaskStatus.DONE);
        long pendingTasks = totalTasks - completedTasks;
        int completionPercentage = totalTasks == 0
                ? 0
                : (int) Math.round((completedTasks * 100.0) / totalTasks);

        return new ProgressResponseDTO(
                totalTasks,
                completedTasks,
                pendingTasks,
                completionPercentage,
                getInsight(completionPercentage)
        );
    }

    private String getInsight(int completionPercentage) {
        if (completionPercentage < 50) {
            return "You need to improve consistency";
        }
        if (completionPercentage <= 80) {
            return "Good progress, keep going";
        }
        return "Excellent consistency";
    }
}
