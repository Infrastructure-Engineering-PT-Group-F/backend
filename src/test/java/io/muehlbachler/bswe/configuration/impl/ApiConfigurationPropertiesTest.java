package io.muehlbachler.bswe.configuration.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import io.muehlbachler.bswe.configuration.ApiConfiguration.ApiConnectionAuthorization;
import io.muehlbachler.bswe.configuration.ApiConfiguration.ApiConnectionInformation;
import org.junit.jupiter.api.Test;

public class ApiConfigurationPropertiesTest {
  @Test
  void testNoArgsConstructor() {
    final ApiConfigurationProperties config = new ApiConfigurationProperties();
    assertNotNull(config);
  }

  @Test
  void testAllArgsConstructor() {
    final ApiConnectionInformation geocoding = new ApiConnectionInformation("geo-url", 0, null);
    final ApiConnectionInformation forecast = new ApiConnectionInformation("forecast-url", 900, null);
    final ApiConnectionInformation metar = new ApiConnectionInformation("metar-url", 0,
        new ApiConnectionAuthorization("auth", "key"));
    final ApiConnectionInformation nearestAirport = new ApiConnectionInformation("airport-url", 0,
        new ApiConnectionAuthorization("auth", "key"));

    final ApiConfigurationProperties config = new ApiConfigurationProperties(
        geocoding, forecast, metar, nearestAirport);

    assertNotNull(config);
    assertEquals(geocoding, config.getGeocoding());
    assertEquals(forecast, config.getForecast());
    assertEquals(metar, config.getMetar());
    assertEquals(nearestAirport, config.getNearestAirport());
  }

  @Test
  void testSetters() {
    final ApiConfigurationProperties config = new ApiConfigurationProperties();
    final ApiConnectionInformation geocoding = new ApiConnectionInformation("geo-url", 0, null);
    final ApiConnectionInformation forecast = new ApiConnectionInformation("forecast-url", 900, null);
    final ApiConnectionInformation metar = new ApiConnectionInformation("metar-url", 0,
        new ApiConnectionAuthorization("auth", "key"));
    final ApiConnectionInformation nearestAirport = new ApiConnectionInformation("airport-url", 0,
        new ApiConnectionAuthorization("auth", "key"));

    config.setGeocoding(geocoding);
    config.setForecast(forecast);
    config.setMetar(metar);
    config.setNearestAirport(nearestAirport);

    assertEquals(geocoding, config.getGeocoding());
    assertEquals(forecast, config.getForecast());
    assertEquals(metar, config.getMetar());
    assertEquals(nearestAirport, config.getNearestAirport());
  }
}
