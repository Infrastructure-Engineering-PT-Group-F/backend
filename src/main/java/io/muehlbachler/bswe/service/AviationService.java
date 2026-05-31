package io.muehlbachler.bswe.service;

import io.muehlbachler.bswe.model.location.Coordinates;
import io.muehlbachler.bswe.service.model.nearestairport.NearestAirportResultStation;

/**
 * A service to handle all aviation related actions.
 */
@SuppressWarnings("PMD.ImplicitFunctionalInterface")
public interface AviationService {
  /**
   * Returns the nearest airport to the given coordinates.
   *
   * @param coordinates the coordinates to search for the nearest airport
   * @return the nearest airport to the given coordinates
   */
  NearestAirportResultStation getNearestAirport(Coordinates coordinates);
}
