package com.dwelzy.dwelzy.repository;

import com.dwelzy.dwelzy.entity.Hub;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface HubRepository extends JpaRepository<Hub, Long> {

    List<Hub> findByIsActiveTrue();

    List<Hub> findByCity(String city);

    List<Hub> findByState(String state);

    List<Hub> findByCityAndState(String city, String state);

    @Query("SELECT h FROM Hub h WHERE h.isActive = true AND " +
           "(LOWER(h.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(h.city) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(h.address) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    List<Hub> searchActiveHubs(@Param("searchTerm") String searchTerm);

    @Query("SELECT h FROM Hub h WHERE h.isActive = true AND " +
           "h.latitude IS NOT NULL AND h.longitude IS NOT NULL AND " +
           "(6371 * acos(cos(radians(:latitude)) * cos(radians(h.latitude)) * " +
           "cos(radians(h.longitude) - radians(:longitude)) + " +
           "sin(radians(:latitude)) * sin(radians(h.latitude)))) <= :radiusKm " +
           "ORDER BY (6371 * acos(cos(radians(:latitude)) * cos(radians(h.latitude)) * " +
           "cos(radians(h.longitude) - radians(:longitude)) + " +
           "sin(radians(:latitude)) * sin(radians(h.latitude))))")
    List<Hub> findNearbyHubs(@Param("latitude") BigDecimal latitude, 
                             @Param("longitude") BigDecimal longitude, 
                             @Param("radiusKm") Double radiusKm);

    @Query("SELECT h FROM Hub h WHERE h.isActive = true ORDER BY " +
           "(6371 * acos(cos(radians(:latitude)) * cos(radians(h.latitude)) * " +
           "cos(radians(h.longitude) - radians(:longitude)) + " +
           "sin(radians(:latitude)) * sin(radians(h.latitude))))")
    List<Hub> findNearestHubs(@Param("latitude") BigDecimal latitude, 
                              @Param("longitude") BigDecimal longitude);
}
