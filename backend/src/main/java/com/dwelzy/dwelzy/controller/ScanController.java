package com.dwelzy.dwelzy.controller;

import com.dwelzy.dwelzy.dto.MessageResponse;
import com.dwelzy.dwelzy.dto.ScanRequest;
import com.dwelzy.dwelzy.dto.ScanResponse;
import com.dwelzy.dwelzy.entity.ShipmentEvent;
import com.dwelzy.dwelzy.service.ScanService;
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
@RequestMapping("/api/v1/scan")
public class ScanController {

    @Autowired
    private ScanService scanService;

    @PostMapping("/process")
    @PreAuthorize("hasRole('DRIVER') or hasRole('ADMIN') or hasRole('HUB_MANAGER')")
    public ResponseEntity<?> processScan(@Valid @RequestBody ScanRequest scanRequest,
                                       Authentication authentication) {
        try {
            String username = authentication.getName();
            
            // Validate scan permission
            if (!scanService.validateScanPermission(scanRequest.getBookingNumber(), username)) {
                return ResponseEntity.badRequest()
                        .body(new MessageResponse("You don't have permission to scan this booking"));
            }

            ShipmentEvent event = scanService.processScan(scanRequest, username);
            ScanResponse response = new ScanResponse(event);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error processing scan: " + e.getMessage()));
        }
    }

    @GetMapping("/booking/{bookingNumber}/events")
    @PreAuthorize("hasRole('USER') or hasRole('DRIVER') or hasRole('ADMIN') or hasRole('HUB_MANAGER')")
    public ResponseEntity<?> getBookingEvents(@PathVariable String bookingNumber) {
        try {
            List<ShipmentEvent> events = scanService.getBookingEvents(bookingNumber);
            List<ScanResponse> responses = events.stream()
                    .map(ScanResponse::new)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(responses);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error retrieving events: " + e.getMessage()));
        }
    }

    @GetMapping("/events/recent")
    @PreAuthorize("hasRole('ADMIN') or hasRole('HUB_MANAGER')")
    public ResponseEntity<?> getRecentEvents(@RequestParam(defaultValue = "50") int limit) {
        try {
            List<ShipmentEvent> events = scanService.getRecentEvents(limit);
            List<ScanResponse> responses = events.stream()
                    .map(ScanResponse::new)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(responses);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error retrieving recent events: " + e.getMessage()));
        }
    }

    @GetMapping("/events/{eventId}")
    @PreAuthorize("hasRole('USER') or hasRole('DRIVER') or hasRole('ADMIN') or hasRole('HUB_MANAGER')")
    public ResponseEntity<?> getEventById(@PathVariable Long eventId) {
        try {
            ShipmentEvent event = scanService.getEventById(eventId);
            ScanResponse response = new ScanResponse(event);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error retrieving event: " + e.getMessage()));
        }
    }

    @GetMapping("/scan-types")
    @PreAuthorize("hasRole('DRIVER') or hasRole('ADMIN') or hasRole('HUB_MANAGER')")
    public ResponseEntity<?> getScanTypes() {
        try {
            String[] scanTypes = {
                "PICKUP_COMPLETED",
                "DELIVERY_COMPLETED", 
                "HUB_ARRIVAL",
                "HUB_DEPARTURE",
                "SCAN_EVENT",
                "PICKUP_ATTEMPTED",
                "DELIVERY_ATTEMPTED",
                "DELIVERY_FAILED",
                "EXCEPTION"
            };
            return ResponseEntity.ok(scanTypes);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error retrieving scan types: " + e.getMessage()));
        }
    }

    @PostMapping("/validate-permission")
    @PreAuthorize("hasRole('DRIVER') or hasRole('ADMIN') or hasRole('HUB_MANAGER')")
    public ResponseEntity<?> validateScanPermission(@RequestParam String bookingNumber,
                                                   Authentication authentication) {
        try {
            String username = authentication.getName();
            boolean hasPermission = scanService.validateScanPermission(bookingNumber, username);
            
            if (hasPermission) {
                return ResponseEntity.ok(new MessageResponse("Permission granted"));
            } else {
                return ResponseEntity.badRequest()
                        .body(new MessageResponse("Permission denied"));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error validating permission: " + e.getMessage()));
        }
    }
}
