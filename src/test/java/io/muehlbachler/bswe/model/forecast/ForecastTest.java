package io.muehlbachler.bswe.model.forecast;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.SequencedMap;

import io.muehlbachler.bswe.model.location.Coordinates;
import org.junit.jupiter.api.Test;

class ForecastTest {
  private final Units units = new Units(
      "celsius",
      "celsius",
      "percent",
      "celsius",
      "percent",
      "mm",
      "percent",
      "km",
      "kmh",
      "degrees",
      "kmh");

  @Test
  void testDefaultConstructor() {
    Forecast forecast = new Forecast();

    assertNull(forecast.getCoordinates());
    assertNull(forecast.getRequestTime());
    assertNull(forecast.getUnits());
    assertNull(forecast.getCurrentWeather());
    assertNull(forecast.getHourlyForecast());
  }

  @Test
  void testParameterizedConstructor() {
    Coordinates coordinates = new Coordinates(15.5, 48.2, 300.5f);
    LocalDateTime requestTime = LocalDateTime.now();
    CurrentWeather currentWeather = new CurrentWeather();
    SequencedMap<LocalDateTime, HourlyForecast> hourlyForecast = new LinkedHashMap<>();

    Forecast forecast = new Forecast(coordinates, requestTime, units, currentWeather, hourlyForecast);

    assertEquals(coordinates, forecast.getCoordinates());
    assertEquals(requestTime, forecast.getRequestTime());
    assertEquals(units, forecast.getUnits());
    assertEquals(currentWeather, forecast.getCurrentWeather());
    assertEquals(hourlyForecast, forecast.getHourlyForecast());
  }

  @Test
  void testSettersAndGetters() {
    Forecast forecast = new Forecast();

    Coordinates coordinates = new Coordinates(15.5, 48.2, 300.5f);
    LocalDateTime requestTime = LocalDateTime.now();
    CurrentWeather currentWeather = new CurrentWeather();
    SequencedMap<LocalDateTime, HourlyForecast> hourlyForecast = new LinkedHashMap<>();
    forecast.setCoordinates(coordinates);
    forecast.setRequestTime(requestTime);
    forecast.setUnits(units);
    forecast.setCurrentWeather(currentWeather);
    forecast.setHourlyForecast(hourlyForecast);

    assertEquals(coordinates, forecast.getCoordinates());
    assertEquals(requestTime, forecast.getRequestTime());
    assertEquals(units, forecast.getUnits());
    assertEquals(currentWeather, forecast.getCurrentWeather());
    assertEquals(hourlyForecast, forecast.getHourlyForecast());
  }

  @Test
  void testEqualsAndHashCode() {
    LocalDateTime now = LocalDateTime.now();
    Coordinates coords1 = new Coordinates(15.5, 48.2, 300.5f);
    Coordinates coords2 = new Coordinates(15.6, 48.2, 300.5f);

    Forecast forecast1 = new Forecast(coords1, now, null, null, null);
    Forecast forecast2 = new Forecast(coords1, now, null, null, null);
    Forecast forecast3 = new Forecast(coords2, now, null, null, null);

    assertEquals(forecast1, forecast2);
    assertNotEquals(forecast1, forecast3);
    assertEquals(forecast1.hashCode(), forecast2.hashCode());
    assertNotEquals(forecast1.hashCode(), forecast3.hashCode());
  }

  @Test
  void testToString() {
    LocalDateTime now = LocalDateTime.now();
    Coordinates coordinates = new Coordinates(15.5, 48.2, 300.5f);
    Forecast forecast = new Forecast(coordinates, now, units, new CurrentWeather(), new LinkedHashMap<>());

    String toString = forecast.toString();

    assertTrue(toString.contains("15.5"));
    assertTrue(toString.contains("48.2"));
    assertTrue(toString.contains(now.toString()));
    assertTrue(toString.contains("celsius"));
  }
}
