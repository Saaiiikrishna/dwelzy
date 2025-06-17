package com.dwelzy.dwelzy.repository;

import com.dwelzy.dwelzy.entity.Booking;
import com.dwelzy.dwelzy.entity.User;
import com.dwelzy.dwelzy.entity.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    Optional<Booking> findByBookingNumber(String bookingNumber);

    List<Booking> findByUser(User user);

    List<Booking> findByDriver(Driver driver);

    List<Booking> findByStatus(Booking.BookingStatus status);

    List<Booking> findByUserAndStatus(User user, Booking.BookingStatus status);

    List<Booking> findByDriverAndStatus(Driver driver, Booking.BookingStatus status);

    @Query("SELECT b FROM Booking b WHERE b.scheduledPickupTime BETWEEN :startTime AND :endTime")
    List<Booking> findByScheduledPickupTimeBetween(@Param("startTime") LocalDateTime startTime, 
                                                   @Param("endTime") LocalDateTime endTime);

    @Query("SELECT b FROM Booking b WHERE b.scheduledDeliveryTime BETWEEN :startTime AND :endTime")
    List<Booking> findByScheduledDeliveryTimeBetween(@Param("startTime") LocalDateTime startTime, 
                                                     @Param("endTime") LocalDateTime endTime);

    @Query("SELECT b FROM Booking b WHERE b.driver IS NULL AND b.status = :status")
    List<Booking> findUnassignedBookingsByStatus(@Param("status") Booking.BookingStatus status);

    @Query("SELECT COUNT(b) FROM Booking b WHERE b.user = :user")
    Long countBookingsByUser(@Param("user") User user);

    @Query("SELECT COUNT(b) FROM Booking b WHERE b.driver = :driver AND b.status = 'DELIVERED'")
    Long countDeliveredBookingsByDriver(@Param("driver") Driver driver);

    Boolean existsByBookingNumber(String bookingNumber);
}
