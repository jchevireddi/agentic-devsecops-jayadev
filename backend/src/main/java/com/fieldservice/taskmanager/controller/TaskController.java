package com.fieldservice.taskmanager.controller;

import com.fieldservice.taskmanager.dto.TaskDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @PostMapping
    public ResponseEntity<TaskDTO> createTask(@RequestBody TaskDTO taskDTO) {
        // Generate a mock task ID
        String mockTaskId = UUID.randomUUID().toString();
        
        // Set the ID in the response
        taskDTO.setId(mockTaskId);
        
        // Return hardcoded success response
        return ResponseEntity.status(HttpStatus.OK).body(taskDTO);
    }
}
