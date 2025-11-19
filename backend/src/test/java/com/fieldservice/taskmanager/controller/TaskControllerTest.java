package com.fieldservice.taskmanager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fieldservice.taskmanager.dto.TaskDTO;
import com.fieldservice.taskmanager.service.TaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskController.class)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    
    @MockBean
    private TaskService taskService;

    @Test
    void testCreateTask_Success() throws Exception {
        // Arrange
        TaskDTO inputTask = TaskDTO.builder()
                .title("Fix HVAC System")
                .description("Client reported AC not cooling properly")
                .address("123 Main St, Springfield")
                .priority("HIGH")
                .duration(120)
                .build();
        
        TaskDTO savedTask = TaskDTO.builder()
                .id("test-uuid-1234")
                .title("Fix HVAC System")
                .description("Client reported AC not cooling properly")
                .address("123 Main St, Springfield")
                .priority("HIGH")
                .duration(120)
                .build();
        
        when(taskService.createTask(any(TaskDTO.class))).thenReturn(savedTask);

        String requestJson = objectMapper.writeValueAsString(inputTask);

        // Act & Assert
        MvcResult result = mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("test-uuid-1234"))
                .andExpect(jsonPath("$.title").value("Fix HVAC System"))
                .andExpect(jsonPath("$.description").value("Client reported AC not cooling properly"))
                .andExpect(jsonPath("$.address").value("123 Main St, Springfield"))
                .andExpect(jsonPath("$.priority").value("HIGH"))
                .andExpect(jsonPath("$.duration").value(120))
                .andReturn();

        // Verify the ID is not null
        String responseJson = result.getResponse().getContentAsString();
        TaskDTO responseTask = objectMapper.readValue(responseJson, TaskDTO.class);
        assertNotNull(responseTask.getId(), "Task ID should not be null");
    }

    @Test
    void testCreateTask_WithMinimalFields() throws Exception {
        // Arrange
        TaskDTO inputTask = TaskDTO.builder()
                .title("Simple Task")
                .build();
        
        TaskDTO savedTask = TaskDTO.builder()
                .id("test-uuid-5678")
                .title("Simple Task")
                .build();
        
        when(taskService.createTask(any(TaskDTO.class))).thenReturn(savedTask);

        String requestJson = objectMapper.writeValueAsString(inputTask);

        // Act & Assert
        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("test-uuid-5678"))
                .andExpect(jsonPath("$.title").value("Simple Task"));
    }

    @Test
    void testCreateTask_WithAllFields() throws Exception {
        // Arrange
        TaskDTO inputTask = TaskDTO.builder()
                .title("Complete System Maintenance")
                .description("Full system check and maintenance")
                .address("456 Oak Avenue, Metropolis")
                .priority("MEDIUM")
                .duration(240)
                .build();
        
        TaskDTO savedTask = TaskDTO.builder()
                .id("test-uuid-9999")
                .title("Complete System Maintenance")
                .description("Full system check and maintenance")
                .address("456 Oak Avenue, Metropolis")
                .priority("MEDIUM")
                .duration(240)
                .build();
        
        when(taskService.createTask(any(TaskDTO.class))).thenReturn(savedTask);

        String requestJson = objectMapper.writeValueAsString(inputTask);

        // Act & Assert
        MvcResult result = mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("test-uuid-9999"))
                .andExpect(jsonPath("$.title").value("Complete System Maintenance"))
                .andExpect(jsonPath("$.description").value("Full system check and maintenance"))
                .andExpect(jsonPath("$.address").value("456 Oak Avenue, Metropolis"))
                .andExpect(jsonPath("$.priority").value("MEDIUM"))
                .andExpect(jsonPath("$.duration").value(240))
                .andReturn();

        // Verify response structure
        String responseJson = result.getResponse().getContentAsString();
        TaskDTO responseTask = objectMapper.readValue(responseJson, TaskDTO.class);
        assertNotNull(responseTask);
        assertNotNull(responseTask.getId());
        assertEquals("Complete System Maintenance", responseTask.getTitle());
    }

    @Test
    void testCreateTask_WithLowPriority() throws Exception {
        // Arrange
        TaskDTO inputTask = TaskDTO.builder()
                .title("Routine Inspection")
                .description("Annual routine inspection")
                .address("789 Pine Street, Gotham")
                .priority("LOW")
                .duration(60)
                .build();
        
        TaskDTO savedTask = TaskDTO.builder()
                .id("test-uuid-low")
                .title("Routine Inspection")
                .description("Annual routine inspection")
                .address("789 Pine Street, Gotham")
                .priority("LOW")
                .duration(60)
                .build();
        
        when(taskService.createTask(any(TaskDTO.class))).thenReturn(savedTask);

        String requestJson = objectMapper.writeValueAsString(inputTask);

        // Act & Assert
        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.priority").value("LOW"));
    }

    @Test
    void testCreateTask_EmptyRequest() throws Exception {
        // Arrange
        TaskDTO inputTask = TaskDTO.builder().build();
        
        TaskDTO savedTask = TaskDTO.builder()
                .id("test-uuid-empty")
                .build();
        
        when(taskService.createTask(any(TaskDTO.class))).thenReturn(savedTask);
        
        String requestJson = objectMapper.writeValueAsString(inputTask);

        // Act & Assert
        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("test-uuid-empty"));
    }

    @Test
    void testGetAllTasks_ReturnsEmptyArray() throws Exception {
        // Arrange
        when(taskService.getAllTasks()).thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void testGetAllTasks_ReturnsSingleTask() throws Exception {
        // Arrange
        TaskDTO task = TaskDTO.builder()
                .id("uuid-1")
                .title("Task 1")
                .description("Description 1")
                .address("Address 1")
                .priority("HIGH")
                .duration(60)
                .build();

        when(taskService.getAllTasks()).thenReturn(Collections.singletonList(task));

        // Act & Assert
        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value("uuid-1"))
                .andExpect(jsonPath("$[0].title").value("Task 1"))
                .andExpect(jsonPath("$[0].description").value("Description 1"))
                .andExpect(jsonPath("$[0].address").value("Address 1"))
                .andExpect(jsonPath("$[0].priority").value("HIGH"))
                .andExpect(jsonPath("$[0].duration").value(60));
    }

    @Test
    void testGetAllTasks_ReturnsMultipleTasks() throws Exception {
        // Arrange
        TaskDTO task1 = TaskDTO.builder()
                .id("uuid-1")
                .title("Task 1")
                .description("Description 1")
                .address("Address 1")
                .priority("HIGH")
                .duration(60)
                .build();

        TaskDTO task2 = TaskDTO.builder()
                .id("uuid-2")
                .title("Task 2")
                .description("Description 2")
                .address("Address 2")
                .priority("MEDIUM")
                .duration(90)
                .build();

        TaskDTO task3 = TaskDTO.builder()
                .id("uuid-3")
                .title("Task 3")
                .description("Description 3")
                .address("Address 3")
                .priority("LOW")
                .duration(120)
                .build();

        when(taskService.getAllTasks()).thenReturn(Arrays.asList(task1, task2, task3));

        // Act & Assert
        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].id").value("uuid-1"))
                .andExpect(jsonPath("$[0].title").value("Task 1"))
                .andExpect(jsonPath("$[0].priority").value("HIGH"))
                .andExpect(jsonPath("$[1].id").value("uuid-2"))
                .andExpect(jsonPath("$[1].title").value("Task 2"))
                .andExpect(jsonPath("$[1].priority").value("MEDIUM"))
                .andExpect(jsonPath("$[2].id").value("uuid-3"))
                .andExpect(jsonPath("$[2].title").value("Task 3"))
                .andExpect(jsonPath("$[2].priority").value("LOW"));
    }
}
