package io.muehlbachler.bswe.configuration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents the configuration for the APIs.
 */
public interface ApiConfiguration {
  /**
   * Returns the connection information for the geocoding API.
   *
   * @return the connection information for the geocoding API
   */
  ApiConnectionInformation getGeocoding();

  /**
   * Returns the connection information for the forecast API.
   *
   * @return the connection information for the forecast API
   */
  ApiConnectionInformation getForecast();

  /**
   * Returns the connection information for the METAR API.
   *
   * @return the connection information for the METAR API
   */
  ApiConnectionInformation getMetar();

  /**
   * Returns the connection information for the nearest airport API.
   *
   * @return the connection information for the nearest airport API
   */
  ApiConnectionInformation getNearestAirport();

  /**
   * Represents the connection information for an API.
   */
  @Getter
  @Setter
  @AllArgsConstructor
  @NoArgsConstructor
  class ApiConnectionInformation {
    private String url;
    private int ttl;
    private ApiConnectionAuthorization authorization;
  }

  /**
   * Represents the authorization information for an API connection.
   */
  @Getter
  @Setter
  @AllArgsConstructor
  @NoArgsConstructor
  class ApiConnectionAuthorization {
    private String header;
    private String value;
  }
}
