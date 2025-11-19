package com.fieldservice.taskmanager.service;

import com.fieldservice.taskmanager.dto.TaskDTO;
import com.fieldservice.taskmanager.entity.Task;
import com.fieldservice.taskmanager.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    private Task mockTask;
    private TaskDTO mockTaskDTO;

    @BeforeEach
    void setUp() {
        mockTask = Task.builder()
                .id("test-uuid-123")
                .title("Test Task")
                .description("Test Description")
                .address("123 Test St")
                .priority("HIGH")
                .estimatedDuration(120)
                .build();

        mockTaskDTO = TaskDTO.builder()
                .title("Test Task")
                .description("Test Description")
                .address("123 Test St")
                .priority("HIGH")
                .duration(120)
                .build();
    }

    @Test
    void testCreateTask_Success() {
        // Arrange
        when(taskRepository.save(any(Task.class))).thenReturn(mockTask);

        // Act
        TaskDTO result = taskService.createTask(mockTaskDTO);

        // Assert
        assertNotNull(result);
        assertEquals("test-uuid-123", result.getId());
        assertEquals("Test Task", result.getTitle());
        assertEquals("Test Description", result.getDescription());
        assertEquals("123 Test St", result.getAddress());
        assertEquals("HIGH", result.getPriority());
        assertEquals(120, result.getDuration());
        
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void testCreateTask_WithMinimalFields() {
        // Arrange
        TaskDTO minimalDTO = TaskDTO.builder()
                .title("Minimal Task")
                .build();

        Task minimalTask = Task.builder()
                .id("minimal-uuid")
                .title("Minimal Task")
                .build();

        when(taskRepository.save(any(Task.class))).thenReturn(minimalTask);

        // Act
        TaskDTO result = taskService.createTask(minimalDTO);

        // Assert
        assertNotNull(result);
        assertEquals("minimal-uuid", result.getId());
        assertEquals("Minimal Task", result.getTitle());
        assertNull(result.getDescription());
        assertNull(result.getAddress());
        assertNull(result.getPriority());
        assertNull(result.getDuration());
        
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void testCreateTask_WithAllFields() {
        // Arrange
        TaskDTO fullDTO = TaskDTO.builder()
                .title("Full Task")
                .description("Complete description")
                .address("456 Full Ave")
                .priority("MEDIUM")
                .duration(240)
                .build();

        Task fullTask = Task.builder()
                .id("full-uuid")
                .title("Full Task")
                .description("Complete description")
                .address("456 Full Ave")
                .priority("MEDIUM")
                .estimatedDuration(240)
                .build();

        when(taskRepository.save(any(Task.class))).thenReturn(fullTask);

        // Act
        TaskDTO result = taskService.createTask(fullDTO);

        // Assert
        assertNotNull(result);
        assertEquals("full-uuid", result.getId());
        assertEquals("Full Task", result.getTitle());
        assertEquals("Complete description", result.getDescription());
        assertEquals("456 Full Ave", result.getAddress());
        assertEquals("MEDIUM", result.getPriority());
        assertEquals(240, result.getDuration());
        
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void testCreateTask_ConvertsFieldsCorrectly() {
        // Arrange
        TaskDTO inputDTO = TaskDTO.builder()
                .title("Conversion Test")
                .duration(180)
                .build();

        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> {
            Task taskArg = invocation.getArgument(0);
            assertEquals("Conversion Test", taskArg.getTitle());
            assertEquals(180, taskArg.getEstimatedDuration());
            
            return Task.builder()
                    .id("conversion-uuid")
                    .title(taskArg.getTitle())
                    .estimatedDuration(taskArg.getEstimatedDuration())
                    .build();
        });

        // Act
        TaskDTO result = taskService.createTask(inputDTO);

        // Assert
        assertNotNull(result);
        assertEquals("conversion-uuid", result.getId());
        assertEquals("Conversion Test", result.getTitle());
        assertEquals(180, result.getDuration());
        
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void testCreateTask_HandlesNullDescription() {
        // Arrange
        TaskDTO dtoWithNulls = TaskDTO.builder()
                .title("Task with nulls")
                .description(null)
                .build();

        Task taskWithNulls = Task.builder()
                .id("null-desc-uuid")
                .title("Task with nulls")
                .description(null)
                .build();

        when(taskRepository.save(any(Task.class))).thenReturn(taskWithNulls);

        // Act
        TaskDTO result = taskService.createTask(dtoWithNulls);

        // Assert
        assertNotNull(result);
        assertEquals("null-desc-uuid", result.getId());
        assertEquals("Task with nulls", result.getTitle());
        assertNull(result.getDescription());
        
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void testGetAllTasks_ReturnsEmptyList() {
        // Arrange
        when(taskRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<TaskDTO> result = taskService.getAllTasks();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(taskRepository, times(1)).findAll();
    }

    @Test
    void testGetAllTasks_ReturnsSingleTask() {
        // Arrange
        when(taskRepository.findAll()).thenReturn(Collections.singletonList(mockTask));

        // Act
        List<TaskDTO> result = taskService.getAllTasks();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        TaskDTO taskDTO = result.get(0);
        assertEquals("test-uuid-123", taskDTO.getId());
        assertEquals("Test Task", taskDTO.getTitle());
        assertEquals("Test Description", taskDTO.getDescription());
        assertEquals("123 Test St", taskDTO.getAddress());
        assertEquals("HIGH", taskDTO.getPriority());
        assertEquals(120, taskDTO.getDuration());
        verify(taskRepository, times(1)).findAll();
    }

    @Test
    void testGetAllTasks_ReturnsMultipleTasks() {
        // Arrange
        Task task1 = Task.builder()
                .id("uuid-1")
                .title("Task 1")
                .description("Description 1")
                .address("Address 1")
                .priority("HIGH")
                .estimatedDuration(60)
                .build();

        Task task2 = Task.builder()
                .id("uuid-2")
                .title("Task 2")
                .description("Description 2")
                .address("Address 2")
                .priority("MEDIUM")
                .estimatedDuration(90)
                .build();

        Task task3 = Task.builder()
                .id("uuid-3")
                .title("Task 3")
                .description("Description 3")
                .address("Address 3")
                .priority("LOW")
                .estimatedDuration(120)
                .build();

        when(taskRepository.findAll()).thenReturn(Arrays.asList(task1, task2, task3));

        // Act
        List<TaskDTO> result = taskService.getAllTasks();

        // Assert
        assertNotNull(result);
        assertEquals(3, result.size());
        
        assertEquals("uuid-1", result.get(0).getId());
        assertEquals("Task 1", result.get(0).getTitle());
        assertEquals("HIGH", result.get(0).getPriority());
        
        assertEquals("uuid-2", result.get(1).getId());
        assertEquals("Task 2", result.get(1).getTitle());
        assertEquals("MEDIUM", result.get(1).getPriority());
        
        assertEquals("uuid-3", result.get(2).getId());
        assertEquals("Task 3", result.get(2).getTitle());
        assertEquals("LOW", result.get(2).getPriority());
        
        verify(taskRepository, times(1)).findAll();
    }
}
