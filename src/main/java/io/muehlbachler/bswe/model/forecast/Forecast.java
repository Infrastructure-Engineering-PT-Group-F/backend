package io.muehlbachler.bswe.model.forecast;

import java.time.LocalDateTime;
import java.util.SequencedMap;

import io.muehlbachler.bswe.model.location.Coordinates;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Identifies a location's forecast.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = { "coordinates", "requestTime" })
@ToString
public class Forecast {
  private Coordinates coordinates;
  private LocalDateTime requestTime;
  private Units units;
  private CurrentWeather currentWeather;
  private SequencedMap<LocalDateTime, HourlyForecast> hourlyForecast;
}
