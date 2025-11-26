package com.fieldservice.api.service;

import com.fieldservice.api.entity.Technician;
import com.fieldservice.api.enums.TechnicianAvailability;
import com.fieldservice.api.repository.TechnicianRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TechnicianServiceTest {

    @Mock
    private TechnicianRepository technicianRepository;

    @InjectMocks
    private TechnicianService technicianService;

    private Technician technician;
    private UUID technicianId;

    @BeforeEach
    void setUp() {
        technicianId = UUID.randomUUID();
        technician = new Technician("John Doe");
        technician.setId(technicianId);
        technician.setEmail("john@example.com");
    }

    @Test
    void shouldCreateTechnician() {
        when(technicianRepository.save(any(Technician.class))).thenReturn(technician);
        
        Technician created = technicianService.createTechnician(technician);
        
        assertNotNull(created);
        assertEquals("John Doe", created.getName());
        verify(technicianRepository, times(1)).save(technician);
    }

    @Test
    void shouldFindTechnicianById() {
        when(technicianRepository.findById(technicianId)).thenReturn(Optional.of(technician));
        
        Optional<Technician> found = technicianService.findById(technicianId);
        
        assertTrue(found.isPresent());
        assertEquals("John Doe", found.get().getName());
        verify(technicianRepository, times(1)).findById(technicianId);
    }

    @Test
    void shouldReturnEmptyWhenTechnicianNotFound() {
        UUID unknownId = UUID.randomUUID();
        when(technicianRepository.findById(unknownId)).thenReturn(Optional.empty());
        
        Optional<Technician> found = technicianService.findById(unknownId);
        
        assertFalse(found.isPresent());
        verify(technicianRepository, times(1)).findById(unknownId);
    }

    @Test
    void shouldFindAllTechnicians() {
        Technician technician2 = new Technician("Jane Smith");
        technician2.setId(UUID.randomUUID());
        List<Technician> technicians = Arrays.asList(technician, technician2);
        
        when(technicianRepository.findAll()).thenReturn(technicians);
        
        List<Technician> found = technicianService.findAll();
        
        assertEquals(2, found.size());
        verify(technicianRepository, times(1)).findAll();
    }

    @Test
    void shouldReturnEmptyListWhenNoTechnicians() {
        when(technicianRepository.findAll()).thenReturn(List.of());
        
        List<Technician> found = technicianService.findAll();
        
        assertTrue(found.isEmpty());
        verify(technicianRepository, times(1)).findAll();
    }

    @Test
    void shouldUpdateLocation() {
        Double latitude = 40.7128;
        Double longitude = -74.0060;
        
        when(technicianRepository.findById(technicianId)).thenReturn(Optional.of(technician));
        when(technicianRepository.save(any(Technician.class))).thenAnswer(invocation -> invocation.getArgument(0));
        
        Optional<Technician> updated = technicianService.updateLocation(technicianId, latitude, longitude);
        
        assertTrue(updated.isPresent());
        assertEquals(latitude, updated.get().getLatitude());
        assertEquals(longitude, updated.get().getLongitude());
        assertNotNull(updated.get().getLastLocationUpdate());
        verify(technicianRepository, times(1)).findById(technicianId);
        verify(technicianRepository, times(1)).save(technician);
    }

    @Test
    void shouldReturnEmptyWhenUpdatingLocationForNonExistentTechnician() {
        UUID unknownId = UUID.randomUUID();
        when(technicianRepository.findById(unknownId)).thenReturn(Optional.empty());
        
        Optional<Technician> updated = technicianService.updateLocation(unknownId, 40.7128, -74.0060);
        
        assertFalse(updated.isPresent());
        verify(technicianRepository, times(1)).findById(unknownId);
        verify(technicianRepository, never()).save(any());
    }

    @Test
    void shouldDeleteTechnician() {
        doNothing().when(technicianRepository).deleteById(technicianId);
        
        technicianService.deleteById(technicianId);
        
        verify(technicianRepository, times(1)).deleteById(technicianId);
    }

    @Test
    void shouldUpdateLocationWithZeroCoordinates() {
        when(technicianRepository.findById(technicianId)).thenReturn(Optional.of(technician));
        when(technicianRepository.save(any(Technician.class))).thenAnswer(invocation -> invocation.getArgument(0));
        
        Optional<Technician> updated = technicianService.updateLocation(technicianId, 0.0, 0.0);
        
        assertTrue(updated.isPresent());
        assertEquals(0.0, updated.get().getLatitude());
        assertEquals(0.0, updated.get().getLongitude());
    }

    @Test
    void shouldUpdateLocationWithNegativeCoordinates() {
        when(technicianRepository.findById(technicianId)).thenReturn(Optional.of(technician));
        when(technicianRepository.save(any(Technician.class))).thenAnswer(invocation -> invocation.getArgument(0));
        
        Optional<Technician> updated = technicianService.updateLocation(technicianId, -33.8688, -151.2093);
        
        assertTrue(updated.isPresent());
        assertEquals(-33.8688, updated.get().getLatitude());
        assertEquals(-151.2093, updated.get().getLongitude());
    }

    @Test
    void shouldUpdateLocationWithBoundaryValues() {
        when(technicianRepository.findById(technicianId)).thenReturn(Optional.of(technician));
        when(technicianRepository.save(any(Technician.class))).thenAnswer(invocation -> invocation.getArgument(0));
        
        Optional<Technician> updated = technicianService.updateLocation(technicianId, 90.0, 180.0);
        
        assertTrue(updated.isPresent());
        assertEquals(90.0, updated.get().getLatitude());
        assertEquals(180.0, updated.get().getLongitude());
    }

    @Test
    void shouldUpdateLocationWithMinBoundaryValues() {
        when(technicianRepository.findById(technicianId)).thenReturn(Optional.of(technician));
        when(technicianRepository.save(any(Technician.class))).thenAnswer(invocation -> invocation.getArgument(0));
        
        Optional<Technician> updated = technicianService.updateLocation(technicianId, -90.0, -180.0);
        
        assertTrue(updated.isPresent());
        assertEquals(-90.0, updated.get().getLatitude());
        assertEquals(-180.0, updated.get().getLongitude());
    }
}
