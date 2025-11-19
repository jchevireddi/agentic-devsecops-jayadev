package com.fieldservice.taskmanager.service;

import com.fieldservice.taskmanager.dto.TaskDTO;
import com.fieldservice.taskmanager.entity.Task;
import com.fieldservice.taskmanager.repository.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
