package io.muehlbachler.bswe.service.model.forecast;

import java.util.LinkedHashMap;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.muehlbachler.bswe.model.forecast.Forecast;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

/**
 * Represents the result of a forecast request.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ForecastResult {
  private ForecastResultCurrentWeather current;
  private ForecastResultUnits hourlyUnits;
  private ForecastResultHourlyForecast hourly;

  /**
   * Transforms the forecast result into a {@link Forecast} object.
   *
   * @return the transformed forecast
   */
  public Forecast transformToForecast() {
    final Forecast forecast = new Forecast();
    if (hourlyUnits != null) {
      forecast.setUnits(hourlyUnits.transformToUnits());
    }
    if (current != null) {
      forecast.setCurrentWeather(current.transformToCurrentWeather());
    }
    if (hourly != null) {
      forecast.setHourlyForecast(hourly.transformToHourlyForecasts());
    } else {
      forecast.setHourlyForecast(new LinkedHashMap<>());
    }
    return forecast;
  }
}
