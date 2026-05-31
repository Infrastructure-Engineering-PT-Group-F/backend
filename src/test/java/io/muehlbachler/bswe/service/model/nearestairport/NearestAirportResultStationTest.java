package io.muehlbachler.bswe.service.model.nearestairport;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class NearestAirportResultStationTest {
  @Test
  void testNoArgsConstructor() {
    NearestAirportResultStation station = new NearestAirportResultStation();
    assertNotNull(station);
    assertNull(station.getIcao());
    assertEquals(0.0, station.getLongitude());
    assertEquals(0.0, station.getLatitude());
    assertEquals(0.0f, station.getElevation());
  }

  @Test
  void testAllArgsConstructor() {
    String icao = "LOWW";
    double longitude = 16.5;
    double latitude = 48.1;
    float elevation = 183.0f;

    NearestAirportResultStation station = new NearestAirportResultStation(icao, longitude, latitude, elevation);

    assertNotNull(station);
    assertEquals(icao, station.getIcao());
    assertEquals(longitude, station.getLongitude());
    assertEquals(latitude, station.getLatitude());
    assertEquals(elevation, station.getElevation());
  }

  @Test
  void testSettersAndGetters() {
    NearestAirportResultStation station = new NearestAirportResultStation();
    station.setIcao("LOWW");
    station.setLongitude(16.5);
    station.setLatitude(48.1);
    station.setElevation(183.0f);

    assertEquals("LOWW", station.getIcao());
    assertEquals(16.5, station.getLongitude());
    assertEquals(48.1, station.getLatitude());
    assertEquals(183.0f, station.getElevation());
  }

  @Test
  void testToString() {
    NearestAirportResultStation station = new NearestAirportResultStation("LOWW", 16.5, 48.1, 183.0f);
    String toString = station.toString();

    assertNotNull(toString);
    assertTrue(toString.contains("LOWW"));
    assertTrue(toString.contains("16.5"));
    assertTrue(toString.contains("48.1"));
    assertTrue(toString.contains("183.0"));
  }
}
