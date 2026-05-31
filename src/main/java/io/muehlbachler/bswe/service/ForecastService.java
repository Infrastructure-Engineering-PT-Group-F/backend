package io.muehlbachler.bswe.service;

import io.muehlbachler.bswe.error.ApiException;
import io.muehlbachler.bswe.model.forecast.Forecast;
import io.muehlbachler.bswe.model.location.Coordinates;

/**
 * A service to handle all forecast related actions.
 */
@SuppressWarnings("PMD.ImplicitFunctionalInterface")
public interface ForecastService {
  /**
   * Fetches the forecast for the given coordinates.
   *
   * @param coordinates the coordinates to fetch the forecast for
   * @return the forecast for the given coordinates
   * @throws ApiException if an error occurs while fetching the forecast
   */
  Forecast fetch(Coordinates coordinates) throws ApiException;
}
