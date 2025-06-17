package com.dwelzy.dwelzy.repository;

import com.dwelzy.dwelzy.entity.Booking;
import com.dwelzy.dwelzy.entity.ShipmentEvent;
import com.dwelzy.dwelzy.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ShipmentEventRepository extends JpaRepository<ShipmentEvent, Long> {

    List<ShipmentEvent> findByBooking(Booking booking);

    List<ShipmentEvent> findByBookingOrderByEventTimestampDesc(Booking booking);

    List<ShipmentEvent> findByEventType(ShipmentEvent.EventType eventType);

    List<ShipmentEvent> findByCreatedBy(User createdBy);

    @Query("SELECT se FROM ShipmentEvent se WHERE se.booking = :booking AND se.eventType = :eventType")
    List<ShipmentEvent> findByBookingAndEventType(@Param("booking") Booking booking, 
                                                  @Param("eventType") ShipmentEvent.EventType eventType);

    @Query("SELECT se FROM ShipmentEvent se WHERE se.eventTimestamp BETWEEN :startTime AND :endTime")
    List<ShipmentEvent> findByEventTimestampBetween(@Param("startTime") LocalDateTime startTime, 
                                                    @Param("endTime") LocalDateTime endTime);

    @Query("SELECT se FROM ShipmentEvent se WHERE se.booking = :booking AND " +
           "se.eventTimestamp BETWEEN :startTime AND :endTime ORDER BY se.eventTimestamp DESC")
    List<ShipmentEvent> findByBookingAndEventTimestampBetween(@Param("booking") Booking booking,
                                                              @Param("startTime") LocalDateTime startTime, 
                                                              @Param("endTime") LocalDateTime endTime);

    @Query("SELECT se FROM ShipmentEvent se WHERE se.booking.id = :bookingId ORDER BY se.eventTimestamp DESC")
    List<ShipmentEvent> findLatestEventsByBookingId(@Param("bookingId") Long bookingId);
}
