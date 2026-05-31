package io.muehlbachler.bswe.service.model.forecast;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.muehlbachler.bswe.model.forecast.CurrentWeather;
import org.junit.jupiter.api.Test;

public class ForecastResultCurrentWeatherTest {
  @Test
  public void testTransformToCurrentWeather() {
    ForecastResultCurrentWeather forecastResultCurrentWeather = new ForecastResultCurrentWeather();
    forecastResultCurrentWeather.setTemperature2m(1.0f);
    forecastResultCurrentWeather.setRelativeHumidity2m(2);
    forecastResultCurrentWeather.setDewPoint2m(3.0f);
    forecastResultCurrentWeather.setApparentTemperature(4.0f);
    forecastResultCurrentWeather.setPrecipitationProbability(5);
    forecastResultCurrentWeather.setPrecipitation(6.0f);
    forecastResultCurrentWeather.setCloudCover(7);
    forecastResultCurrentWeather.setVisibility(8);
    forecastResultCurrentWeather.setWindSpeed10m(9.0f);
    forecastResultCurrentWeather.setWindDirection10m(10);
    forecastResultCurrentWeather.setWindGusts10m(11.0f);

    CurrentWeather currentWeather = forecastResultCurrentWeather.transformToCurrentWeather();

    assertEquals(1.0f, currentWeather.getTemperature());
    assertEquals(2, currentWeather.getHumidity());
    assertEquals(3.0f, currentWeather.getDewPoint());
    assertEquals(4.0f, currentWeather.getFeelsLike());
    assertEquals(6.0f, currentWeather.getPrecipitation());
    assertEquals(9.0f, currentWeather.getWindSpeed());
    assertEquals(10, currentWeather.getWindDirection());
    assertEquals(11.0f, currentWeather.getWindGusts());
  }

  @Test
  public void testTransformToCurrentWeatherNull() {
    ForecastResultCurrentWeather forecastResultCurrentWeather = new ForecastResultCurrentWeather();

    CurrentWeather currentWeather = forecastResultCurrentWeather.transformToCurrentWeather();

    assertEquals(0.0f, currentWeather.getTemperature());
    assertEquals(0, currentWeather.getHumidity());
    assertEquals(0.0f, currentWeather.getDewPoint());
    assertEquals(0.0f, currentWeather.getFeelsLike());
    assertEquals(0.0f, currentWeather.getPrecipitation());
    assertEquals(0.0f, currentWeather.getWindSpeed());
    assertEquals(0, currentWeather.getWindDirection());
    assertEquals(0.0f, currentWeather.getWindGusts());
  }

  @Test
  void testNoArgsConstructor() {
    ForecastResultCurrentWeather weather = new ForecastResultCurrentWeather();
    assertNotNull(weather);
    assertEquals(0.0f, weather.getTemperature2m(), 0.001);
    assertEquals(0, weather.getRelativeHumidity2m());
    assertEquals(0.0f, weather.getDewPoint2m(), 0.001);
    assertEquals(0.0f, weather.getApparentTemperature(), 0.001);
    assertEquals(0, weather.getPrecipitationProbability());
    assertEquals(0.0f, weather.getPrecipitation(), 0.001);
    assertEquals(0, weather.getCloudCover());
    assertEquals(0, weather.getVisibility());
    assertEquals(0.0f, weather.getWindSpeed10m(), 0.001);
    assertEquals(0, weather.getWindDirection10m());
    assertEquals(0.0f, weather.getWindGusts10m(), 0.001);
  }

  @Test
  void testAllArgsConstructor() {
    ForecastResultCurrentWeather weather = new ForecastResultCurrentWeather(
        20.5f, 65, 15.3f, 19.8f, 30, 0.5f,
        75, 10000, 12.3f, 180, 15.7f);

    assertEquals(20.5f, weather.getTemperature2m(), 0.001);
    assertEquals(65, weather.getRelativeHumidity2m());
    assertEquals(15.3f, weather.getDewPoint2m(), 0.001);
    assertEquals(19.8f, weather.getApparentTemperature(), 0.001);
    assertEquals(30, weather.getPrecipitationProbability());
    assertEquals(0.5f, weather.getPrecipitation(), 0.001);
    assertEquals(75, weather.getCloudCover());
    assertEquals(10000, weather.getVisibility());
    assertEquals(12.3f, weather.getWindSpeed10m(), 0.001);
    assertEquals(180, weather.getWindDirection10m());
    assertEquals(15.7f, weather.getWindGusts10m(), 0.001);
  }

  @Test
  void testToString() {
    ForecastResultCurrentWeather weather = new ForecastResultCurrentWeather(
        20.5f, 65, 15.3f, 19.8f, 30, 0.5f,
        75, 10000, 12.3f, 180, 15.7f);

    String toString = weather.toString();
    assertNotNull(toString);
    assertTrue(toString.contains("20.5"));
    assertTrue(toString.contains("65"));
    assertTrue(toString.contains("180"));
  }
}
