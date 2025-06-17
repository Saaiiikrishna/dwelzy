package com.dwelzy.dwelzy.controller;

import com.dwelzy.dwelzy.dto.BookingRequest;
import com.dwelzy.dwelzy.dto.BookingResponse;
import com.dwelzy.dwelzy.dto.MessageResponse;
import com.dwelzy.dwelzy.entity.Booking;
import com.dwelzy.dwelzy.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> createBooking(@Valid @RequestBody BookingRequest bookingRequest,
                                         Authentication authentication) {
        try {
            String username = authentication.getName();
            Booking booking = bookingService.createBooking(bookingRequest, username);
            BookingResponse response = new BookingResponse(booking);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error creating booking: " + e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('DRIVER') or hasRole('ADMIN')")
    public ResponseEntity<?> getBookingById(@PathVariable Long id) {
        try {
            Booking booking = bookingService.getBookingById(id)
                    .orElseThrow(() -> new RuntimeException("Booking not found with id: " + id));
            BookingResponse response = new BookingResponse(booking);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error retrieving booking: " + e.getMessage()));
        }
    }

    @GetMapping("/number/{bookingNumber}")
    @PreAuthorize("hasRole('USER') or hasRole('DRIVER') or hasRole('ADMIN')")
    public ResponseEntity<?> getBookingByNumber(@PathVariable String bookingNumber) {
        try {
            Booking booking = bookingService.getBookingByNumber(bookingNumber)
                    .orElseThrow(() -> new RuntimeException("Booking not found with number: " + bookingNumber));
            BookingResponse response = new BookingResponse(booking);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error retrieving booking: " + e.getMessage()));
        }
    }

    @GetMapping("/my-bookings")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> getMyBookings(Authentication authentication) {
        try {
            String username = authentication.getName();
            List<Booking> bookings = bookingService.getBookingsByUser(username);
            List<BookingResponse> responses = bookings.stream()
                    .map(BookingResponse::new)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(responses);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error retrieving bookings: " + e.getMessage()));
        }
    }

    @GetMapping("/status/{status}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('HUB_MANAGER')")
    public ResponseEntity<?> getBookingsByStatus(@PathVariable String status) {
        try {
            Booking.BookingStatus bookingStatus = Booking.BookingStatus.valueOf(status.toUpperCase());
            List<Booking> bookings = bookingService.getBookingsByStatus(bookingStatus);
            List<BookingResponse> responses = bookings.stream()
                    .map(BookingResponse::new)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(responses);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Invalid status: " + status));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error retrieving bookings: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('DRIVER') or hasRole('ADMIN') or hasRole('HUB_MANAGER')")
    public ResponseEntity<?> updateBookingStatus(@PathVariable Long id,
                                               @RequestParam String status,
                                               Authentication authentication) {
        try {
            String username = authentication.getName();
            Booking.BookingStatus newStatus = Booking.BookingStatus.valueOf(status.toUpperCase());
            Booking booking = bookingService.updateBookingStatus(id, newStatus, username);
            BookingResponse response = new BookingResponse(booking);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Invalid status: " + status));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error updating booking status: " + e.getMessage()));
        }
    }
}
