package io.muehlbachler.bswe.model.forecast;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Represents the current weather at a location.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class CurrentWeather {
  private float temperature;
  private float feelsLike;
  private int humidity;
  private float dewPoint;
  private float precipitation;
  private float windSpeed;
  private int windDirection;
  private float windGusts;
}
