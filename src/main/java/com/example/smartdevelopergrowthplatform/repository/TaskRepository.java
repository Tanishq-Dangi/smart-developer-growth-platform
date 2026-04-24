package com.example.smartdevelopergrowthplatform.repository;

import com.example.smartdevelopergrowthplatform.entity.Task;
import com.example.smartdevelopergrowthplatform.entity.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByUserId(Long userId);

    List<Task> findByUserIdAndCreatedAtBetween(Long userId, LocalDateTime startOfDay, LocalDateTime endOfDay);

    long countByUserId(Long userId);

    long countByUserIdAndStatus(Long userId, TaskStatus status);
}
