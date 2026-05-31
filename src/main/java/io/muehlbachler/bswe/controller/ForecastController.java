package io.muehlbachler.bswe.controller;

import io.muehlbachler.bswe.error.ApiException;
import io.muehlbachler.bswe.model.forecast.Forecast;
import io.muehlbachler.bswe.model.location.FavoriteLocation;
import io.muehlbachler.bswe.service.FavoriteLocationService;
import io.muehlbachler.bswe.service.ForecastService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller to expose favorite location endpoints.
 */
@AllArgsConstructor
@RestController
@RequestMapping("/api/{userId}/{locationId}/forecast")
@CrossOrigin
public class ForecastController {
  @Autowired
  private final FavoriteLocationService favoriteLocationService;
  @Autowired
  private final ForecastService forecastService;

  /**
   * Creates a new favorite location.
   *
   * @param userId     the user id
   * @param locationId the location id
   * @return the forecast
   */
  @GetMapping("/")
  public ResponseEntity<Forecast> get(@PathVariable final String userId,
      @PathVariable final String locationId) {
    final FavoriteLocation location = favoriteLocationService.get(userId, locationId);
    if (location == null) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    try {
      final Forecast forecast = forecastService.fetch(location.getCoordinates());
      if (forecast == null) {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
      }

      return new ResponseEntity<>(forecast, HttpStatus.OK);
    } catch (final ApiException e) {
      return new ResponseEntity<>(e.getHttpStatus());
    }
  }
}
