package io.muehlbachler.bswe.error;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

public class ApiExceptionTest {
  @Test
  void testTypeConstructor() {
    ApiException exception = new ApiException(ApiException.ApiExceptionType.NO_DATA);
    assertNotNull(exception);
    assertEquals(ApiException.ApiExceptionType.NO_DATA, exception.getType());
    assertNull(exception.getCause());
  }

  @Test
  void testTypeAndParentConstructor() {
    Exception parent = new RuntimeException("test");
    ApiException exception = new ApiException(ApiException.ApiExceptionType.NO_DATA, parent);
    assertNotNull(exception);
    assertEquals(ApiException.ApiExceptionType.NO_DATA, exception.getType());
    assertEquals(parent, exception.getCause());
  }

  @Test
  void testGetHttpStatusNullType() {
    ApiException exception = new ApiException(null);
    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getHttpStatus());
  }

  @Test
  void testGetHttpStatusNoData() {
    ApiException exception = new ApiException(ApiException.ApiExceptionType.NO_DATA);
    assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
  }

  @Test
  void testGetHttpStatusSaveError() {
    ApiException exception = new ApiException(ApiException.ApiExceptionType.SAVE_ERROR);
    assertEquals(HttpStatus.CONFLICT, exception.getHttpStatus());
  }

  @Test
  void testGetHttpStatusGeocodingFailed() {
    ApiException exception = new ApiException(ApiException.ApiExceptionType.GEOCODING_FAILED);
    assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
  }

  @Test
  void testGetHttpStatusNoAirportFound() {
    ApiException exception = new ApiException(ApiException.ApiExceptionType.NO_AIRPORT_FOUND);
    assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
  }

  @Test
  void testGetHttpStatusForecastFailed() {
    ApiException exception = new ApiException(ApiException.ApiExceptionType.FORECAST_FAILED);
    assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
  }

  @Test
  void testGetHttpStatusInternalServerError() {
    ApiException exception = new ApiException(ApiException.ApiExceptionType.INTERNAL_SERVER_ERROR);
    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getHttpStatus());
  }
}
