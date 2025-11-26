package com.fieldservice.api.service;

import com.fieldservice.api.entity.Technician;
import com.fieldservice.api.repository.TechnicianRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service layer for Technician operations.
 */
@Service
@Transactional
public class TechnicianService {

    private final TechnicianRepository technicianRepository;

    public TechnicianService(TechnicianRepository technicianRepository) {
        this.technicianRepository = technicianRepository;
    }

    /**
     * Creates a new technician.
     *
     * @param technician the technician to create
     * @return the created technician
     */
    public Technician createTechnician(Technician technician) {
        return technicianRepository.save(technician);
    }

    /**
     * Finds a technician by ID.
     *
     * @param id the technician ID
     * @return the technician if found
     */
    @Transactional(readOnly = true)
    public Optional<Technician> findById(UUID id) {
        return technicianRepository.findById(id);
    }

    /**
     * Returns all technicians.
     *
     * @return list of all technicians
     */
    @Transactional(readOnly = true)
    public List<Technician> findAll() {
        return technicianRepository.findAll();
    }

    /**
     * Updates a technician's location.
     *
     * @param id        the technician ID
     * @param latitude  the new latitude
     * @param longitude the new longitude
     * @return the updated technician if found
     */
    public Optional<Technician> updateLocation(UUID id, Double latitude, Double longitude) {
        return technicianRepository.findById(id)
                .map(technician -> {
                    technician.updateLocation(latitude, longitude);
                    return technicianRepository.save(technician);
                });
    }

    /**
     * Deletes a technician by ID.
     *
     * @param id the technician ID
     */
    public void deleteById(UUID id) {
        technicianRepository.deleteById(id);
    }
}
