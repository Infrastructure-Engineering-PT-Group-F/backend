package io.muehlbachler.bswe.model.forecast;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Represents the units of the forecast.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class Units {
  private String temperature;
  private String feelsLike;
  private String humidity;
  private String dewPoint;
  private String precipitationProbability;
  private String precipitation;
  private String cloudCover;
  private String visibility;
  private String windSpeed;
  private String windDirection;
  private String windGusts;
}
