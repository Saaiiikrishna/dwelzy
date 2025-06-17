package com.dwelzy.dwelzy.repository;

import com.dwelzy.dwelzy.entity.Driver;
import com.dwelzy.dwelzy.entity.Hub;
import com.dwelzy.dwelzy.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {

    Optional<Driver> findByUser(User user);

    Optional<Driver> findByLicenseNumber(String licenseNumber);

    List<Driver> findByHub(Hub hub);

    List<Driver> findByStatus(Driver.DriverStatus status);

    List<Driver> findByHubAndStatus(Hub hub, Driver.DriverStatus status);

    List<Driver> findByIsVerifiedTrue();

    List<Driver> findByIsVerifiedFalse();

    @Query("SELECT d FROM Driver d WHERE d.status = 'AVAILABLE' AND d.isVerified = true")
    List<Driver> findAvailableVerifiedDrivers();

    @Query("SELECT d FROM Driver d WHERE d.hub = :hub AND d.status = 'AVAILABLE' AND d.isVerified = true")
    List<Driver> findAvailableVerifiedDriversByHub(@Param("hub") Hub hub);

    @Query("SELECT d FROM Driver d WHERE d.rating >= :minRating ORDER BY d.rating DESC")
    List<Driver> findByRatingGreaterThanEqualOrderByRatingDesc(@Param("minRating") Double minRating);

    @Query("SELECT d FROM Driver d WHERE d.totalDeliveries >= :minDeliveries ORDER BY d.totalDeliveries DESC")
    List<Driver> findByTotalDeliveriesGreaterThanEqualOrderByTotalDeliveriesDesc(@Param("minDeliveries") Integer minDeliveries);

    Boolean existsByLicenseNumber(String licenseNumber);

    Boolean existsByUser(User user);
}
