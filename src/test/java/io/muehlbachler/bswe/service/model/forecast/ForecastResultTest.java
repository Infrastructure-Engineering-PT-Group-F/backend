package io.muehlbachler.bswe.service.model.forecast;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;

import io.muehlbachler.bswe.model.forecast.Forecast;
import io.muehlbachler.bswe.model.forecast.HourlyForecast;
import org.junit.jupiter.api.Test;

class ForecastResultTest {
  @Test
  public void testNoArgsConstructor() {
    ForecastResult result = new ForecastResult();
    assertNotNull(result);
    assertNull(result.getCurrent());
    assertNull(result.getHourlyUnits());
    assertNull(result.getHourly());
  }

  @Test
  public void testAllArgsConstructor() {
    ForecastResultCurrentWeather current = new ForecastResultCurrentWeather();
    ForecastResultUnits units = new ForecastResultUnits();
    ForecastResultHourlyForecast hourly = new ForecastResultHourlyForecast();

    ForecastResult result = new ForecastResult(current, units, hourly);

    assertEquals(current, result.getCurrent());
    assertEquals(units, result.getHourlyUnits());
    assertEquals(hourly, result.getHourly());
  }

  @Test
  public void testSettersAndGetters() {
    ForecastResult result = new ForecastResult();

    result.setCurrent(new ForecastResultCurrentWeather());
    result.setHourlyUnits(new ForecastResultUnits());
    result.setHourly(new ForecastResultHourlyForecast());

    assertNotNull(result.getCurrent());
    assertNotNull(result.getHourlyUnits());
    assertNotNull(result.getHourly());
  }

  @Test
  public void testTransformToForecast() {
    ForecastResultCurrentWeather current = new ForecastResultCurrentWeather();
    current.setTemperature2m(20.5f);
    current.setApparentTemperature(21.0f);
    current.setRelativeHumidity2m(65);
    current.setDewPoint2m(14.0f);
    current.setPrecipitation(0.0f);
    current.setCloudCover(30);
    current.setWindSpeed10m(15.0f);
    current.setWindDirection10m(180);
    current.setWindGusts10m(25.0f);

    ForecastResultUnits units = new ForecastResultUnits(
        "°C", "%", "°C", "°C", "%",
        "mm", "%", "m", "km/h", "°", "km/h");

    ForecastResultHourlyForecast hourly = new ForecastResultHourlyForecast();
    LocalDateTime time = LocalDateTime.now();
    hourly.setTime(new LocalDateTime[] { time });
    hourly.setTemperature2m(new float[] { 22.0f });
    hourly.setApparentTemperature(new float[] { 23.0f });
    hourly.setRelativeHumidity2m(new int[] { 70 });
    hourly.setDewPoint2m(new float[] { 16.0f });
    hourly.setPrecipitationProbability(new int[] { 20 });
    hourly.setPrecipitation(new float[] { 0.5f });
    hourly.setCloudCover(new int[] { 40 });
    hourly.setVisibility(new int[] { 10000 });
    hourly.setWindSpeed10m(new float[] { 18.0f });
    hourly.setWindDirection10m(new int[] { 180 });
    hourly.setWindGusts10m(new float[] { 25.0f });

    ForecastResult result = new ForecastResult(current, units, hourly);

    Forecast forecast = result.transformToForecast();
    assertNotNull(forecast);

    assertNotNull(forecast.getUnits());
    assertEquals("°C", forecast.getUnits().getTemperature());
    assertEquals("°C", forecast.getUnits().getFeelsLike());
    assertEquals("%", forecast.getUnits().getHumidity());
    assertEquals("°C", forecast.getUnits().getDewPoint());
    assertEquals("%", forecast.getUnits().getPrecipitationProbability());
    assertEquals("mm", forecast.getUnits().getPrecipitation());
    assertEquals("%", forecast.getUnits().getCloudCover());
    assertEquals("m", forecast.getUnits().getVisibility());
    assertEquals("km/h", forecast.getUnits().getWindSpeed());
    assertEquals("°", forecast.getUnits().getWindDirection());
    assertEquals("km/h", forecast.getUnits().getWindGusts());

    assertNotNull(forecast.getCurrentWeather());
    assertEquals(20.5f, forecast.getCurrentWeather().getTemperature());
    assertEquals(21.0f, forecast.getCurrentWeather().getFeelsLike());
    assertEquals(65, forecast.getCurrentWeather().getHumidity());
    assertEquals(14.0f, forecast.getCurrentWeather().getDewPoint());
    assertEquals(0.0f, forecast.getCurrentWeather().getPrecipitation());
    assertEquals(15.0f, forecast.getCurrentWeather().getWindSpeed());
    assertEquals(180, forecast.getCurrentWeather().getWindDirection());
    assertEquals(25.0f, forecast.getCurrentWeather().getWindGusts());

    assertEquals(1, forecast.getHourlyForecast().size());
    HourlyForecast hourlyForecast = forecast.getHourlyForecast().get(time);
    assertNotNull(hourlyForecast);
    assertEquals(time, hourlyForecast.getTime());
    assertEquals(22.0f, hourlyForecast.getTemperature());
    assertEquals(23.0f, hourlyForecast.getFeelsLike());
    assertEquals(70, hourlyForecast.getHumidity());
    assertEquals(16.0f, hourlyForecast.getDewPoint());
    assertEquals(0.5f, hourlyForecast.getPrecipitation());
    assertEquals(20, hourlyForecast.getPrecipitationProbability());
    assertEquals(40, hourlyForecast.getCloudCover());
    assertEquals(10000, hourlyForecast.getVisibility());
    assertEquals(18.0f, hourlyForecast.getWindSpeed());
    assertEquals(180, hourlyForecast.getWindDirection());
    assertEquals(25.0f, hourlyForecast.getWindGusts());
  }

  @Test
  public void testTransformToForecastNull() {
    ForecastResult result = new ForecastResult();
    Forecast forecast = result.transformToForecast();

    assertNotNull(forecast);
    assertNull(forecast.getUnits());
    assertNull(forecast.getCurrentWeather());
    assertNotNull(forecast.getHourlyForecast());
    assertTrue(forecast.getHourlyForecast().isEmpty());
  }

  @Test
  public void testTransformToForecastPartialNull() {
    ForecastResult result = new ForecastResult(
        null,
        new ForecastResultUnits("°C", "%", "°C", "°C", "%", "mm", "%", "m", "km/h", "°", "km/h"),
        null);

    Forecast forecast = result.transformToForecast();

    assertNotNull(forecast);
    assertNotNull(forecast.getUnits());
    assertNull(forecast.getCurrentWeather());
    assertNotNull(forecast.getHourlyForecast());
    assertTrue(forecast.getHourlyForecast().isEmpty());

    assertEquals("°C", forecast.getUnits().getTemperature());
    assertEquals("%", forecast.getUnits().getHumidity());
  }

  @Test
  public void testToString() {
    ForecastResult result = new ForecastResult(
        new ForecastResultCurrentWeather(),
        new ForecastResultUnits(),
        new ForecastResultHourlyForecast());

    String toString = result.toString();
    assertNotNull(toString);
    assertTrue(toString.contains("current="));
    assertTrue(toString.contains("hourlyUnits="));
    assertTrue(toString.contains("hourly="));
  }
}
