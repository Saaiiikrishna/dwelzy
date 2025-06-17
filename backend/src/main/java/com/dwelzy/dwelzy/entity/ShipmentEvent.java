package com.dwelzy.dwelzy.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "shipment_events")
public class ShipmentEvent extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;

    @Enumerated(EnumType.STRING)
    @Column(name = "event_type", nullable = false)
    private EventType eventType;

    @NotBlank
    @Size(max = 500)
    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "event_timestamp", nullable = false)
    private LocalDateTime eventTimestamp;

    @Size(max = 255)
    @Column(name = "location")
    private String location;

    @Column(name = "latitude", precision = 10, scale = 8)
    private BigDecimal latitude;

    @Column(name = "longitude", precision = 11, scale = 8)
    private BigDecimal longitude;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_user_id")
    private User createdBy;

    @Size(max = 1000)
    @Column(name = "additional_notes")
    private String additionalNotes;

    @Size(max = 255)
    @Column(name = "image_url")
    private String imageUrl;

    // Constructors
    public ShipmentEvent() {}

    public ShipmentEvent(Booking booking, EventType eventType, String description) {
        this.booking = booking;
        this.eventType = eventType;
        this.description = description;
        this.eventTimestamp = LocalDateTime.now();
    }

    // Getters and Setters
    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getEventTimestamp() {
        return eventTimestamp;
    }

    public void setEventTimestamp(LocalDateTime eventTimestamp) {
        this.eventTimestamp = eventTimestamp;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public String getAdditionalNotes() {
        return additionalNotes;
    }

    public void setAdditionalNotes(String additionalNotes) {
        this.additionalNotes = additionalNotes;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    // Enum for event types
    public enum EventType {
        BOOKING_CREATED("Booking was created"),
        BOOKING_CONFIRMED("Booking was confirmed"),
        DRIVER_ASSIGNED("Driver was assigned to booking"),
        PICKUP_SCHEDULED("Pickup was scheduled"),
        PICKUP_ATTEMPTED("Pickup was attempted"),
        PICKUP_COMPLETED("Package was picked up"),
        IN_TRANSIT("Package is in transit"),
        OUT_FOR_DELIVERY("Package is out for delivery"),
        DELIVERY_ATTEMPTED("Delivery was attempted"),
        DELIVERY_COMPLETED("Package was delivered"),
        DELIVERY_FAILED("Delivery failed"),
        BOOKING_CANCELLED("Booking was cancelled"),
        EXCEPTION("Exception occurred during shipment"),
        SCAN_EVENT("Package was scanned"),
        HUB_ARRIVAL("Package arrived at hub"),
        HUB_DEPARTURE("Package departed from hub");

        private final String description;

        EventType(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }
}
