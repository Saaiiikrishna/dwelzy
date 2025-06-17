package com.dwelzy.dwelzy.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "hubs")
public class Hub extends BaseEntity {

    @NotBlank
    @Size(max = 100)
    @Column(name = "name", nullable = false)
    private String name;

    @NotBlank
    @Size(max = 255)
    @Column(name = "address", nullable = false)
    private String address;

    @Size(max = 100)
    @Column(name = "city")
    private String city;

    @Size(max = 50)
    @Column(name = "state")
    private String state;

    @Size(max = 20)
    @Column(name = "postal_code")
    private String postalCode;

    @Size(max = 50)
    @Column(name = "country")
    private String country;

    @Column(name = "latitude", precision = 10, scale = 8)
    private BigDecimal latitude;

    @Column(name = "longitude", precision = 11, scale = 8)
    private BigDecimal longitude;

    @Size(max = 15)
    @Column(name = "phone_number")
    private String phoneNumber;

    @Size(max = 100)
    @Column(name = "email")
    private String email;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "capacity")
    private Integer capacity;

    @Size(max = 500)
    @Column(name = "operating_hours")
    private String operatingHours;

    @OneToMany(mappedBy = "hub", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Driver> drivers = new ArrayList<>();

    @OneToMany(mappedBy = "pickupHub", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Booking> pickupBookings = new ArrayList<>();

    @OneToMany(mappedBy = "deliveryHub", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Booking> deliveryBookings = new ArrayList<>();

    // Constructors
    public Hub() {}

    public Hub(String name, String address) {
        this.name = name;
        this.address = address;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public String getOperatingHours() {
        return operatingHours;
    }

    public void setOperatingHours(String operatingHours) {
        this.operatingHours = operatingHours;
    }

    public List<Driver> getDrivers() {
        return drivers;
    }

    public void setDrivers(List<Driver> drivers) {
        this.drivers = drivers;
    }

    public List<Booking> getPickupBookings() {
        return pickupBookings;
    }

    public void setPickupBookings(List<Booking> pickupBookings) {
        this.pickupBookings = pickupBookings;
    }

    public List<Booking> getDeliveryBookings() {
        return deliveryBookings;
    }

    public void setDeliveryBookings(List<Booking> deliveryBookings) {
        this.deliveryBookings = deliveryBookings;
    }

    public String getFullAddress() {
        StringBuilder fullAddress = new StringBuilder(address);
        if (city != null) fullAddress.append(", ").append(city);
        if (state != null) fullAddress.append(", ").append(state);
        if (postalCode != null) fullAddress.append(" ").append(postalCode);
        if (country != null) fullAddress.append(", ").append(country);
        return fullAddress.toString();
    }
}
