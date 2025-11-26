package com.fieldservice.api.controller;

import com.fieldservice.api.dto.UpdateLocationRequest;
import com.fieldservice.api.entity.Technician;
import com.fieldservice.api.service.TechnicianService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * REST controller for Technician operations.
 */
@RestController
@RequestMapping("/api/technicians")
public class TechnicianController {

    private final TechnicianService technicianService;

    public TechnicianController(TechnicianService technicianService) {
        this.technicianService = technicianService;
    }

    /**
     * Creates a new technician.
     *
     * @param technician the technician to create
     * @return the created technician
     */
    @PostMapping
    public ResponseEntity<Technician> createTechnician(@Valid @RequestBody Technician technician) {
        Technician created = technicianService.createTechnician(technician);
        return ResponseEntity.status(201).body(created);
    }

    /**
     * Gets all technicians.
     *
     * @return list of all technicians
     */
    @GetMapping
    public ResponseEntity<List<Technician>> getAllTechnicians() {
        return ResponseEntity.ok(technicianService.findAll());
    }

    /**
     * Gets a technician by ID.
     *
     * @param id the technician ID
     * @return the technician if found
     */
    @GetMapping("/{id}")
    public ResponseEntity<Technician> getTechnicianById(@PathVariable UUID id) {
        return technicianService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Updates a technician's location.
     *
     * @param id      the technician ID
     * @param request the location update request
     * @return the updated technician
     */
    @PutMapping("/{id}/location")
    public ResponseEntity<Technician> updateLocation(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateLocationRequest request) {
        return technicianService.updateLocation(id, request.getLatitude(), request.getLongitude())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Deletes a technician by ID.
     *
     * @param id the technician ID
     * @return no content response
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTechnician(@PathVariable UUID id) {
        technicianService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
