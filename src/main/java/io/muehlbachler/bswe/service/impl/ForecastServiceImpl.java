package io.muehlbachler.bswe.service.impl;

import java.time.LocalDateTime;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.muehlbachler.bswe.configuration.ApiConfiguration;
import io.muehlbachler.bswe.error.ApiException;
import io.muehlbachler.bswe.model.forecast.Forecast;
import io.muehlbachler.bswe.model.location.Coordinates;
import io.muehlbachler.bswe.service.ForecastService;
import io.muehlbachler.bswe.service.model.forecast.ForecastResult;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * Implementation of the {@link ForecastService} interface.
 */
@AllArgsConstructor
@Service
public class ForecastServiceImpl implements ForecastService {
  private static final Logger LOG = LoggerFactory.getLogger(ForecastServiceImpl.class);
  private static final int NO_OF_DAYS = 1;

  @Autowired
  private final ApiConfiguration apiConfiguration;
  @Autowired
  private final RestTemplate restTemplate;

  @Cacheable(value = "forecasts", key = "#coordinates", unless = "#result == null || #result.requestTime.plusMinutes(#root.target.getCacheTtl()).isBefore(T(java.time.LocalDateTime).now())")
  @CircuitBreaker(name = "getForecast", fallbackMethod = "getForecastFallback")
  @Retry(name = "getForecast", fallbackMethod = "getForecastFallback")
  @Override
  public Forecast fetch(final Coordinates coordinates) throws ApiException {
    if (coordinates == null) {
      throw new ApiException(ApiException.ApiExceptionType.FORECAST_FAILED);
    }

    LOG.info("fetching forecast for coordinates {}", coordinates);

    try {
      final ResponseEntity<ForecastResult> result = restTemplate.exchange(apiConfiguration.getForecast().getUrl(),
          HttpMethod.GET, null,
          ForecastResult.class, coordinates.getLatitude(), coordinates.getLongitude(), NO_OF_DAYS);
      if (result == null || result.getStatusCode() != HttpStatus.OK || !result.hasBody()) {
        throw new ApiException(ApiException.ApiExceptionType.FORECAST_FAILED);
      }

      final ForecastResult results = result.getBody();
      if (results == null) {
        throw new ApiException(ApiException.ApiExceptionType.FORECAST_FAILED);
      }
      LOG.debug("forecast for coordinates {}: {}", coordinates, results);
      final Forecast forecast = results.transformToForecast();
      forecast.setCoordinates(coordinates);
      forecast.setRequestTime(LocalDateTime.now());
      return forecast;
    } catch (RestClientException e) {
      LOG.error("failed to parse forecast response: {}", e.getMessage());
      throw new ApiException(ApiException.ApiExceptionType.FORECAST_FAILED, e);
    }
  }

  /**
   * Returns the time-to-live for the forecast cache.
   *
   * @return the time-to-live for the forecast cache
   */
  public int getCacheTtl() {
    return apiConfiguration.getForecast().getTtl();
  }

  /**
   * Fallback method for the fetch/getForecast method in case of a failure.
   *
   * @param ex the exception that caused the failure
   * @return null
   */
  protected Forecast getForecastFallback(final Exception ex) {
    LOG.error("failed to fetch forecast", ex);
    return null;
  }
}
