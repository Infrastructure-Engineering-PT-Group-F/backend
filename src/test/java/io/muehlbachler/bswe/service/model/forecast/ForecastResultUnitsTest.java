package io.muehlbachler.bswe.service.model.forecast;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.muehlbachler.bswe.model.forecast.Units;
import org.junit.jupiter.api.Test;

class ForecastResultUnitsTest {
  @Test
  void testNoArgsConstructor() {
    ForecastResultUnits units = new ForecastResultUnits();
    assertNotNull(units);
    assertNull(units.getTemperature2m());
    assertNull(units.getRelativeHumidity2m());
    assertNull(units.getDewPoint2m());
    assertNull(units.getApparentTemperature());
    assertNull(units.getPrecipitationProbability());
    assertNull(units.getPrecipitation());
    assertNull(units.getCloudCover());
    assertNull(units.getVisibility());
    assertNull(units.getWindSpeed10m());
    assertNull(units.getWindDirection10m());
    assertNull(units.getWindGusts10m());
  }

  @Test
  void testAllArgsConstructor() {
    ForecastResultUnits units = new ForecastResultUnits(
        "°C", "%", "°C", "°C", "%",
        "mm", "%", "m", "km/h", "°", "km/h");

    assertEquals("°C", units.getTemperature2m());
    assertEquals("%", units.getRelativeHumidity2m());
    assertEquals("°C", units.getDewPoint2m());
    assertEquals("°C", units.getApparentTemperature());
    assertEquals("%", units.getPrecipitationProbability());
    assertEquals("mm", units.getPrecipitation());
    assertEquals("%", units.getCloudCover());
    assertEquals("m", units.getVisibility());
    assertEquals("km/h", units.getWindSpeed10m());
    assertEquals("°", units.getWindDirection10m());
    assertEquals("km/h", units.getWindGusts10m());
  }

  @Test
  void testSettersAndGetters() {
    ForecastResultUnits units = new ForecastResultUnits();

    units.setTemperature2m("°F");
    units.setRelativeHumidity2m("%");
    units.setDewPoint2m("°F");
    units.setApparentTemperature("°F");
    units.setPrecipitationProbability("%");
    units.setPrecipitation("in");
    units.setCloudCover("%");
    units.setVisibility("mi");
    units.setWindSpeed10m("mph");
    units.setWindDirection10m("°");
    units.setWindGusts10m("mph");

    assertEquals("°F", units.getTemperature2m());
    assertEquals("%", units.getRelativeHumidity2m());
    assertEquals("°F", units.getDewPoint2m());
    assertEquals("°F", units.getApparentTemperature());
    assertEquals("%", units.getPrecipitationProbability());
    assertEquals("in", units.getPrecipitation());
    assertEquals("%", units.getCloudCover());
    assertEquals("mi", units.getVisibility());
    assertEquals("mph", units.getWindSpeed10m());
    assertEquals("°", units.getWindDirection10m());
    assertEquals("mph", units.getWindGusts10m());
  }

  @Test
  void testTransformToUnits() {
    ForecastResultUnits forecastUnits = new ForecastResultUnits(
        "°C", "%", "°C", "°C", "%",
        "mm", "%", "m", "km/h", "°", "km/h");

    Units units = forecastUnits.transformToUnits();

    assertNotNull(units);
    assertEquals("°C", units.getTemperature());
    assertEquals("°C", units.getFeelsLike());
    assertEquals("%", units.getHumidity());
    assertEquals("°C", units.getDewPoint());
    assertEquals("%", units.getPrecipitationProbability());
    assertEquals("mm", units.getPrecipitation());
    assertEquals("%", units.getCloudCover());
    assertEquals("m", units.getVisibility());
    assertEquals("km/h", units.getWindSpeed());
    assertEquals("°", units.getWindDirection());
    assertEquals("km/h", units.getWindGusts());
  }

  @Test
  void testToString() {
    ForecastResultUnits units = new ForecastResultUnits(
        "°C", "°C", "%", "°C", "%",
        "mm", "%", "m", "km/h", "°", "km/h");

    String toString = units.toString();
    assertNotNull(toString);
    assertTrue(toString.contains("°C"));
    assertTrue(toString.contains("km/h"));
    assertTrue(toString.contains("%"));
  }

  @Test
  public void testTransformToUnitsNull() {
    ForecastResultUnits forecastResultUnits = new ForecastResultUnits();

    Units units = forecastResultUnits.transformToUnits();

    assertEquals(null, units.getTemperature());
    assertEquals(null, units.getHumidity());
    assertEquals(null, units.getFeelsLike());
    assertEquals(null, units.getPrecipitationProbability());
    assertEquals(null, units.getPrecipitation());
    assertEquals(null, units.getDewPoint());
    assertEquals(null, units.getCloudCover());
    assertEquals(null, units.getVisibility());
    assertEquals(null, units.getWindSpeed());
    assertEquals(null, units.getWindDirection());
    assertEquals(null, units.getWindGusts());
  }
}
