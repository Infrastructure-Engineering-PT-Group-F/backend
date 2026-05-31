package io.muehlbachler.bswe.service.model.forecast;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.muehlbachler.bswe.model.forecast.Units;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

/**
 * Represents the units of a forecast result.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ForecastResultUnits {
  @JsonProperty("temperature_2m")
  private String temperature2m;
  @JsonProperty("relative_humidity_2m")
  private String relativeHumidity2m;
  @JsonProperty("dew_point_2m")
  private String dewPoint2m;
  private String apparentTemperature;
  private String precipitationProbability;
  private String precipitation;
  private String cloudCover;
  private String visibility;
  @JsonProperty("wind_speed_10m")
  private String windSpeed10m;
  @JsonProperty("wind_direction_10m")
  private String windDirection10m;
  @JsonProperty("wind_gusts_10m")
  private String windGusts10m;

  /**
   * Transforms the forecast result units into a {@link Units} object.
   *
   * @return the transformed units
   */
  public Units transformToUnits() {
    final Units units = new Units();
    units.setTemperature(temperature2m);
    units.setHumidity(relativeHumidity2m);
    units.setFeelsLike(apparentTemperature);
    units.setPrecipitationProbability(precipitationProbability);
    units.setPrecipitation(precipitation);
    units.setDewPoint(dewPoint2m);
    units.setCloudCover(cloudCover);
    units.setVisibility(visibility);
    units.setWindSpeed(windSpeed10m);
    units.setWindDirection(windDirection10m);
    units.setWindGusts(windGusts10m);
    return units;
  }
}
