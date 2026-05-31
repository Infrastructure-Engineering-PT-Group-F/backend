package io.muehlbachler.bswe.model.forecast;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

public class HourlyForecastTest {
  @Test
  void testNoArgsConstructor() {
    HourlyForecast forecast = new HourlyForecast();

    assertNotNull(forecast);
    assertNull(forecast.getTime());
    assertEquals(0.0f, forecast.getTemperature(), 0.001);
    assertEquals(0.0f, forecast.getFeelsLike(), 0.001);
    assertEquals(0, forecast.getHumidity());
    assertEquals(0.0f, forecast.getDewPoint(), 0.001);
    assertEquals(0, forecast.getPrecipitationProbability());
    assertEquals(0.0f, forecast.getPrecipitation(), 0.001);
    assertEquals(0, forecast.getCloudCover());
    assertEquals(0, forecast.getVisibility());
    assertEquals(0.0f, forecast.getWindSpeed(), 0.001);
    assertEquals(0, forecast.getWindDirection());
    assertEquals(0.0f, forecast.getWindGusts(), 0.001);
  }

  @Test
  void testAllArgsConstructor() {
    LocalDateTime time = LocalDateTime.now();
    HourlyForecast forecast = new HourlyForecast(
        time, 20.5f, 19.8f, 65, 15.3f, 30,
        0.5f, 75, 10000, 12.3f, 180, 15.7f);

    assertNotNull(forecast);
    assertEquals(time, forecast.getTime());
    assertEquals(20.5f, forecast.getTemperature(), 0.001);
    assertEquals(19.8f, forecast.getFeelsLike(), 0.001);
    assertEquals(65, forecast.getHumidity());
    assertEquals(15.3f, forecast.getDewPoint(), 0.001);
    assertEquals(30, forecast.getPrecipitationProbability());
    assertEquals(0.5f, forecast.getPrecipitation(), 0.001);
    assertEquals(75, forecast.getCloudCover());
    assertEquals(10000, forecast.getVisibility());
    assertEquals(12.3f, forecast.getWindSpeed(), 0.001);
    assertEquals(180, forecast.getWindDirection());
    assertEquals(15.7f, forecast.getWindGusts(), 0.001);
  }

  @Test
  void testSettersAndGetters() {
    HourlyForecast forecast = new HourlyForecast();
    LocalDateTime time = LocalDateTime.now();

    forecast.setTime(time);
    forecast.setTemperature(22.5f);
    forecast.setFeelsLike(21.8f);
    forecast.setHumidity(70);
    forecast.setDewPoint(16.4f);
    forecast.setPrecipitationProbability(40);
    forecast.setPrecipitation(1.2f);
    forecast.setCloudCover(85);
    forecast.setVisibility(8000);
    forecast.setWindSpeed(8.5f);
    forecast.setWindDirection(270);
    forecast.setWindGusts(10.2f);

    assertEquals(time, forecast.getTime());
    assertEquals(22.5f, forecast.getTemperature(), 0.001);
    assertEquals(21.8f, forecast.getFeelsLike(), 0.001);
    assertEquals(70, forecast.getHumidity());
    assertEquals(16.4f, forecast.getDewPoint(), 0.001);
    assertEquals(40, forecast.getPrecipitationProbability());
    assertEquals(1.2f, forecast.getPrecipitation(), 0.001);
    assertEquals(85, forecast.getCloudCover());
    assertEquals(8000, forecast.getVisibility());
    assertEquals(8.5f, forecast.getWindSpeed(), 0.001);
    assertEquals(270, forecast.getWindDirection());
    assertEquals(10.2f, forecast.getWindGusts(), 0.001);
  }

  @Test
  void testEquals() {
    LocalDateTime time = LocalDateTime.now();
    HourlyForecast forecast1 = new HourlyForecast(
        time, 20.5f, 19.8f, 65, 15.3f, 30,
        0.5f, 75, 10000, 12.3f, 180, 15.7f);
    HourlyForecast forecast2 = new HourlyForecast(
        time, 22.0f, 21.0f, 70, 16.0f, 40,
        0.7f, 80, 9000, 13.0f, 190, 16.0f);
    HourlyForecast forecast3 = new HourlyForecast(
        time.plusHours(1), 20.5f, 19.8f, 65, 15.3f, 30,
        0.5f, 75, 10000, 12.3f, 180, 15.7f);

    assertEquals(forecast1, forecast2, "Forecasts with same time should be equal");
    assertNotEquals(forecast1, forecast3, "Forecasts with different times should not be equal");
  }

  @Test
  void testToString() {
    LocalDateTime time = LocalDateTime.now();
    HourlyForecast forecast = new HourlyForecast(
        time, 20.5f, 19.8f, 65, 15.3f, 30,
        0.5f, 75, 10000, 12.3f, 180, 15.7f);

    String toString = forecast.toString();
    assertNotNull(toString);
    assertTrue(toString.contains(time.toString()));
    assertTrue(toString.contains("20.5"));
  }
}
