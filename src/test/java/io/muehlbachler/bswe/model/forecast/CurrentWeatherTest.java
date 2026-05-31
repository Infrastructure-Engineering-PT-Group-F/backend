package io.muehlbachler.bswe.model.forecast;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class CurrentWeatherTest {
  @Test
  void testDefaultConstructor() {
    CurrentWeather weather = new CurrentWeather();

    assertEquals(0.0f, weather.getTemperature());
    assertEquals(0.0f, weather.getFeelsLike());
    assertEquals(0, weather.getHumidity());
    assertEquals(0.0f, weather.getDewPoint());
    assertEquals(0.0f, weather.getPrecipitation());
    assertEquals(0.0f, weather.getWindSpeed());
    assertEquals(0, weather.getWindDirection());
    assertEquals(0.0f, weather.getWindGusts());
  }

  @Test
  void testParameterizedConstructor() {
    float temperature = 20.5f;
    float feelsLike = 22.0f;
    int humidity = 65;
    float dewPoint = 15.5f;
    float precipitation = 0.5f;
    float windSpeed = 10.5f;
    int windDirection = 180;
    float windGusts = 15.5f;

    CurrentWeather weather = new CurrentWeather(temperature, feelsLike, humidity,
        dewPoint, precipitation, windSpeed, windDirection, windGusts);

    assertEquals(temperature, weather.getTemperature());
    assertEquals(feelsLike, weather.getFeelsLike());
    assertEquals(humidity, weather.getHumidity());
    assertEquals(dewPoint, weather.getDewPoint());
    assertEquals(precipitation, weather.getPrecipitation());
    assertEquals(windSpeed, weather.getWindSpeed());
    assertEquals(windDirection, weather.getWindDirection());
    assertEquals(windGusts, weather.getWindGusts());
  }

  @Test
  void testSettersAndGetters() {
    CurrentWeather weather = new CurrentWeather();

    weather.setTemperature(20.5f);
    weather.setFeelsLike(22.0f);
    weather.setHumidity(65);
    weather.setDewPoint(15.5f);
    weather.setPrecipitation(0.5f);
    weather.setWindSpeed(10.5f);
    weather.setWindDirection(180);
    weather.setWindGusts(15.5f);

    assertEquals(20.5f, weather.getTemperature());
    assertEquals(22.0f, weather.getFeelsLike());
    assertEquals(65, weather.getHumidity());
    assertEquals(15.5f, weather.getDewPoint());
    assertEquals(0.5f, weather.getPrecipitation());
    assertEquals(10.5f, weather.getWindSpeed());
    assertEquals(180, weather.getWindDirection());
    assertEquals(15.5f, weather.getWindGusts());
  }

  @Test
  void testEqualsAndHashCode() {
    CurrentWeather weather1 = new CurrentWeather(20.5f, 22.0f, 65, 15.5f, 0.5f, 10.5f, 180, 15.5f);
    CurrentWeather weather2 = new CurrentWeather(20.5f, 22.0f, 65, 15.5f, 0.5f, 10.5f, 180, 15.5f);
    CurrentWeather weather3 = new CurrentWeather(21.5f, 22.0f, 65, 15.5f, 0.5f, 10.5f, 180, 15.5f);

    assertEquals(weather1, weather2);
    assertNotEquals(weather1, weather3);
    assertEquals(weather1.hashCode(), weather2.hashCode());
    assertNotEquals(weather1.hashCode(), weather3.hashCode());
  }

  @Test
  void testToString() {
    CurrentWeather weather = new CurrentWeather(20.5f, 22.0f, 65, 15.5f, 0.5f, 10.5f, 180, 15.5f);
    String toString = weather.toString();

    assertTrue(toString.contains("20.5"));
    assertTrue(toString.contains("22.0"));
    assertTrue(toString.contains("65"));
    assertTrue(toString.contains("180"));
  }
}
