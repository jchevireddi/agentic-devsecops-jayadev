package com.fieldservice.api.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UpdateLocationRequestTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void shouldCreateValidRequest() {
        UpdateLocationRequest request = new UpdateLocationRequest(40.7128, -74.0060);
        
        Set<ConstraintViolation<UpdateLocationRequest>> violations = validator.validate(request);
        
        assertTrue(violations.isEmpty());
        assertEquals(40.7128, request.getLatitude());
        assertEquals(-74.0060, request.getLongitude());
    }

    @Test
    void shouldCreateRequestWithDefaultConstructor() {
        UpdateLocationRequest request = new UpdateLocationRequest();
        request.setLatitude(40.7128);
        request.setLongitude(-74.0060);
        
        assertEquals(40.7128, request.getLatitude());
        assertEquals(-74.0060, request.getLongitude());
    }

    @Test
    void shouldFailValidationWhenLatitudeIsNull() {
        UpdateLocationRequest request = new UpdateLocationRequest(null, -74.0060);
        
        Set<ConstraintViolation<UpdateLocationRequest>> violations = validator.validate(request);
        
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Latitude is required")));
    }

    @Test
    void shouldFailValidationWhenLongitudeIsNull() {
        UpdateLocationRequest request = new UpdateLocationRequest(40.7128, null);
        
        Set<ConstraintViolation<UpdateLocationRequest>> violations = validator.validate(request);
        
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Longitude is required")));
    }

    @Test
    void shouldFailValidationWhenLatitudeTooHigh() {
        UpdateLocationRequest request = new UpdateLocationRequest(91.0, -74.0060);
        
        Set<ConstraintViolation<UpdateLocationRequest>> violations = validator.validate(request);
        
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Latitude must be between -90 and 90")));
    }

    @Test
    void shouldFailValidationWhenLatitudeTooLow() {
        UpdateLocationRequest request = new UpdateLocationRequest(-91.0, -74.0060);
        
        Set<ConstraintViolation<UpdateLocationRequest>> violations = validator.validate(request);
        
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Latitude must be between -90 and 90")));
    }

    @Test
    void shouldFailValidationWhenLongitudeTooHigh() {
        UpdateLocationRequest request = new UpdateLocationRequest(40.7128, 181.0);
        
        Set<ConstraintViolation<UpdateLocationRequest>> violations = validator.validate(request);
        
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Longitude must be between -180 and 180")));
    }

    @Test
    void shouldFailValidationWhenLongitudeTooLow() {
        UpdateLocationRequest request = new UpdateLocationRequest(40.7128, -181.0);
        
        Set<ConstraintViolation<UpdateLocationRequest>> violations = validator.validate(request);
        
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Longitude must be between -180 and 180")));
    }

    @Test
    void shouldAcceptBoundaryLatitudeMax() {
        UpdateLocationRequest request = new UpdateLocationRequest(90.0, 0.0);
        
        Set<ConstraintViolation<UpdateLocationRequest>> violations = validator.validate(request);
        
        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldAcceptBoundaryLatitudeMin() {
        UpdateLocationRequest request = new UpdateLocationRequest(-90.0, 0.0);
        
        Set<ConstraintViolation<UpdateLocationRequest>> violations = validator.validate(request);
        
        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldAcceptBoundaryLongitudeMax() {
        UpdateLocationRequest request = new UpdateLocationRequest(0.0, 180.0);
        
        Set<ConstraintViolation<UpdateLocationRequest>> violations = validator.validate(request);
        
        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldAcceptBoundaryLongitudeMin() {
        UpdateLocationRequest request = new UpdateLocationRequest(0.0, -180.0);
        
        Set<ConstraintViolation<UpdateLocationRequest>> violations = validator.validate(request);
        
        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldAcceptZeroCoordinates() {
        UpdateLocationRequest request = new UpdateLocationRequest(0.0, 0.0);
        
        Set<ConstraintViolation<UpdateLocationRequest>> violations = validator.validate(request);
        
        assertTrue(violations.isEmpty());
    }
}
