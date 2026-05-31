package io.muehlbachler.bswe.service.model.geocoding;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

class GeocodingResultTest {
  @Test
  void testNoArgsConstructor() {
    GeocodingResult result = new GeocodingResult();

    assertNotNull(result);
    assertNull(result.getResults());
  }

  @Test
  void testAllArgsConstructor() {
    List<GeocodingResultEntry> entries = Arrays.asList(
        new GeocodingResultEntry(16.363449, 48.210033, 151.5f),
        new GeocodingResultEntry(16.372080, 48.208174, 172.0f));

    GeocodingResult result = new GeocodingResult(entries);

    assertNotNull(result);
    assertNotNull(result.getResults());
    assertEquals(2, result.getResults().size());
    assertEquals(16.363449, result.getResults().get(0).getLongitude(), 0.000001);
    assertEquals(48.210033, result.getResults().get(0).getLatitude(), 0.000001);
    assertEquals(151.5f, result.getResults().get(0).getElevation(), 0.001);
  }

  @Test
  void testSettersAndGetters() {
    GeocodingResult result = new GeocodingResult();
    List<GeocodingResultEntry> entries = Arrays.asList(
        new GeocodingResultEntry(16.363449, 48.210033, 151.5f));

    result.setResults(entries);

    assertNotNull(result.getResults());
    assertEquals(1, result.getResults().size());
    assertEquals(16.363449, result.getResults().get(0).getLongitude(), 0.000001);
    assertEquals(48.210033, result.getResults().get(0).getLatitude(), 0.000001);
    assertEquals(151.5f, result.getResults().get(0).getElevation(), 0.001);
  }

  @Test
  void testToString() {
    List<GeocodingResultEntry> entries = Arrays.asList(
        new GeocodingResultEntry(16.363449, 48.210033, 151.5f));
    GeocodingResult result = new GeocodingResult(entries);

    String toString = result.toString();

    assertNotNull(toString);
    assertTrue(toString.contains("16.363449"));
    assertTrue(toString.contains("48.210033"));
    assertTrue(toString.contains("151.5"));
  }
}
