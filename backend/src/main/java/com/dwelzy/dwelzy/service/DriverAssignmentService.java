package com.dwelzy.dwelzy.service;

import com.dwelzy.dwelzy.entity.Booking;
import com.dwelzy.dwelzy.entity.Driver;
import com.dwelzy.dwelzy.entity.Hub;
import com.dwelzy.dwelzy.entity.ShipmentEvent;
import com.dwelzy.dwelzy.repository.BookingRepository;
import com.dwelzy.dwelzy.repository.DriverRepository;
import com.dwelzy.dwelzy.repository.HubRepository;
import com.dwelzy.dwelzy.repository.ShipmentEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class DriverAssignmentService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private HubRepository hubRepository;

    @Autowired
    private ShipmentEventRepository shipmentEventRepository;

    /**
     * Scheduled task to automatically assign drivers to pending bookings
     * Runs every 5 minutes
     */
    @Scheduled(fixedRate = 300000) // 5 minutes
    @Async
    public void autoAssignDrivers() {
        List<Booking> pendingBookings = bookingRepository.findUnassignedBookingsByStatus(Booking.BookingStatus.CONFIRMED);
        
        for (Booking booking : pendingBookings) {
            try {
                assignDriverToBooking(booking.getId());
            } catch (Exception e) {
                System.err.println("Failed to assign driver to booking " + booking.getId() + ": " + e.getMessage());
            }
        }
    }

    /**
     * Manually assign a driver to a booking
     */
    public Booking assignDriverToBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found: " + bookingId));

        if (booking.getDriver() != null) {
            throw new RuntimeException("Booking already has a driver assigned");
        }

        // Find the best available driver
        Driver assignedDriver = findBestAvailableDriver(booking);
        
        if (assignedDriver == null) {
            throw new RuntimeException("No available drivers found for this booking");
        }

        // Assign driver to booking
        booking.setDriver(assignedDriver);
        booking.setStatus(Booking.BookingStatus.ASSIGNED);
        
        // Update driver status
        assignedDriver.setStatus(Driver.DriverStatus.BUSY);
        
        // Save changes
        booking = bookingRepository.save(booking);
        driverRepository.save(assignedDriver);

        // Create shipment event
        createShipmentEvent(booking, ShipmentEvent.EventType.DRIVER_ASSIGNED, 
                          "Driver " + assignedDriver.getUser().getFullName() + " assigned to booking");

        return booking;
    }

    /**
     * Find the best available driver for a booking based on various criteria
     */
    private Driver findBestAvailableDriver(Booking booking) {
        // Get all available verified drivers
        List<Driver> availableDrivers = driverRepository.findAvailableVerifiedDrivers();
        
        if (availableDrivers.isEmpty()) {
            return null;
        }

        Driver bestDriver = null;
        double bestScore = -1;

        for (Driver driver : availableDrivers) {
            double score = calculateDriverScore(driver, booking);
            if (score > bestScore) {
                bestScore = score;
                bestDriver = driver;
            }
        }

        return bestDriver;
    }

    /**
     * Calculate a score for a driver based on various factors
     */
    private double calculateDriverScore(Driver driver, Booking booking) {
        double score = 0;

        // Factor 1: Driver rating (0-5 scale, weight: 30%)
        if (driver.getRating() != null) {
            score += (driver.getRating() / 5.0) * 0.3;
        } else {
            score += 0.15; // Default score for new drivers
        }

        // Factor 2: Experience (total deliveries, weight: 20%)
        int deliveries = driver.getTotalDeliveries() != null ? driver.getTotalDeliveries() : 0;
        double experienceScore = Math.min(deliveries / 100.0, 1.0); // Max score at 100 deliveries
        score += experienceScore * 0.2;

        // Factor 3: Proximity to pickup location (weight: 40%)
        double proximityScore = calculateProximityScore(driver, booking);
        score += proximityScore * 0.4;

        // Factor 4: Hub compatibility (weight: 10%)
        double hubScore = calculateHubCompatibilityScore(driver, booking);
        score += hubScore * 0.1;

        return score;
    }

    /**
     * Calculate proximity score based on distance from driver's hub to pickup location
     */
    private double calculateProximityScore(Driver driver, Booking booking) {
        if (driver.getHub() == null || 
            booking.getPickupLatitude() == null || booking.getPickupLongitude() == null ||
            driver.getHub().getLatitude() == null || driver.getHub().getLongitude() == null) {
            return 0.5; // Default score if location data is missing
        }

        double distance = calculateDistance(
            driver.getHub().getLatitude(), driver.getHub().getLongitude(),
            booking.getPickupLatitude(), booking.getPickupLongitude()
        );

        // Score decreases with distance (max score for distance <= 5km, min score for distance >= 50km)
        if (distance <= 5) {
            return 1.0;
        } else if (distance >= 50) {
            return 0.1;
        } else {
            return 1.0 - ((distance - 5) / 45) * 0.9;
        }
    }

    /**
     * Calculate hub compatibility score
     */
    private double calculateHubCompatibilityScore(Driver driver, Booking booking) {
        if (driver.getHub() == null) {
            return 0.5; // Default score if no hub assigned
        }

        // If pickup hub is set and matches driver's hub, give bonus
        if (booking.getPickupHub() != null && 
            booking.getPickupHub().getId().equals(driver.getHub().getId())) {
            return 1.0;
        }

        // If delivery hub is set and matches driver's hub, give partial bonus
        if (booking.getDeliveryHub() != null && 
            booking.getDeliveryHub().getId().equals(driver.getHub().getId())) {
            return 0.8;
        }

        return 0.5; // Default score
    }

    /**
     * Assign pickup and delivery hubs to a booking based on location
     */
    public Booking assignHubsToBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found: " + bookingId));

        // Find nearest hub for pickup
        if (booking.getPickupLatitude() != null && booking.getPickupLongitude() != null) {
            List<Hub> nearestHubs = hubRepository.findNearestHubs(
                booking.getPickupLatitude(), booking.getPickupLongitude()
            );
            if (!nearestHubs.isEmpty()) {
                booking.setPickupHub(nearestHubs.get(0));
            }
        }

        // Find nearest hub for delivery
        if (booking.getDeliveryLatitude() != null && booking.getDeliveryLongitude() != null) {
            List<Hub> nearestHubs = hubRepository.findNearestHubs(
                booking.getDeliveryLatitude(), booking.getDeliveryLongitude()
            );
            if (!nearestHubs.isEmpty()) {
                booking.setDeliveryHub(nearestHubs.get(0));
            }
        }

        return bookingRepository.save(booking);
    }

    /**
     * Release a driver from a booking (when delivery is completed or cancelled)
     */
    public void releaseDriver(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found: " + bookingId));

        if (booking.getDriver() != null) {
            Driver driver = booking.getDriver();
            driver.setStatus(Driver.DriverStatus.AVAILABLE);
            
            // Update total deliveries if booking was delivered
            if (booking.getStatus() == Booking.BookingStatus.DELIVERED) {
                int currentDeliveries = driver.getTotalDeliveries() != null ? driver.getTotalDeliveries() : 0;
                driver.setTotalDeliveries(currentDeliveries + 1);
            }
            
            driverRepository.save(driver);
        }
    }

    private void createShipmentEvent(Booking booking, ShipmentEvent.EventType eventType, String description) {
        ShipmentEvent event = new ShipmentEvent(booking, eventType, description);
        event.setEventTimestamp(LocalDateTime.now());
        shipmentEventRepository.save(event);
    }

    private double calculateDistance(BigDecimal lat1, BigDecimal lon1, BigDecimal lat2, BigDecimal lon2) {
        double earthRadius = 6371; // Earth radius in kilometers

        double dLat = Math.toRadians(lat2.doubleValue() - lat1.doubleValue());
        double dLon = Math.toRadians(lon2.doubleValue() - lon1.doubleValue());

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                   Math.cos(Math.toRadians(lat1.doubleValue())) * Math.cos(Math.toRadians(lat2.doubleValue())) *
                   Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return earthRadius * c;
    }
}
