package com.fieldservice.api.entity;

import com.fieldservice.api.enums.TechnicianAvailability;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Represents a field technician in the system.
 * Contains base information and location tracking fields.
 */
@Entity
@Table(name = "technicians")
public class Technician {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank(message = "Name is required")
    @Column(nullable = false)
    private String name;

    @Email(message = "Email should be valid")
    @Column(unique = true)
    private String email;

    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TechnicianAvailability availability = TechnicianAvailability.AVAILABLE;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // Location fields for real-time tracking
    @DecimalMin(value = "-90.0", message = "Latitude must be between -90 and 90")
    @DecimalMax(value = "90.0", message = "Latitude must be between -90 and 90")
    private Double latitude;

    @DecimalMin(value = "-180.0", message = "Longitude must be between -180 and 180")
    @DecimalMax(value = "180.0", message = "Longitude must be between -180 and 180")
    private Double longitude;

    private LocalDateTime lastLocationUpdate;

    public Technician() {
    }

    public Technician(String name) {
        this.name = name;
        this.availability = TechnicianAvailability.AVAILABLE;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public TechnicianAvailability getAvailability() {
        return availability;
    }

    public void setAvailability(TechnicianAvailability availability) {
        this.availability = availability;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public LocalDateTime getLastLocationUpdate() {
        return lastLocationUpdate;
    }

    public void setLastLocationUpdate(LocalDateTime lastLocationUpdate) {
        this.lastLocationUpdate = lastLocationUpdate;
    }

    /**
     * Updates the technician's location with the given coordinates.
     * Also updates the lastLocationUpdate timestamp.
     *
     * @param latitude  the latitude coordinate
     * @param longitude the longitude coordinate
     */
    public void updateLocation(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.lastLocationUpdate = LocalDateTime.now();
    }
}
