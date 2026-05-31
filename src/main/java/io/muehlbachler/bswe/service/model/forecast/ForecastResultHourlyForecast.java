package io.muehlbachler.bswe.service.model.forecast;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.SequencedMap;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.muehlbachler.bswe.model.forecast.HourlyForecast;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

/**
 * Represents the hourly forecast of a forecast result.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ForecastResultHourlyForecast {
  private LocalDateTime[] time;
  @JsonProperty("temperature_2m")
  private float[] temperature2m;
  @JsonProperty("relative_humidity_2m")
  private int[] relativeHumidity2m;
  @JsonProperty("dew_point_2m")
  private float[] dewPoint2m;
  private float[] apparentTemperature;
  private int[] precipitationProbability;
  private float[] precipitation;
  private int[] cloudCover;
  private int[] visibility;
  @JsonProperty("wind_speed_10m")
  private float[] windSpeed10m;
  @JsonProperty("wind_direction_10m")
  private int[] windDirection10m;
  @JsonProperty("wind_gusts_10m")
  private float[] windGusts10m;

  /**
   * Transforms the forecast result hourly forecast into an array of
   * {@link HourlyForecast} objects.
   *
   * @return the transformed hourly forecast
   */
  public SequencedMap<LocalDateTime, HourlyForecast> transformToHourlyForecasts() {
    final SequencedMap<LocalDateTime, HourlyForecast> hourlyForecast = new LinkedHashMap<>();

    if (valuesNotNull() && valuesHaveSameLength()) {
      for (int forecastNo = 0; forecastNo < time.length; forecastNo++) {
        final HourlyForecast forecast = new HourlyForecast();
        forecast.setTime(time[forecastNo]);
        forecast.setTemperature(temperature2m[forecastNo]);
        forecast.setHumidity(relativeHumidity2m[forecastNo]);
        forecast.setFeelsLike(apparentTemperature[forecastNo]);
        forecast.setDewPoint(dewPoint2m[forecastNo]);
        forecast.setPrecipitation(precipitation[forecastNo]);
        forecast.setPrecipitationProbability(precipitationProbability[forecastNo]);
        forecast.setWindSpeed(windSpeed10m[forecastNo]);
        forecast.setWindDirection(windDirection10m[forecastNo]);
        forecast.setWindGusts(windGusts10m[forecastNo]);
        forecast.setCloudCover(cloudCover[forecastNo]);
        forecast.setVisibility(visibility[forecastNo]);
        hourlyForecast.put(time[forecastNo], forecast);
      }
    }

    return hourlyForecast;
  }

  /**
   * Checks if all values are not null.
   *
   * @return true if all values are not null, false otherwise
   */
  private boolean valuesNotNull() {
    return time != null && temperature2m != null && relativeHumidity2m != null && dewPoint2m != null
        && apparentTemperature != null && precipitationProbability != null && precipitation != null
        && cloudCover != null && visibility != null && windSpeed10m != null && windDirection10m != null
        && windGusts10m != null;
  }

  /**
   * Checks if all values have the same length.
   *
   * @return true if all values have the same length, false otherwise
   */
  public boolean valuesHaveSameLength() {
    return time.length == temperature2m.length && time.length == relativeHumidity2m.length
        && time.length == dewPoint2m.length && time.length == apparentTemperature.length
        && time.length == precipitationProbability.length && time.length == precipitation.length
        && time.length == cloudCover.length && time.length == visibility.length && time.length == windSpeed10m.length
        && time.length == windDirection10m.length && time.length == windGusts10m.length;
  }
}
