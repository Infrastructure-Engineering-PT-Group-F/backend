package io.muehlbachler.bswe.service;

import tools.jackson.databind.JsonNode;

/**
 * A service to fetch METAR observations from the upstream aviation weather API.
 */
@SuppressWarnings("PMD.ImplicitFunctionalInterface")
public interface MetarService {
  /**
   * Returns the raw METAR observation for the given station.
   *
   * @param icao the ICAO code of the station to fetch the METAR for
   * @return the upstream METAR response, or {@code null} if unavailable
   */
  JsonNode getMetar(String icao);
}
