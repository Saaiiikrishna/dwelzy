package com.dwelzy.dwelzy.service;

import com.dwelzy.dwelzy.dto.ScanRequest;
import com.dwelzy.dwelzy.entity.Booking;
import com.dwelzy.dwelzy.entity.ShipmentEvent;
import com.dwelzy.dwelzy.entity.User;
import com.dwelzy.dwelzy.repository.BookingRepository;
import com.dwelzy.dwelzy.repository.ShipmentEventRepository;
import com.dwelzy.dwelzy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class ScanService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private ShipmentEventRepository shipmentEventRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DriverAssignmentService driverAssignmentService;

    public ShipmentEvent processScan(ScanRequest scanRequest, String username) {
        // Find the booking
        Booking booking = bookingRepository.findByBookingNumber(scanRequest.getBookingNumber())
                .orElseThrow(() -> new RuntimeException("Booking not found: " + scanRequest.getBookingNumber()));

        // Find the user who performed the scan
        User scannedBy = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));

        // Determine event type and update booking status
        ShipmentEvent.EventType eventType = ShipmentEvent.EventType.valueOf(scanRequest.getScanType());
        String description = generateEventDescription(eventType, scanRequest);

        // Create shipment event
        ShipmentEvent event = new ShipmentEvent(booking, eventType, description);
        event.setEventTimestamp(LocalDateTime.now());
        event.setLocation(scanRequest.getLocation());
        event.setLatitude(scanRequest.getLatitude());
        event.setLongitude(scanRequest.getLongitude());
        event.setAdditionalNotes(scanRequest.getNotes());
        event.setCreatedBy(scannedBy);

        // Handle image if provided
        if (scanRequest.getImageBase64() != null && !scanRequest.getImageBase64().trim().isEmpty()) {
            // In a real implementation, you would save the image to cloud storage
            // and store the URL here. For now, we'll just indicate that an image was captured
            event.setImageUrl("image_captured_" + System.currentTimeMillis());
        }

        // Update booking status based on scan type
        updateBookingStatus(booking, eventType);

        // Save event and booking
        event = shipmentEventRepository.save(event);
        bookingRepository.save(booking);

        // Handle special cases
        handleSpecialScanEvents(booking, eventType);

        return event;
    }

    private String generateEventDescription(ShipmentEvent.EventType eventType, ScanRequest scanRequest) {
        return switch (eventType) {
            case PICKUP_COMPLETED -> "Package picked up from " + (scanRequest.getLocation() != null ? scanRequest.getLocation() : "pickup location");
            case DELIVERY_COMPLETED -> "Package delivered to " + (scanRequest.getLocation() != null ? scanRequest.getLocation() : "delivery location");
            case HUB_ARRIVAL -> "Package arrived at hub" + (scanRequest.getLocation() != null ? " - " + scanRequest.getLocation() : "");
            case HUB_DEPARTURE -> "Package departed from hub" + (scanRequest.getLocation() != null ? " - " + scanRequest.getLocation() : "");
            case SCAN_EVENT -> "Package scanned" + (scanRequest.getLocation() != null ? " at " + scanRequest.getLocation() : "");
            case PICKUP_ATTEMPTED -> "Pickup attempted" + (scanRequest.getLocation() != null ? " at " + scanRequest.getLocation() : "");
            case DELIVERY_ATTEMPTED -> "Delivery attempted" + (scanRequest.getLocation() != null ? " at " + scanRequest.getLocation() : "");
            case DELIVERY_FAILED -> "Delivery failed" + (scanRequest.getNotes() != null ? " - " + scanRequest.getNotes() : "");
            case EXCEPTION -> "Exception occurred" + (scanRequest.getNotes() != null ? " - " + scanRequest.getNotes() : "");
            default -> eventType.getDescription();
        };
    }

    private void updateBookingStatus(Booking booking, ShipmentEvent.EventType eventType) {
        switch (eventType) {
            case PICKUP_COMPLETED -> {
                booking.setStatus(Booking.BookingStatus.IN_TRANSIT);
                booking.setActualPickupTime(LocalDateTime.now());
            }
            case DELIVERY_COMPLETED -> {
                booking.setStatus(Booking.BookingStatus.DELIVERED);
                booking.setActualDeliveryTime(LocalDateTime.now());
            }
            case DELIVERY_FAILED -> {
                booking.setStatus(Booking.BookingStatus.FAILED);
            }
            case PICKUP_ATTEMPTED, DELIVERY_ATTEMPTED -> {
                // Status remains the same, just log the attempt
            }
            case HUB_ARRIVAL, HUB_DEPARTURE, SCAN_EVENT -> {
                // These don't change the main booking status
                if (booking.getStatus() == Booking.BookingStatus.ASSIGNED) {
                    booking.setStatus(Booking.BookingStatus.IN_TRANSIT);
                }
            }
            case EXCEPTION -> {
                // Handle exceptions based on current status
                if (booking.getStatus() == Booking.BookingStatus.IN_TRANSIT) {
                    // Could set to a special "EXCEPTION" status if you have one
                }
            }
        }
    }

    private void handleSpecialScanEvents(Booking booking, ShipmentEvent.EventType eventType) {
        switch (eventType) {
            case DELIVERY_COMPLETED, DELIVERY_FAILED -> {
                // Release the driver
                driverAssignmentService.releaseDriver(booking.getId());
            }
            case PICKUP_COMPLETED -> {
                // Could trigger notifications or other business logic
            }
        }
    }

    public List<ShipmentEvent> getBookingEvents(String bookingNumber) {
        Booking booking = bookingRepository.findByBookingNumber(bookingNumber)
                .orElseThrow(() -> new RuntimeException("Booking not found: " + bookingNumber));
        
        return shipmentEventRepository.findByBookingOrderByEventTimestampDesc(booking);
    }

    public List<ShipmentEvent> getRecentEvents(int limit) {
        // This would need a custom query to limit results
        // For now, we'll return all events (in a real implementation, add pagination)
        return shipmentEventRepository.findAll();
    }

    public ShipmentEvent getEventById(Long eventId) {
        return shipmentEventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found: " + eventId));
    }

    public boolean validateScanPermission(String bookingNumber, String username) {
        Booking booking = bookingRepository.findByBookingNumber(bookingNumber)
                .orElseThrow(() -> new RuntimeException("Booking not found: " + bookingNumber));

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));

        // Check if user is the assigned driver, admin, or hub manager
        boolean isAssignedDriver = booking.getDriver() != null && 
                                  booking.getDriver().getUser().getId().equals(user.getId());
        
        boolean hasAdminRole = user.getRoles().stream()
                .anyMatch(role -> role.getName().name().equals("ROLE_ADMIN") || 
                                role.getName().name().equals("ROLE_HUB_MANAGER"));

        return isAssignedDriver || hasAdminRole;
    }
}
