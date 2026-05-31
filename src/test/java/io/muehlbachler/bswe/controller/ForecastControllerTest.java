package io.muehlbachler.bswe.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;

import io.muehlbachler.bswe.error.ApiException;
import io.muehlbachler.bswe.model.forecast.Forecast;
import io.muehlbachler.bswe.model.location.Coordinates;
import io.muehlbachler.bswe.model.location.FavoriteLocation;
import io.muehlbachler.bswe.service.FavoriteLocationService;
import io.muehlbachler.bswe.service.ForecastService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
public class ForecastControllerTest {
  private ForecastController controller;

  @Mock
  private FavoriteLocationService favoriteLocationService;
  @Mock
  private ForecastService forecastService;

  @BeforeEach
  public void setUp() {
    controller = new ForecastController(favoriteLocationService, forecastService);

    reset(favoriteLocationService, forecastService);
  }

  @AfterEach
  public void tearDown() {
    verifyNoMoreInteractions(favoriteLocationService, forecastService);
  }

  @Test
  public void testGet() throws ApiException {
    Coordinates coordinates = new Coordinates(1.0, 2.0, 3.0f);
    FavoriteLocation location = new FavoriteLocation();
    location.setId("locationId");
    location.setCoordinates(coordinates);
    Forecast forecast = new Forecast();
    forecast.setCoordinates(coordinates);
    forecast.setRequestTime(LocalDateTime.now());
    when(favoriteLocationService.get("userId", "locationId")).thenReturn(location);
    when(forecastService.fetch(coordinates)).thenReturn(forecast);

    ResponseEntity<Forecast> result = controller.get("userId", "locationId");
    assertEquals(HttpStatus.OK, result.getStatusCode());

    assertEquals(forecast, result.getBody());

    verify(favoriteLocationService, times(1)).get("userId", "locationId");
    verify(forecastService, times(1)).fetch(coordinates);
  }

  @Test
  public void testGetForecastError() throws ApiException {
    Coordinates coordinates = new Coordinates(1.0, 2.0, 3.0f);
    FavoriteLocation location = new FavoriteLocation();
    location.setId("locationId");
    location.setCoordinates(coordinates);
    when(favoriteLocationService.get("userId", "locationId")).thenReturn(location);
    when(forecastService.fetch(coordinates)).thenThrow(new ApiException(ApiException.ApiExceptionType.FORECAST_FAILED));

    ResponseEntity<Forecast> result = controller.get("userId", "locationId");
    assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());

    verify(favoriteLocationService, times(1)).get("userId", "locationId");
    verify(forecastService, times(1)).fetch(coordinates);
  }

  @Test
  public void testGetForecastNull() throws ApiException {
    Coordinates coordinates = new Coordinates(1.0, 2.0, 3.0f);
    FavoriteLocation location = new FavoriteLocation();
    location.setId("locationId");
    location.setCoordinates(coordinates);
    when(favoriteLocationService.get("userId", "locationId")).thenReturn(location);
    when(forecastService.fetch(coordinates)).thenReturn(null);

    ResponseEntity<Forecast> result = controller.get("userId", "locationId");
    assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());

    verify(favoriteLocationService, times(1)).get("userId", "locationId");
    verify(forecastService, times(1)).fetch(coordinates);
  }

  @Test
  public void testGetForecastNoLocation() throws ApiException {
    when(favoriteLocationService.get("userId", "locationId")).thenReturn(null);

    ResponseEntity<Forecast> result = controller.get("userId", "locationId");
    assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());

    verify(favoriteLocationService, times(1)).get("userId", "locationId");
  }
}
