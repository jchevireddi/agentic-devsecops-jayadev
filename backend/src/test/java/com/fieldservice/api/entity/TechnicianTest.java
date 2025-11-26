package com.fieldservice.api.entity;

import com.fieldservice.api.enums.TechnicianAvailability;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class TechnicianTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void shouldCreateTechnicianWithValidName() {
        Technician technician = new Technician("John Doe");
        
        assertEquals("John Doe", technician.getName());
        assertEquals(TechnicianAvailability.AVAILABLE, technician.getAvailability());
    }

    @Test
    void shouldDefaultAvailabilityToAvailable() {
        Technician technician = new Technician();
        technician.setName("Jane Smith");
        
        assertEquals(TechnicianAvailability.AVAILABLE, technician.getAvailability());
    }

    @Test
    void shouldFailValidationWhenNameIsBlank() {
        Technician technician = new Technician();
        technician.setName("");
        
        Set<ConstraintViolation<Technician>> violations = validator.validate(technician);
        
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Name is required")));
    }

    @Test
    void shouldFailValidationWhenNameIsNull() {
        Technician technician = new Technician();
        technician.setName(null);
        
        Set<ConstraintViolation<Technician>> violations = validator.validate(technician);
        
        assertFalse(violations.isEmpty());
    }

    @Test
    void shouldAcceptValidEmail() {
        Technician technician = new Technician("John Doe");
        technician.setEmail("john@example.com");
        
        Set<ConstraintViolation<Technician>> violations = validator.validate(technician);
        
        assertTrue(violations.isEmpty());
        assertEquals("john@example.com", technician.getEmail());
    }

    @Test
    void shouldFailValidationWhenEmailIsInvalid() {
        Technician technician = new Technician("John Doe");
        technician.setEmail("invalid-email");
        
        Set<ConstraintViolation<Technician>> violations = validator.validate(technician);
        
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Email should be valid")));
    }

    @Test
    void shouldSetAndGetPhoneNumber() {
        Technician technician = new Technician("John Doe");
        technician.setPhoneNumber("+1234567890");
        
        assertEquals("+1234567890", technician.getPhoneNumber());
    }

    @Test
    void shouldUpdateLocation() {
        Technician technician = new Technician("John Doe");
        
        technician.updateLocation(40.7128, -74.0060);
        
        assertEquals(40.7128, technician.getLatitude());
        assertEquals(-74.0060, technician.getLongitude());
        assertNotNull(technician.getLastLocationUpdate());
    }

    @Test
    void shouldAcceptValidLatitude() {
        Technician technician = new Technician("John Doe");
        technician.setLatitude(45.0);
        
        Set<ConstraintViolation<Technician>> violations = validator.validate(technician);
        
        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldAcceptMinLatitude() {
        Technician technician = new Technician("John Doe");
        technician.setLatitude(-90.0);
        
        Set<ConstraintViolation<Technician>> violations = validator.validate(technician);
        
        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldAcceptMaxLatitude() {
        Technician technician = new Technician("John Doe");
        technician.setLatitude(90.0);
        
        Set<ConstraintViolation<Technician>> violations = validator.validate(technician);
        
        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldFailValidationWhenLatitudeTooLow() {
        Technician technician = new Technician("John Doe");
        technician.setLatitude(-91.0);
        
        Set<ConstraintViolation<Technician>> violations = validator.validate(technician);
        
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Latitude must be between -90 and 90")));
    }

    @Test
    void shouldFailValidationWhenLatitudeTooHigh() {
        Technician technician = new Technician("John Doe");
        technician.setLatitude(91.0);
        
        Set<ConstraintViolation<Technician>> violations = validator.validate(technician);
        
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Latitude must be between -90 and 90")));
    }

    @Test
    void shouldAcceptValidLongitude() {
        Technician technician = new Technician("John Doe");
        technician.setLongitude(-74.0);
        
        Set<ConstraintViolation<Technician>> violations = validator.validate(technician);
        
        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldAcceptMinLongitude() {
        Technician technician = new Technician("John Doe");
        technician.setLongitude(-180.0);
        
        Set<ConstraintViolation<Technician>> violations = validator.validate(technician);
        
        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldAcceptMaxLongitude() {
        Technician technician = new Technician("John Doe");
        technician.setLongitude(180.0);
        
        Set<ConstraintViolation<Technician>> violations = validator.validate(technician);
        
        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldFailValidationWhenLongitudeTooLow() {
        Technician technician = new Technician("John Doe");
        technician.setLongitude(-181.0);
        
        Set<ConstraintViolation<Technician>> violations = validator.validate(technician);
        
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Longitude must be between -180 and 180")));
    }

    @Test
    void shouldFailValidationWhenLongitudeTooHigh() {
        Technician technician = new Technician("John Doe");
        technician.setLongitude(181.0);
        
        Set<ConstraintViolation<Technician>> violations = validator.validate(technician);
        
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Longitude must be between -180 and 180")));
    }

    @Test
    void shouldSetAndGetId() {
        Technician technician = new Technician("John Doe");
        UUID id = UUID.randomUUID();
        technician.setId(id);
        
        assertEquals(id, technician.getId());
    }

    @Test
    void shouldSetAndGetAvailability() {
        Technician technician = new Technician("John Doe");
        technician.setAvailability(TechnicianAvailability.BUSY);
        
        assertEquals(TechnicianAvailability.BUSY, technician.getAvailability());
    }

    @Test
    void shouldSetAndGetCreatedAt() {
        Technician technician = new Technician("John Doe");
        LocalDateTime now = LocalDateTime.now();
        technician.setCreatedAt(now);
        
        assertEquals(now, technician.getCreatedAt());
    }

    @Test
    void shouldSetAndGetLastLocationUpdate() {
        Technician technician = new Technician("John Doe");
        LocalDateTime now = LocalDateTime.now();
        technician.setLastLocationUpdate(now);
        
        assertEquals(now, technician.getLastLocationUpdate());
    }

    @Test
    void shouldUpdateLocationTimestamp() {
        Technician technician = new Technician("John Doe");
        LocalDateTime before = LocalDateTime.now();
        
        technician.updateLocation(40.7128, -74.0060);
        
        LocalDateTime after = LocalDateTime.now();
        assertNotNull(technician.getLastLocationUpdate());
        assertTrue(technician.getLastLocationUpdate().isAfter(before.minusSeconds(1)));
        assertTrue(technician.getLastLocationUpdate().isBefore(after.plusSeconds(1)));
    }
}
