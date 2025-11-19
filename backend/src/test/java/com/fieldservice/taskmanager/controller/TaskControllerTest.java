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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
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
        // Arrange - this test now expects validation error because address and priority are missing
        TaskDTO inputTask = TaskDTO.builder()
                .title("Simple Task")
                .build();

        String requestJson = objectMapper.writeValueAsString(inputTask);

        // Act & Assert - should return 400 due to missing required fields
        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.address").value("Address is required"))
                .andExpect(jsonPath("$.priority").value("Priority is required"));
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
        // Arrange - empty request should fail validation
        TaskDTO inputTask = TaskDTO.builder().build();
        
        String requestJson = objectMapper.writeValueAsString(inputTask);

        // Act & Assert - should return 400 due to missing required fields
        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title").value("Title is required"))
                .andExpect(jsonPath("$.address").value("Address is required"))
                .andExpect(jsonPath("$.priority").value("Priority is required"));
    }

    @Test
    void testCreateTask_MissingTitle() throws Exception {
        // Arrange
        TaskDTO inputTask = TaskDTO.builder()
                .address("123 Main St")
                .priority("HIGH")
                .build();

        String requestJson = objectMapper.writeValueAsString(inputTask);

        // Act & Assert
        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title").value("Title is required"));
    }

    @Test
    void testCreateTask_MissingAddress() throws Exception {
        // Arrange
        TaskDTO inputTask = TaskDTO.builder()
                .title("Fix HVAC")
                .priority("HIGH")
                .build();

        String requestJson = objectMapper.writeValueAsString(inputTask);

        // Act & Assert
        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.address").value("Address is required"));
    }

    @Test
    void testCreateTask_MissingPriority() throws Exception {
        // Arrange
        TaskDTO inputTask = TaskDTO.builder()
                .title("Fix HVAC")
                .address("123 Main St")
                .build();

        String requestJson = objectMapper.writeValueAsString(inputTask);

        // Act & Assert
        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.priority").value("Priority is required"));
    }

    @Test
    void testCreateTask_BlankTitle() throws Exception {
        // Arrange
        TaskDTO inputTask = TaskDTO.builder()
                .title("   ")
                .address("123 Main St")
                .priority("HIGH")
                .build();

        String requestJson = objectMapper.writeValueAsString(inputTask);

        // Act & Assert
        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title").value("Title is required"));
    }
}
