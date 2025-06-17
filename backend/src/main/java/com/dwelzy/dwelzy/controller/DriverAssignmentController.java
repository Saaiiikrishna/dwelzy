package com.dwelzy.dwelzy.controller;

import com.dwelzy.dwelzy.dto.BookingResponse;
import com.dwelzy.dwelzy.dto.MessageResponse;
import com.dwelzy.dwelzy.entity.Booking;
import com.dwelzy.dwelzy.service.DriverAssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/driver-assignment")
public class DriverAssignmentController {

    @Autowired
    private DriverAssignmentService driverAssignmentService;

    @PostMapping("/assign/{bookingId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('HUB_MANAGER')")
    public ResponseEntity<?> assignDriverToBooking(@PathVariable Long bookingId) {
        try {
            Booking booking = driverAssignmentService.assignDriverToBooking(bookingId);
            BookingResponse response = new BookingResponse(booking);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error assigning driver: " + e.getMessage()));
        }
    }

    @PostMapping("/assign-hubs/{bookingId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('HUB_MANAGER')")
    public ResponseEntity<?> assignHubsToBooking(@PathVariable Long bookingId) {
        try {
            Booking booking = driverAssignmentService.assignHubsToBooking(bookingId);
            BookingResponse response = new BookingResponse(booking);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error assigning hubs: " + e.getMessage()));
        }
    }

    @PostMapping("/release/{bookingId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('HUB_MANAGER')")
    public ResponseEntity<?> releaseDriver(@PathVariable Long bookingId) {
        try {
            driverAssignmentService.releaseDriver(bookingId);
            return ResponseEntity.ok(new MessageResponse("Driver released successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error releasing driver: " + e.getMessage()));
        }
    }

    @PostMapping("/auto-assign")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> triggerAutoAssignment() {
        try {
            driverAssignmentService.autoAssignDrivers();
            return ResponseEntity.ok(new MessageResponse("Auto-assignment triggered successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error triggering auto-assignment: " + e.getMessage()));
        }
    }
}
