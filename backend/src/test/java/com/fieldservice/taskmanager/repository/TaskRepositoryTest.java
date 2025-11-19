package com.fieldservice.taskmanager.repository;

import com.fieldservice.taskmanager.entity.Task;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class TaskRepositoryTest {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void testSaveTask_Success() {
        // Arrange
        Task task = Task.builder()
                .title("Test Task")
                .description("Test Description")
                .address("123 Test St")
                .priority("HIGH")
                .estimatedDuration(120)
                .build();

        // Act
        Task savedTask = taskRepository.save(task);

        // Assert
        assertNotNull(savedTask);
        assertNotNull(savedTask.getId());
        assertEquals("Test Task", savedTask.getTitle());
        assertEquals("Test Description", savedTask.getDescription());
        assertEquals("123 Test St", savedTask.getAddress());
        assertEquals("HIGH", savedTask.getPriority());
        assertEquals(120, savedTask.getEstimatedDuration());
    }

    @Test
    void testSaveTask_GeneratesUUID() {
        // Arrange
        Task task = Task.builder()
                .title("UUID Test")
                .build();

        // Act
        Task savedTask = taskRepository.save(task);

        // Assert
        assertNotNull(savedTask.getId());
        assertTrue(savedTask.getId().matches("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}"),
                "Task ID should be a valid UUID");
    }

    @Test
    void testFindById_Success() {
        // Arrange
        Task task = Task.builder()
                .title("Find Test")
                .description("Find by ID test")
                .build();
        Task savedTask = entityManager.persistAndFlush(task);

        // Act
        Optional<Task> foundTask = taskRepository.findById(savedTask.getId());

        // Assert
        assertTrue(foundTask.isPresent());
        assertEquals("Find Test", foundTask.get().getTitle());
        assertEquals("Find by ID test", foundTask.get().getDescription());
    }

    @Test
    void testFindById_NotFound() {
        // Act
        Optional<Task> foundTask = taskRepository.findById("non-existent-uuid");

        // Assert
        assertFalse(foundTask.isPresent());
    }

    @Test
    void testSaveTask_WithMinimalFields() {
        // Arrange
        Task task = Task.builder()
                .title("Minimal Task")
                .build();

        // Act
        Task savedTask = taskRepository.save(task);

        // Assert
        assertNotNull(savedTask);
        assertNotNull(savedTask.getId());
        assertEquals("Minimal Task", savedTask.getTitle());
        assertNull(savedTask.getDescription());
        assertNull(savedTask.getAddress());
        assertNull(savedTask.getPriority());
        assertNull(savedTask.getEstimatedDuration());
    }

    @Test
    void testSaveTask_WithAllFields() {
        // Arrange
        Task task = Task.builder()
                .title("Complete Task")
                .description("Complete description")
                .address("789 Complete Blvd")
                .priority("MEDIUM")
                .estimatedDuration(240)
                .build();

        // Act
        Task savedTask = taskRepository.save(task);

        // Assert
        assertNotNull(savedTask);
        assertNotNull(savedTask.getId());
        assertEquals("Complete Task", savedTask.getTitle());
        assertEquals("Complete description", savedTask.getDescription());
        assertEquals("789 Complete Blvd", savedTask.getAddress());
        assertEquals("MEDIUM", savedTask.getPriority());
        assertEquals(240, savedTask.getEstimatedDuration());
    }

    @Test
    void testSaveTask_PersistsData() {
        // Arrange
        Task task = Task.builder()
                .title("Persistence Test")
                .description("Testing data persistence")
                .build();

        // Act
        Task savedTask = taskRepository.save(task);
        entityManager.flush();
        entityManager.clear();

        // Assert - fetch from DB again
        Optional<Task> retrievedTask = taskRepository.findById(savedTask.getId());
        assertTrue(retrievedTask.isPresent());
        assertEquals("Persistence Test", retrievedTask.get().getTitle());
        assertEquals("Testing data persistence", retrievedTask.get().getDescription());
    }
}
