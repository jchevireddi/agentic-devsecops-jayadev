package com.fieldservice.taskmanager.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskDTOTest {

    @Test
    void testTaskDTOBuilder() {
        // Arrange & Act
        TaskDTO task = TaskDTO.builder()
                .id("test-id-123")
                .title("Test Task")
                .description("Test Description")
                .address("Test Address")
                .priority("HIGH")
                .duration(120)
                .build();

        // Assert
        assertEquals("test-id-123", task.getId());
        assertEquals("Test Task", task.getTitle());
        assertEquals("Test Description", task.getDescription());
        assertEquals("Test Address", task.getAddress());
        assertEquals("HIGH", task.getPriority());
        assertEquals(120, task.getDuration());
    }

    @Test
    void testTaskDTONoArgsConstructor() {
        // Act
        TaskDTO task = new TaskDTO();

        // Assert
        assertNotNull(task);
        assertNull(task.getId());
        assertNull(task.getTitle());
    }

    @Test
    void testTaskDTOAllArgsConstructor() {
        // Act
        TaskDTO task = new TaskDTO("id-1", "Title", "Description", "Address", "MEDIUM", 90);

        // Assert
        assertEquals("id-1", task.getId());
        assertEquals("Title", task.getTitle());
        assertEquals("Description", task.getDescription());
        assertEquals("Address", task.getAddress());
        assertEquals("MEDIUM", task.getPriority());
        assertEquals(90, task.getDuration());
    }

    @Test
    void testTaskDTOSetters() {
        // Arrange
        TaskDTO task = new TaskDTO();

        // Act
        task.setId("new-id");
        task.setTitle("New Title");
        task.setDescription("New Description");
        task.setAddress("New Address");
        task.setPriority("LOW");
        task.setDuration(60);

        // Assert
        assertEquals("new-id", task.getId());
        assertEquals("New Title", task.getTitle());
        assertEquals("New Description", task.getDescription());
        assertEquals("New Address", task.getAddress());
        assertEquals("LOW", task.getPriority());
        assertEquals(60, task.getDuration());
    }

    @Test
    void testTaskDTOEqualsAndHashCode() {
        // Arrange
        TaskDTO task1 = TaskDTO.builder()
                .id("id-1")
                .title("Task 1")
                .description("Description 1")
                .address("Address 1")
                .priority("HIGH")
                .duration(120)
                .build();

        TaskDTO task2 = TaskDTO.builder()
                .id("id-1")
                .title("Task 1")
                .description("Description 1")
                .address("Address 1")
                .priority("HIGH")
                .duration(120)
                .build();

        TaskDTO task3 = TaskDTO.builder()
                .id("id-2")
                .title("Task 2")
                .build();

        // Assert
        assertEquals(task1, task2);
        assertEquals(task1.hashCode(), task2.hashCode());
        assertNotEquals(task1, task3);
        assertNotEquals(task1.hashCode(), task3.hashCode());
    }

    @Test
    void testTaskDTOToString() {
        // Arrange
        TaskDTO task = TaskDTO.builder()
                .id("id-123")
                .title("Test Task")
                .build();

        // Act
        String result = task.toString();

        // Assert
        assertNotNull(result);
        assertTrue(result.contains("id-123"));
        assertTrue(result.contains("Test Task"));
    }
}
