package io.muehlbachler.bswe.model.forecast;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class UnitsTest {
  @Test
  void testNoArgsConstructor() {
    Units units = new Units();

    assertNotNull(units);
    assertNull(units.getTemperature());
    assertNull(units.getFeelsLike());
    assertNull(units.getHumidity());
    assertNull(units.getDewPoint());
    assertNull(units.getPrecipitationProbability());
    assertNull(units.getPrecipitation());
    assertNull(units.getCloudCover());
    assertNull(units.getVisibility());
    assertNull(units.getWindSpeed());
    assertNull(units.getWindDirection());
    assertNull(units.getWindGusts());
  }

  @Test
  void testAllArgsConstructor() {
    Units units = new Units(
        "°C", "°C", "%", "°C", "%",
        "mm", "%", "m", "km/h", "°", "km/h");

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
  void testSettersAndGetters() {
    Units units = new Units();

    units.setTemperature("°F");
    units.setFeelsLike("°F");
    units.setHumidity("%");
    units.setDewPoint("°F");
    units.setPrecipitationProbability("%");
    units.setPrecipitation("in");
    units.setCloudCover("%");
    units.setVisibility("mi");
    units.setWindSpeed("mph");
    units.setWindDirection("°");
    units.setWindGusts("mph");

    assertEquals("°F", units.getTemperature());
    assertEquals("°F", units.getFeelsLike());
    assertEquals("%", units.getHumidity());
    assertEquals("°F", units.getDewPoint());
    assertEquals("%", units.getPrecipitationProbability());
    assertEquals("in", units.getPrecipitation());
    assertEquals("%", units.getCloudCover());
    assertEquals("mi", units.getVisibility());
    assertEquals("mph", units.getWindSpeed());
    assertEquals("°", units.getWindDirection());
    assertEquals("mph", units.getWindGusts());
  }

  @Test
  void testEquals() {
    Units units1 = new Units(
        "°C", "°C", "%", "°C", "%",
        "mm", "%", "m", "km/h", "°", "km/h");
    Units units2 = new Units(
        "°C", "°C", "%", "°C", "%",
        "mm", "%", "m", "km/h", "°", "km/h");
    Units units3 = new Units(
        "°F", "°F", "%", "°F", "%",
        "in", "%", "mi", "mph", "°", "mph");

    assertEquals(units1, units2, "Identical units should be equal");
    assertNotEquals(units1, units3, "Different units should not be equal");
    assertEquals(units1, units1, "Same instance should be equal");
    assertNotEquals(units1, null, "Units should not be equal to null");
  }

  @Test
  void testToString() {
    Units units = new Units(
        "°C", "°C", "%", "°C", "%",
        "mm", "%", "m", "km/h", "°", "km/h");

    String toString = units.toString();
    assertNotNull(toString);
    assertTrue(toString.contains("°C"));
    assertTrue(toString.contains("km/h"));
    assertTrue(toString.contains("%"));
  }

  @Test
  void testHashCode() {
    Units units1 = new Units(
        "°C", "°C", "%", "°C", "%",
        "mm", "%", "m", "km/h", "°", "km/h");
    Units units2 = new Units(
        "°C", "°C", "%", "°C", "%",
        "mm", "%", "m", "km/h", "°", "km/h");

    assertEquals(units1.hashCode(), units2.hashCode(), "Hash codes should be equal for equal objects");
  }
}
