package com.aldisued.iot.monitoring.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class MeasurementCalculatorService {

  public List<Double> filterByAverageDeviation(List<Double> values, Double deviation) {
    if (deviation == null || deviation < 0.0 || deviation > 1.0) {
      throw new IllegalArgumentException("Deviation must be between 0.0 and 1.0");
    }

    if (values == null || values.isEmpty()) {
      return List.of();
    }

    double average = values.stream()
            .mapToDouble(Double::doubleValue)
            .average()
            .orElse(0.0);

    double margin = deviation * Math.abs(average);  // Math.abs - prepare for minus values like temp
    double lowerBound = average - margin;
    double upperBound = average + margin;

    return values.stream()
            .filter(value -> value >= lowerBound && value <= upperBound)
            .collect(Collectors.toList());
  }

}