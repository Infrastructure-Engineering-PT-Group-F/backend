package io.muehlbachler.bswe.model.forecast;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Represents the forecast for a specific hour.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "time")
@ToString
public class HourlyForecast {
  private LocalDateTime time;
  private float temperature;
  private float feelsLike;
  private int humidity;
  private float dewPoint;
  private int precipitationProbability;
  private float precipitation;
  private int cloudCover;
  private int visibility;
  private float windSpeed;
  private int windDirection;
  private float windGusts;
}
