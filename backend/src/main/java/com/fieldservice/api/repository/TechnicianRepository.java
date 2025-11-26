package com.fieldservice.api.repository;

import com.fieldservice.api.entity.Technician;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Repository interface for Technician entity operations.
 */
@Repository
public interface TechnicianRepository extends JpaRepository<Technician, UUID> {
}
