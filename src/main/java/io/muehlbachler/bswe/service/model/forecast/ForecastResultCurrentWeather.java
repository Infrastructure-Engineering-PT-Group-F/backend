package io.muehlbachler.bswe.service.model.forecast;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.muehlbachler.bswe.model.forecast.CurrentWeather;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

/**
 * Represents the current weather of a forecast result.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ForecastResultCurrentWeather {
  @JsonProperty("temperature_2m")
  private float temperature2m;
  @JsonProperty("relative_humidity_2m")
  private int relativeHumidity2m;
  @JsonProperty("dew_point_2m")
  private float dewPoint2m;
  private float apparentTemperature;
  private int precipitationProbability;
  private float precipitation;
  private int cloudCover;
  private int visibility;
  @JsonProperty("wind_speed_10m")
  private float windSpeed10m;
  @JsonProperty("wind_direction_10m")
  private int windDirection10m;
  @JsonProperty("wind_gusts_10m")
  private float windGusts10m;

  /**
   * Transforms the forecast result current weather into a {@link CurrentWeather}
   * object.
   *
   * @return the transformed current weather
   */
  public CurrentWeather transformToCurrentWeather() {
    final CurrentWeather currentWeather = new CurrentWeather();
    currentWeather.setTemperature(temperature2m);
    currentWeather.setHumidity(relativeHumidity2m);
    currentWeather.setDewPoint(dewPoint2m);
    currentWeather.setFeelsLike(apparentTemperature);
    currentWeather.setPrecipitation(precipitation);
    currentWeather.setWindSpeed(windSpeed10m);
    currentWeather.setWindDirection(windDirection10m);
    currentWeather.setWindGusts(windGusts10m);
    return currentWeather;
  }
}
