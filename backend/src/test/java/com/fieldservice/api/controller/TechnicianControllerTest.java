package com.fieldservice.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fieldservice.api.dto.UpdateLocationRequest;
import com.fieldservice.api.entity.Technician;
import com.fieldservice.api.enums.TechnicianAvailability;
import com.fieldservice.api.service.TechnicianService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TechnicianController.class)
class TechnicianControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TechnicianService technicianService;

    private Technician technician;
    private UUID technicianId;

    @BeforeEach
    void setUp() {
        technicianId = UUID.randomUUID();
        technician = new Technician("John Doe");
        technician.setId(technicianId);
        technician.setEmail("john@example.com");
        technician.setAvailability(TechnicianAvailability.AVAILABLE);
    }

    @Test
    void shouldCreateTechnician() throws Exception {
        when(technicianService.createTechnician(any(Technician.class))).thenReturn(technician);

        mockMvc.perform(post("/api/technicians")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(technician)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("John Doe")))
                .andExpect(jsonPath("$.email", is("john@example.com")));

        verify(technicianService, times(1)).createTechnician(any(Technician.class));
    }

    @Test
    void shouldFailToCreateTechnicianWithBlankName() throws Exception {
        Technician invalidTechnician = new Technician();
        invalidTechnician.setName("");

        mockMvc.perform(post("/api/technicians")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidTechnician)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldGetAllTechnicians() throws Exception {
        Technician technician2 = new Technician("Jane Smith");
        technician2.setId(UUID.randomUUID());
        List<Technician> technicians = Arrays.asList(technician, technician2);

        when(technicianService.findAll()).thenReturn(technicians);

        mockMvc.perform(get("/api/technicians"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("John Doe")))
                .andExpect(jsonPath("$[1].name", is("Jane Smith")));

        verify(technicianService, times(1)).findAll();
    }

    @Test
    void shouldReturnEmptyListWhenNoTechnicians() throws Exception {
        when(technicianService.findAll()).thenReturn(List.of());

        mockMvc.perform(get("/api/technicians"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void shouldGetTechnicianById() throws Exception {
        when(technicianService.findById(technicianId)).thenReturn(Optional.of(technician));

        mockMvc.perform(get("/api/technicians/{id}", technicianId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("John Doe")))
                .andExpect(jsonPath("$.email", is("john@example.com")));

        verify(technicianService, times(1)).findById(technicianId);
    }

    @Test
    void shouldReturn404WhenTechnicianNotFound() throws Exception {
        UUID unknownId = UUID.randomUUID();
        when(technicianService.findById(unknownId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/technicians/{id}", unknownId))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldUpdateLocation() throws Exception {
        UpdateLocationRequest request = new UpdateLocationRequest(40.7128, -74.0060);
        technician.updateLocation(40.7128, -74.0060);

        when(technicianService.updateLocation(eq(technicianId), eq(40.7128), eq(-74.0060)))
                .thenReturn(Optional.of(technician));

        mockMvc.perform(put("/api/technicians/{id}/location", technicianId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.latitude", is(40.7128)))
                .andExpect(jsonPath("$.longitude", is(-74.0060)))
                .andExpect(jsonPath("$.lastLocationUpdate", notNullValue()));

        verify(technicianService, times(1)).updateLocation(technicianId, 40.7128, -74.0060);
    }

    @Test
    void shouldReturn404WhenUpdatingLocationForNonExistentTechnician() throws Exception {
        UUID unknownId = UUID.randomUUID();
        UpdateLocationRequest request = new UpdateLocationRequest(40.7128, -74.0060);

        when(technicianService.updateLocation(eq(unknownId), anyDouble(), anyDouble()))
                .thenReturn(Optional.empty());

        mockMvc.perform(put("/api/technicians/{id}/location", unknownId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldRejectInvalidLatitude() throws Exception {
        UpdateLocationRequest request = new UpdateLocationRequest(91.0, -74.0060);

        mockMvc.perform(put("/api/technicians/{id}/location", technicianId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldRejectLatitudeTooLow() throws Exception {
        UpdateLocationRequest request = new UpdateLocationRequest(-91.0, -74.0060);

        mockMvc.perform(put("/api/technicians/{id}/location", technicianId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldRejectInvalidLongitude() throws Exception {
        UpdateLocationRequest request = new UpdateLocationRequest(40.7128, 181.0);

        mockMvc.perform(put("/api/technicians/{id}/location", technicianId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldRejectLongitudeTooLow() throws Exception {
        UpdateLocationRequest request = new UpdateLocationRequest(40.7128, -181.0);

        mockMvc.perform(put("/api/technicians/{id}/location", technicianId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldRejectNullLatitude() throws Exception {
        UpdateLocationRequest request = new UpdateLocationRequest(null, -74.0060);

        mockMvc.perform(put("/api/technicians/{id}/location", technicianId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldRejectNullLongitude() throws Exception {
        UpdateLocationRequest request = new UpdateLocationRequest(40.7128, null);

        mockMvc.perform(put("/api/technicians/{id}/location", technicianId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldAcceptBoundaryLatitudeMax() throws Exception {
        UpdateLocationRequest request = new UpdateLocationRequest(90.0, 0.0);
        technician.updateLocation(90.0, 0.0);

        when(technicianService.updateLocation(eq(technicianId), eq(90.0), eq(0.0)))
                .thenReturn(Optional.of(technician));

        mockMvc.perform(put("/api/technicians/{id}/location", technicianId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void shouldAcceptBoundaryLatitudeMin() throws Exception {
        UpdateLocationRequest request = new UpdateLocationRequest(-90.0, 0.0);
        technician.updateLocation(-90.0, 0.0);

        when(technicianService.updateLocation(eq(technicianId), eq(-90.0), eq(0.0)))
                .thenReturn(Optional.of(technician));

        mockMvc.perform(put("/api/technicians/{id}/location", technicianId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void shouldAcceptBoundaryLongitudeMax() throws Exception {
        UpdateLocationRequest request = new UpdateLocationRequest(0.0, 180.0);
        technician.updateLocation(0.0, 180.0);

        when(technicianService.updateLocation(eq(technicianId), eq(0.0), eq(180.0)))
                .thenReturn(Optional.of(technician));

        mockMvc.perform(put("/api/technicians/{id}/location", technicianId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void shouldAcceptBoundaryLongitudeMin() throws Exception {
        UpdateLocationRequest request = new UpdateLocationRequest(0.0, -180.0);
        technician.updateLocation(0.0, -180.0);

        when(technicianService.updateLocation(eq(technicianId), eq(0.0), eq(-180.0)))
                .thenReturn(Optional.of(technician));

        mockMvc.perform(put("/api/technicians/{id}/location", technicianId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void shouldDeleteTechnician() throws Exception {
        doNothing().when(technicianService).deleteById(technicianId);

        mockMvc.perform(delete("/api/technicians/{id}", technicianId))
                .andExpect(status().isNoContent());

        verify(technicianService, times(1)).deleteById(technicianId);
    }
}
