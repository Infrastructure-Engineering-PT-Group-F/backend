package io.muehlbachler.bswe.service.model.nearestairport;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class NearestAirportResultTest {
  @Test
  void testNoArgsConstructor() {
    NearestAirportResult result = new NearestAirportResult();
    assertNotNull(result);
    assertNull(result.getStation());
  }

  @Test
  void testAllArgsConstructor() {
    NearestAirportResultStation station = new NearestAirportResultStation("LOWW", 16.5, 48.1, 183.0f);
    NearestAirportResult result = new NearestAirportResult(station);

    assertNotNull(result);
    assertEquals(station, result.getStation());
  }

  @Test
  void testSettersAndGetters() {
    NearestAirportResultStation station = new NearestAirportResultStation("LOWW", 16.5, 48.1, 183.0f);
    NearestAirportResult result = new NearestAirportResult();
    result.setStation(station);

    assertEquals(station, result.getStation());
    assertEquals("LOWW", result.getStation().getIcao());
    assertEquals(16.5, result.getStation().getLongitude());
    assertEquals(48.1, result.getStation().getLatitude());
    assertEquals(183.0f, result.getStation().getElevation());
  }

  @Test
  void testToString() {
    NearestAirportResultStation station = new NearestAirportResultStation("LOWW", 16.5, 48.1, 183.0f);
    NearestAirportResult result = new NearestAirportResult(station);
    String toString = result.toString();

    assertNotNull(toString);
    assertTrue(toString.contains("LOWW"));
    assertTrue(toString.contains("16.5"));
    assertTrue(toString.contains("48.1"));
    assertTrue(toString.contains("183.0"));
  }
}
