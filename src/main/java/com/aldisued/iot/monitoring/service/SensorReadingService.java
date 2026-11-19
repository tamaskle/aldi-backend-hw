package com.aldisued.iot.monitoring.service;

import com.aldisued.iot.monitoring.dto.SensorReadingDto;
import com.aldisued.iot.monitoring.entity.Alert;
import com.aldisued.iot.monitoring.entity.Sensor;
import com.aldisued.iot.monitoring.entity.SensorReading;
import com.aldisued.iot.monitoring.repository.AlertRepository;
import com.aldisued.iot.monitoring.repository.SensorReadingRepository;
import com.aldisued.iot.monitoring.repository.SensorRepository;
import org.springframework.stereotype.Service;

@Service
public class SensorReadingService {

  private final SensorReadingRepository sensorReadingRepository;
  private final SensorRepository sensorRepository;
  private final AlertRepository alertRepository;

  public SensorReadingService(SensorReadingRepository sensorReadingRepository,
                              SensorRepository sensorRepository,
                              AlertRepository alertRepository) {
    this.sensorReadingRepository = sensorReadingRepository;
    this.sensorRepository = sensorRepository;
    this.alertRepository = alertRepository;
  }

  public SensorReading saveSensorReading(SensorReadingDto sensorReadingDto) {
    Sensor sensor = sensorRepository.findById(sensorReadingDto.sensorId())
            .orElseThrow(() -> new IllegalArgumentException("Sensor not found with ID: " + sensorReadingDto.sensorId()));

    SensorReading sensorReading = new SensorReading(
            sensorReadingDto.value(),
            sensorReadingDto.timestamp(),
            sensor
    );
    SensorReading savedReading = sensorReadingRepository.save(sensorReading);

    double value = sensorReadingDto.value();
    String alertMessage = null;

    switch (sensor.getType()) {
      case TEMPERATURE:
        if (value > 40.0 || value < -20.0) {
          alertMessage = "Temperature out of range: " + value;
        }
        break;
      case HUMIDITY:
        if (value > 90.0) {
          alertMessage = "Humidity too high: " + value;
        }
        break;
      case ATMOSPHERIC_PRESSURE:
        if (value < 950.0 || value > 1050.0) {
          alertMessage = "Pressure out of range: " + value;
        }
        break;
    }

    if (alertMessage != null) {
      Alert alert = new Alert(
              alertMessage,
              sensorReadingDto.timestamp(),
              sensor
      );
      alertRepository.save(alert);
    }

    return savedReading;
  }

}