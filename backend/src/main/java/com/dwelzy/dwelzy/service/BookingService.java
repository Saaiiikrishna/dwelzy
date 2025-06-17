package com.dwelzy.dwelzy.service;

import com.dwelzy.dwelzy.dto.BookingRequest;
import com.dwelzy.dwelzy.entity.Booking;
import com.dwelzy.dwelzy.entity.ShipmentEvent;
import com.dwelzy.dwelzy.entity.User;
import com.dwelzy.dwelzy.repository.BookingRepository;
import com.dwelzy.dwelzy.repository.ShipmentEventRepository;
import com.dwelzy.dwelzy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ShipmentEventRepository shipmentEventRepository;

    @Autowired
    private PricingService pricingService;

    public Booking createBooking(BookingRequest request, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));

        // Generate unique booking number
        String bookingNumber = generateBookingNumber();

        // Calculate pricing
        BigDecimal totalPrice = pricingService.calculatePrice(request);

        // Calculate distance if coordinates are provided
        BigDecimal distance = null;
        if (request.getPickupLatitude() != null && request.getPickupLongitude() != null &&
            request.getDeliveryLatitude() != null && request.getDeliveryLongitude() != null) {
            distance = calculateDistance(
                request.getPickupLatitude(), request.getPickupLongitude(),
                request.getDeliveryLatitude(), request.getDeliveryLongitude()
            );
        }

        // Create booking
        Booking booking = new Booking(
            bookingNumber,
            user,
            request.getPickupAddress(),
            request.getDeliveryAddress(),
            totalPrice
        );

        booking.setPickupLatitude(request.getPickupLatitude());
        booking.setPickupLongitude(request.getPickupLongitude());
        booking.setDeliveryLatitude(request.getDeliveryLatitude());
        booking.setDeliveryLongitude(request.getDeliveryLongitude());
        booking.setScheduledPickupTime(request.getScheduledPickupTime());
        booking.setScheduledDeliveryTime(request.getScheduledDeliveryTime());
        booking.setSpecialInstructions(request.getSpecialInstructions());
        booking.setNotes(request.getNotes());
        booking.setDistanceKm(distance);

        // Save booking
        booking = bookingRepository.save(booking);

        // Create initial shipment event
        createShipmentEvent(booking, ShipmentEvent.EventType.BOOKING_CREATED, 
                          "Booking created successfully", user);

        return booking;
    }

    public Optional<Booking> getBookingById(Long id) {
        return bookingRepository.findById(id);
    }

    public Optional<Booking> getBookingByNumber(String bookingNumber) {
        return bookingRepository.findByBookingNumber(bookingNumber);
    }

    public List<Booking> getBookingsByUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
        return bookingRepository.findByUser(user);
    }

    public List<Booking> getBookingsByStatus(Booking.BookingStatus status) {
        return bookingRepository.findByStatus(status);
    }

    public Booking updateBookingStatus(Long bookingId, Booking.BookingStatus newStatus, String username) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found: " + bookingId));

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));

        Booking.BookingStatus oldStatus = booking.getStatus();
        booking.setStatus(newStatus);
        booking = bookingRepository.save(booking);

        // Create shipment event for status change
        String description = String.format("Booking status changed from %s to %s", 
                                         oldStatus.name(), newStatus.name());
        createShipmentEvent(booking, getEventTypeForStatus(newStatus), description, user);

        return booking;
    }

    private void createShipmentEvent(Booking booking, ShipmentEvent.EventType eventType, 
                                   String description, User createdBy) {
        ShipmentEvent event = new ShipmentEvent(booking, eventType, description);
        event.setCreatedBy(createdBy);
        shipmentEventRepository.save(event);
    }

    private ShipmentEvent.EventType getEventTypeForStatus(Booking.BookingStatus status) {
        return switch (status) {
            case PENDING -> ShipmentEvent.EventType.BOOKING_CREATED;
            case CONFIRMED -> ShipmentEvent.EventType.BOOKING_CONFIRMED;
            case ASSIGNED -> ShipmentEvent.EventType.DRIVER_ASSIGNED;
            case IN_TRANSIT -> ShipmentEvent.EventType.IN_TRANSIT;
            case DELIVERED -> ShipmentEvent.EventType.DELIVERY_COMPLETED;
            case CANCELLED -> ShipmentEvent.EventType.BOOKING_CANCELLED;
            case FAILED -> ShipmentEvent.EventType.DELIVERY_FAILED;
        };
    }

    private String generateBookingNumber() {
        String prefix = "DWZ";
        String timestamp = String.valueOf(System.currentTimeMillis());
        String random = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        return prefix + timestamp.substring(timestamp.length() - 6) + random;
    }

    private BigDecimal calculateDistance(BigDecimal lat1, BigDecimal lon1, BigDecimal lat2, BigDecimal lon2) {
        // Haversine formula to calculate distance between two points
        double earthRadius = 6371; // Earth radius in kilometers

        double dLat = Math.toRadians(lat2.doubleValue() - lat1.doubleValue());
        double dLon = Math.toRadians(lon2.doubleValue() - lon1.doubleValue());

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                   Math.cos(Math.toRadians(lat1.doubleValue())) * Math.cos(Math.toRadians(lat2.doubleValue())) *
                   Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = earthRadius * c;

        return BigDecimal.valueOf(distance);
    }
}
