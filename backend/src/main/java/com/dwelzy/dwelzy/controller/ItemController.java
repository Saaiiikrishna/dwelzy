package com.dwelzy.dwelzy.controller;

import com.dwelzy.dwelzy.dto.ItemAnalysisRequest;
import com.dwelzy.dwelzy.dto.ItemAnalysisResponse;
import com.dwelzy.dwelzy.dto.MessageResponse;
import com.dwelzy.dwelzy.service.ItemAnalysisService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/items")
public class ItemController {

    @Autowired
    private ItemAnalysisService itemAnalysisService;

    @PostMapping("/analyze")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> analyzeItem(@Valid @RequestBody ItemAnalysisRequest request) {
        try {
            // Validate base64 image
            if (request.getImageBase64() == null || request.getImageBase64().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(new MessageResponse("Image data is required"));
            }

            // Validate base64 format
            try {
                Base64.getDecoder().decode(request.getImageBase64());
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest()
                        .body(new MessageResponse("Invalid base64 image format"));
            }

            ItemAnalysisResponse response = itemAnalysisService.analyzeItem(request);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error analyzing item: " + e.getMessage()));
        }
    }

    @PostMapping("/analyze-upload")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> analyzeItemUpload(@RequestParam("image") MultipartFile file,
                                             @RequestParam(value = "description", required = false) String description,
                                             @RequestParam(value = "estimatedWeight", required = false) Double estimatedWeight,
                                             @RequestParam(value = "estimatedLength", required = false) Double estimatedLength,
                                             @RequestParam(value = "estimatedWidth", required = false) Double estimatedWidth,
                                             @RequestParam(value = "estimatedHeight", required = false) Double estimatedHeight) {
        try {
            // Validate file
            if (file.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(new MessageResponse("Image file is required"));
            }

            // Check file type
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                return ResponseEntity.badRequest()
                        .body(new MessageResponse("File must be an image"));
            }

            // Check file size (max 10MB)
            if (file.getSize() > 10 * 1024 * 1024) {
                return ResponseEntity.badRequest()
                        .body(new MessageResponse("File size must be less than 10MB"));
            }

            // Convert to base64
            byte[] imageBytes = file.getBytes();
            String base64Image = Base64.getEncoder().encodeToString(imageBytes);

            // Create request object
            ItemAnalysisRequest request = new ItemAnalysisRequest();
            request.setImageBase64(base64Image);
            request.setItemDescription(description);
            request.setEstimatedWeight(estimatedWeight);
            request.setEstimatedLength(estimatedLength);
            request.setEstimatedWidth(estimatedWidth);
            request.setEstimatedHeight(estimatedHeight);

            ItemAnalysisResponse response = itemAnalysisService.analyzeItem(request);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error analyzing item: " + e.getMessage()));
        }
    }

    @GetMapping("/categories")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> getItemCategories() {
        try {
            String[] categories = {
                "electronics", "fragile", "documents", "food", 
                "medical", "hazardous", "general"
            };
            return ResponseEntity.ok(categories);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error retrieving categories: " + e.getMessage()));
        }
    }

    @GetMapping("/handling-instructions/{itemType}")
    @PreAuthorize("hasRole('USER') or hasRole('DRIVER') or hasRole('ADMIN')")
    public ResponseEntity<?> getHandlingInstructions(@PathVariable String itemType) {
        try {
            // This would typically come from a database or configuration
            String[] instructions = switch (itemType.toLowerCase()) {
                case "electronics" -> new String[]{
                    "Keep dry", "Handle with care", "Avoid magnetic fields", "Fragile - This side up"
                };
                case "fragile" -> new String[]{
                    "Fragile - Handle with extreme care", "This side up", "Do not stack", "Use bubble wrap"
                };
                case "documents" -> new String[]{
                    "Keep dry", "Do not fold", "Handle with care", "Confidential"
                };
                case "food" -> new String[]{
                    "Keep refrigerated", "Perishable", "Handle with care", "Check expiry date"
                };
                case "medical" -> new String[]{
                    "Urgent delivery", "Keep sterile", "Temperature sensitive", "Handle with care"
                };
                case "hazardous" -> new String[]{
                    "Dangerous goods", "Special handling required", "Follow safety protocols", "Trained personnel only"
                };
                default -> new String[]{
                    "Handle with care", "Standard delivery", "Keep dry"
                };
            };
            
            return ResponseEntity.ok(instructions);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error retrieving handling instructions: " + e.getMessage()));
        }
    }
}
