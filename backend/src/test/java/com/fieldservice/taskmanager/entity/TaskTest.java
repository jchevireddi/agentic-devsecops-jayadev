package com.fieldservice.taskmanager.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    @Test
    void testTaskBuilder_Success() {
        // Act
        Task task = Task.builder()
                .id("test-id")
                .title("Test Task")
                .description("Test Description")
                .address("123 Test St")
                .priority("HIGH")
                .estimatedDuration(120)
                .build();

        // Assert
        assertNotNull(task);
        assertEquals("test-id", task.getId());
        assertEquals("Test Task", task.getTitle());
        assertEquals("Test Description", task.getDescription());
        assertEquals("123 Test St", task.getAddress());
        assertEquals("HIGH", task.getPriority());
        assertEquals(120, task.getEstimatedDuration());
    }

    @Test
    void testTaskNoArgsConstructor() {
        // Act
        Task task = new Task();

        // Assert
        assertNotNull(task);
        assertNull(task.getId());
        assertNull(task.getTitle());
        assertNull(task.getDescription());
        assertNull(task.getAddress());
        assertNull(task.getPriority());
        assertNull(task.getEstimatedDuration());
    }

    @Test
    void testTaskAllArgsConstructor() {
        // Act
        Task task = new Task("id-123", "Title", "Description", "Address", "MEDIUM", 180);

        // Assert
        assertNotNull(task);
        assertEquals("id-123", task.getId());
        assertEquals("Title", task.getTitle());
        assertEquals("Description", task.getDescription());
        assertEquals("Address", task.getAddress());
        assertEquals("MEDIUM", task.getPriority());
        assertEquals(180, task.getEstimatedDuration());
    }

    @Test
    void testTaskSettersAndGetters() {
        // Arrange
        Task task = new Task();

        // Act
        task.setId("setter-id");
        task.setTitle("Setter Title");
        task.setDescription("Setter Description");
        task.setAddress("Setter Address");
        task.setPriority("LOW");
        task.setEstimatedDuration(90);

        // Assert
        assertEquals("setter-id", task.getId());
        assertEquals("Setter Title", task.getTitle());
        assertEquals("Setter Description", task.getDescription());
        assertEquals("Setter Address", task.getAddress());
        assertEquals("LOW", task.getPriority());
        assertEquals(90, task.getEstimatedDuration());
    }

    @Test
    void testTaskEqualsAndHashCode() {
        // Arrange
        Task task1 = Task.builder()
                .id("same-id")
                .title("Task")
                .build();

        Task task2 = Task.builder()
                .id("same-id")
                .title("Task")
                .build();

        Task task3 = Task.builder()
                .id("different-id")
                .title("Task")
                .build();

        // Assert
        assertEquals(task1, task2);
        assertNotEquals(task1, task3);
        assertEquals(task1.hashCode(), task2.hashCode());
    }

    @Test
    void testTaskToString() {
        // Arrange
        Task task = Task.builder()
                .id("to-string-id")
                .title("ToString Task")
                .build();

        // Act
        String result = task.toString();

        // Assert
        assertNotNull(result);
        assertTrue(result.contains("to-string-id"));
        assertTrue(result.contains("ToString Task"));
    }
}
