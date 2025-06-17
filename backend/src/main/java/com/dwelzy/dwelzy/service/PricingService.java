package com.dwelzy.dwelzy.service;

import com.dwelzy.dwelzy.dto.BookingRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class PricingService {

    // Base pricing constants
    private static final BigDecimal BASE_PRICE = new BigDecimal("50.00");
    private static final BigDecimal PRICE_PER_KM = new BigDecimal("5.00");
    private static final BigDecimal PRICE_PER_KG = new BigDecimal("10.00");
    private static final BigDecimal VOLUME_MULTIPLIER = new BigDecimal("0.5");
    private static final BigDecimal URGENT_SURCHARGE = new BigDecimal("25.00");

    public BigDecimal calculatePrice(BookingRequest request) {
        BigDecimal totalPrice = BASE_PRICE;

        // Add distance-based pricing
        if (request.getPickupLatitude() != null && request.getPickupLongitude() != null &&
            request.getDeliveryLatitude() != null && request.getDeliveryLongitude() != null) {
            
            BigDecimal distance = calculateDistance(
                request.getPickupLatitude(), request.getPickupLongitude(),
                request.getDeliveryLatitude(), request.getDeliveryLongitude()
            );
            
            totalPrice = totalPrice.add(distance.multiply(PRICE_PER_KM));
        }

        // Add weight-based pricing
        if (request.getWeight() != null && request.getWeight() > 0) {
            BigDecimal weightPrice = BigDecimal.valueOf(request.getWeight()).multiply(PRICE_PER_KG);
            totalPrice = totalPrice.add(weightPrice);
        }

        // Add volume-based pricing
        if (request.getLength() != null && request.getWidth() != null && request.getHeight() != null) {
            double volume = request.getLength() * request.getWidth() * request.getHeight();
            if (volume > 0) {
                BigDecimal volumePrice = BigDecimal.valueOf(volume).multiply(VOLUME_MULTIPLIER);
                totalPrice = totalPrice.add(volumePrice);
            }
        }

        // Add item type surcharge
        if (request.getItemType() != null) {
            BigDecimal itemSurcharge = getItemTypeSurcharge(request.getItemType());
            totalPrice = totalPrice.add(itemSurcharge);
        }

        // Add urgent delivery surcharge if scheduled within 2 hours
        if (request.getScheduledPickupTime() != null) {
            long hoursUntilPickup = java.time.Duration.between(
                java.time.LocalDateTime.now(), 
                request.getScheduledPickupTime()
            ).toHours();
            
            if (hoursUntilPickup <= 2) {
                totalPrice = totalPrice.add(URGENT_SURCHARGE);
            }
        }

        return totalPrice.setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal getItemTypeSurcharge(String itemType) {
        return switch (itemType.toLowerCase()) {
            case "fragile" -> new BigDecimal("20.00");
            case "electronics" -> new BigDecimal("15.00");
            case "documents" -> new BigDecimal("5.00");
            case "food" -> new BigDecimal("10.00");
            case "medical" -> new BigDecimal("30.00");
            case "hazardous" -> new BigDecimal("50.00");
            default -> BigDecimal.ZERO;
        };
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

    public BigDecimal getEstimatedPrice(Double weight, Double length, Double width, Double height, 
                                       String itemType, BigDecimal distance) {
        BigDecimal totalPrice = BASE_PRICE;

        // Add distance-based pricing
        if (distance != null) {
            totalPrice = totalPrice.add(distance.multiply(PRICE_PER_KM));
        }

        // Add weight-based pricing
        if (weight != null && weight > 0) {
            BigDecimal weightPrice = BigDecimal.valueOf(weight).multiply(PRICE_PER_KG);
            totalPrice = totalPrice.add(weightPrice);
        }

        // Add volume-based pricing
        if (length != null && width != null && height != null) {
            double volume = length * width * height;
            if (volume > 0) {
                BigDecimal volumePrice = BigDecimal.valueOf(volume).multiply(VOLUME_MULTIPLIER);
                totalPrice = totalPrice.add(volumePrice);
            }
        }

        // Add item type surcharge
        if (itemType != null) {
            BigDecimal itemSurcharge = getItemTypeSurcharge(itemType);
            totalPrice = totalPrice.add(itemSurcharge);
        }

        return totalPrice.setScale(2, RoundingMode.HALF_UP);
    }
}
