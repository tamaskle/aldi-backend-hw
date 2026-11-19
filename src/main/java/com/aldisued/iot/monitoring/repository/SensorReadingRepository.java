package com.aldisued.iot.monitoring.repository;

import com.aldisued.iot.monitoring.entity.SensorReading;
import com.aldisued.iot.monitoring.entity.SensorType;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SensorReadingRepository extends JpaRepository<SensorReading, Long> {

    @Query("SELECT AVG(sr.value) FROM SensorReading sr WHERE sr.sensor.type = :type AND sr.timestamp BETWEEN :from AND :to")
    Optional<Double> getAverageValueBySensorTypeAndTimestampBetween(
            @Param("type") SensorType type,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to
    );

    // New method for Task 8
    @Query("SELECT sr.value FROM SensorReading sr WHERE sr.sensor.type = :type AND sr.timestamp BETWEEN :from AND :to ORDER BY sr.timestamp ASC")
    List<Double> findValuesBySensorTypeAndTimestampBetweenOrderByTimestampAsc(
            @Param("type") SensorType type,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to
    );
}