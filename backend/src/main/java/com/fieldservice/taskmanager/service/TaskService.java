package com.fieldservice.taskmanager.service;

import com.fieldservice.taskmanager.dto.TaskDTO;
import com.fieldservice.taskmanager.entity.Task;
import com.fieldservice.taskmanager.repository.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskService {
    
    private final TaskRepository taskRepository;
    
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }
    
    @Transactional
    public TaskDTO createTask(TaskDTO taskDTO) {
        // Convert DTO to Entity
        Task task = Task.builder()
                .title(taskDTO.getTitle())
                .description(taskDTO.getDescription())
                .address(taskDTO.getAddress())
                .priority(taskDTO.getPriority())
                .estimatedDuration(taskDTO.getDuration())
                .build();
        
        // Save to database
        Task savedTask = taskRepository.save(task);
        
        // Convert Entity back to DTO
        return TaskDTO.builder()
                .id(savedTask.getId())
                .title(savedTask.getTitle())
                .description(savedTask.getDescription())
                .address(savedTask.getAddress())
                .priority(savedTask.getPriority())
                .duration(savedTask.getEstimatedDuration())
                .build();
    }
    
    @Transactional(readOnly = true)
    public List<TaskDTO> getAllTasks() {
        return taskRepository.findAll().stream()
                .map(task -> TaskDTO.builder()
                        .id(task.getId())
                        .title(task.getTitle())
                        .description(task.getDescription())
                        .address(task.getAddress())
                        .priority(task.getPriority())
                        .duration(task.getEstimatedDuration())
                        .build())
                .collect(Collectors.toList());
    }
}
