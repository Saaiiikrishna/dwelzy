package com.dwelzy.dwelzy.dto;

import com.dwelzy.dwelzy.entity.ShipmentEvent;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ScanResponse {

    private Long eventId;
    private String bookingNumber;
    private String eventType;
    private String description;
    private LocalDateTime eventTimestamp;
    private String location;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String notes;
    private String scannedBy;
    private String currentStatus;
    private String nextExpectedEvent;

    // Constructors
    public ScanResponse() {}

    public ScanResponse(ShipmentEvent event) {
        this.eventId = event.getId();
        this.bookingNumber = event.getBooking().getBookingNumber();
        this.eventType = event.getEventType().name();
        this.description = event.getDescription();
        this.eventTimestamp = event.getEventTimestamp();
        this.location = event.getLocation();
        this.latitude = event.getLatitude();
        this.longitude = event.getLongitude();
        this.notes = event.getAdditionalNotes();
        this.currentStatus = event.getBooking().getStatus().name();
        
        if (event.getCreatedBy() != null) {
            this.scannedBy = event.getCreatedBy().getFullName();
        }
        
        this.nextExpectedEvent = determineNextExpectedEvent(event.getBooking().getStatus());
    }

    private String determineNextExpectedEvent(com.dwelzy.dwelzy.entity.Booking.BookingStatus status) {
        return switch (status) {
            case PENDING -> "BOOKING_CONFIRMED";
            case CONFIRMED -> "DRIVER_ASSIGNED";
            case ASSIGNED -> "PICKUP_COMPLETED";
            case IN_TRANSIT -> "DELIVERY_COMPLETED";
            case DELIVERED -> "COMPLETED";
            case CANCELLED -> "NONE";
            case FAILED -> "RETRY_DELIVERY";
        };
    }

    // Getters and Setters
    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public String getBookingNumber() {
        return bookingNumber;
    }

    public void setBookingNumber(String bookingNumber) {
        this.bookingNumber = bookingNumber;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
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

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getScannedBy() {
        return scannedBy;
    }

    public void setScannedBy(String scannedBy) {
        this.scannedBy = scannedBy;
    }

    public String getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(String currentStatus) {
        this.currentStatus = currentStatus;
    }

    public String getNextExpectedEvent() {
        return nextExpectedEvent;
    }

    public void setNextExpectedEvent(String nextExpectedEvent) {
        this.nextExpectedEvent = nextExpectedEvent;
    }
}
