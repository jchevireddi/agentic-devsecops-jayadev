package com.fieldservice.taskmanager.exception;

import com.fieldservice.taskmanager.dto.TaskDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testValidationExceptionHandler_AllFieldsMissing() throws Exception {
        TaskDTO invalidTask = TaskDTO.builder().build();
        String requestJson = objectMapper.writeValueAsString(invalidTask);

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").exists())
                .andExpect(jsonPath("$.address").exists())
                .andExpect(jsonPath("$.priority").exists());
    }

    @Test
    void testValidationExceptionHandler_SingleFieldMissing() throws Exception {
        TaskDTO invalidTask = TaskDTO.builder()
                .address("123 Main St")
                .priority("HIGH")
                .build();
        String requestJson = objectMapper.writeValueAsString(invalidTask);

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Title is required"));
    }

    @Test
    void testValidationExceptionHandler_ValidRequest() throws Exception {
        TaskDTO validTask = TaskDTO.builder()
                .title("Fix HVAC System")
                .address("123 Main St")
                .priority("HIGH")
                .build();
        String requestJson = objectMapper.writeValueAsString(validTask);

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk());
    }

    @Test
    void testValidationExceptionHandler_BlankFields() throws Exception {
        TaskDTO invalidTask = TaskDTO.builder()
                .title("  ")
                .address("  ")
                .priority("  ")
                .build();
        String requestJson = objectMapper.writeValueAsString(invalidTask);

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Title is required"))
                .andExpect(jsonPath("$.address").value("Address is required"))
                .andExpect(jsonPath("$.priority").value("Priority is required"));
    }

    @Test
    void testValidationExceptionHandler_MultipleFieldsMissing() throws Exception {
        TaskDTO invalidTask = TaskDTO.builder()
                .title("Fix HVAC")
                .build();
        String requestJson = objectMapper.writeValueAsString(invalidTask);

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.address").value("Address is required"))
                .andExpect(jsonPath("$.priority").value("Priority is required"));
    }
}
