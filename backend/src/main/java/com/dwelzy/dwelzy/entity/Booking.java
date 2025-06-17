package com.dwelzy.dwelzy.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "bookings")
public class Booking extends BaseEntity {

    @NotBlank
    @Size(max = 50)
    @Column(name = "booking_number", nullable = false, unique = true)
    private String bookingNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_id")
    private Driver driver;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pickup_hub_id")
    private Hub pickupHub;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_hub_id")
    private Hub deliveryHub;

    @NotBlank
    @Size(max = 255)
    @Column(name = "pickup_address", nullable = false)
    private String pickupAddress;

    @NotBlank
    @Size(max = 255)
    @Column(name = "delivery_address", nullable = false)
    private String deliveryAddress;

    @Column(name = "pickup_latitude", precision = 10, scale = 8)
    private BigDecimal pickupLatitude;

    @Column(name = "pickup_longitude", precision = 11, scale = 8)
    private BigDecimal pickupLongitude;

    @Column(name = "delivery_latitude", precision = 10, scale = 8)
    private BigDecimal deliveryLatitude;

    @Column(name = "delivery_longitude", precision = 11, scale = 8)
    private BigDecimal deliveryLongitude;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private BookingStatus status = BookingStatus.PENDING;

    @Column(name = "scheduled_pickup_time")
    private LocalDateTime scheduledPickupTime;

    @Column(name = "scheduled_delivery_time")
    private LocalDateTime scheduledDeliveryTime;

    @Column(name = "actual_pickup_time")
    private LocalDateTime actualPickupTime;

    @Column(name = "actual_delivery_time")
    private LocalDateTime actualDeliveryTime;

    @NotNull
    @Column(name = "total_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalPrice;

    @Column(name = "distance_km", precision = 8, scale = 2)
    private BigDecimal distanceKm;

    @Size(max = 500)
    @Column(name = "special_instructions")
    private String specialInstructions;

    @Size(max = 500)
    @Column(name = "notes")
    private String notes;

    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ShipmentEvent> shipmentEvents = new ArrayList<>();

    // Constructors
    public Booking() {}

    public Booking(String bookingNumber, User user, String pickupAddress, String deliveryAddress, BigDecimal totalPrice) {
        this.bookingNumber = bookingNumber;
        this.user = user;
        this.pickupAddress = pickupAddress;
        this.deliveryAddress = deliveryAddress;
        this.totalPrice = totalPrice;
    }

    // Getters and Setters
    public String getBookingNumber() {
        return bookingNumber;
    }

    public void setBookingNumber(String bookingNumber) {
        this.bookingNumber = bookingNumber;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public Hub getPickupHub() {
        return pickupHub;
    }

    public void setPickupHub(Hub pickupHub) {
        this.pickupHub = pickupHub;
    }

    public Hub getDeliveryHub() {
        return deliveryHub;
    }

    public void setDeliveryHub(Hub deliveryHub) {
        this.deliveryHub = deliveryHub;
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

    public BigDecimal getPickupLatitude() {
        return pickupLatitude;
    }

    public void setPickupLatitude(BigDecimal pickupLatitude) {
        this.pickupLatitude = pickupLatitude;
    }

    public BigDecimal getPickupLongitude() {
        return pickupLongitude;
    }

    public void setPickupLongitude(BigDecimal pickupLongitude) {
        this.pickupLongitude = pickupLongitude;
    }

    public BigDecimal getDeliveryLatitude() {
        return deliveryLatitude;
    }

    public void setDeliveryLatitude(BigDecimal deliveryLatitude) {
        this.deliveryLatitude = deliveryLatitude;
    }

    public BigDecimal getDeliveryLongitude() {
        return deliveryLongitude;
    }

    public void setDeliveryLongitude(BigDecimal deliveryLongitude) {
        this.deliveryLongitude = deliveryLongitude;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
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

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public BigDecimal getDistanceKm() {
        return distanceKm;
    }

    public void setDistanceKm(BigDecimal distanceKm) {
        this.distanceKm = distanceKm;
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

    public List<ShipmentEvent> getShipmentEvents() {
        return shipmentEvents;
    }

    public void setShipmentEvents(List<ShipmentEvent> shipmentEvents) {
        this.shipmentEvents = shipmentEvents;
    }

    // Enum for booking status
    public enum BookingStatus {
        PENDING("Booking created, awaiting confirmation"),
        CONFIRMED("Booking confirmed, awaiting driver assignment"),
        ASSIGNED("Driver assigned, awaiting pickup"),
        IN_TRANSIT("Package picked up, in transit"),
        DELIVERED("Package delivered successfully"),
        CANCELLED("Booking cancelled"),
        FAILED("Delivery failed");

        private final String description;

        BookingStatus(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }
}
