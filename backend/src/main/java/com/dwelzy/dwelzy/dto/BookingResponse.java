package com.dwelzy.dwelzy.dto;

import com.dwelzy.dwelzy.entity.Booking;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class BookingResponse {
    private Long id;
    private String bookingNumber;
    private String pickupAddress;
    private String deliveryAddress;
    private BigDecimal totalPrice;
    private String status;
    private LocalDateTime scheduledPickupTime;
    private LocalDateTime scheduledDeliveryTime;
    private LocalDateTime actualPickupTime;
    private LocalDateTime actualDeliveryTime;
    private String specialInstructions;
    private String notes;
    private BigDecimal distanceKm;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Driver information
    private String driverName;
    private String driverPhone;

    // Hub information
    private String pickupHubName;
    private String deliveryHubName;

    public BookingResponse() {}

    public BookingResponse(Booking booking) {
        this.id = booking.getId();
        this.bookingNumber = booking.getBookingNumber();
        this.pickupAddress = booking.getPickupAddress();
        this.deliveryAddress = booking.getDeliveryAddress();
        this.totalPrice = booking.getTotalPrice();
        this.status = booking.getStatus().name();
        this.scheduledPickupTime = booking.getScheduledPickupTime();
        this.scheduledDeliveryTime = booking.getScheduledDeliveryTime();
        this.actualPickupTime = booking.getActualPickupTime();
        this.actualDeliveryTime = booking.getActualDeliveryTime();
        this.specialInstructions = booking.getSpecialInstructions();
        this.notes = booking.getNotes();
        this.distanceKm = booking.getDistanceKm();
        this.createdAt = booking.getCreatedAt();
        this.updatedAt = booking.getUpdatedAt();

        // Set driver information if available
        if (booking.getDriver() != null && booking.getDriver().getUser() != null) {
            this.driverName = booking.getDriver().getUser().getFullName();
            this.driverPhone = booking.getDriver().getUser().getPhoneNumber();
        }

        // Set hub information if available
        if (booking.getPickupHub() != null) {
            this.pickupHubName = booking.getPickupHub().getName();
        }
        if (booking.getDeliveryHub() != null) {
            this.deliveryHubName = booking.getDeliveryHub().getName();
        }
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBookingNumber() {
        return bookingNumber;
    }

    public void setBookingNumber(String bookingNumber) {
        this.bookingNumber = bookingNumber;
    }

    public String getPickupAddress() {
        return pickupAddress;
    }

    public void setPickupAddress(String pickupAddress) {
        this.pickupAddress = pickupAddress;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getScheduledPickupTime() {
        return scheduledPickupTime;
    }

    public void setScheduledPickupTime(LocalDateTime scheduledPickupTime) {
        this.scheduledPickupTime = scheduledPickupTime;
    }

    public LocalDateTime getScheduledDeliveryTime() {
        return scheduledDeliveryTime;
    }

    public void setScheduledDeliveryTime(LocalDateTime scheduledDeliveryTime) {
        this.scheduledDeliveryTime = scheduledDeliveryTime;
    }

    public LocalDateTime getActualPickupTime() {
        return actualPickupTime;
    }

    public void setActualPickupTime(LocalDateTime actualPickupTime) {
        this.actualPickupTime = actualPickupTime;
    }

    public LocalDateTime getActualDeliveryTime() {
        return actualDeliveryTime;
    }

    public void setActualDeliveryTime(LocalDateTime actualDeliveryTime) {
        this.actualDeliveryTime = actualDeliveryTime;
    }

    public String getSpecialInstructions() {
        return specialInstructions;
    }

    public void setSpecialInstructions(String specialInstructions) {
        this.specialInstructions = specialInstructions;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public BigDecimal getDistanceKm() {
        return distanceKm;
    }

    public void setDistanceKm(BigDecimal distanceKm) {
        this.distanceKm = distanceKm;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getDriverPhone() {
        return driverPhone;
    }

    public void setDriverPhone(String driverPhone) {
        this.driverPhone = driverPhone;
    }

    public String getPickupHubName() {
        return pickupHubName;
    }

    public void setPickupHubName(String pickupHubName) {
        this.pickupHubName = pickupHubName;
    }

    public String getDeliveryHubName() {
        return deliveryHubName;
    }

    public void setDeliveryHubName(String deliveryHubName) {
        this.deliveryHubName = deliveryHubName;
    }
}
