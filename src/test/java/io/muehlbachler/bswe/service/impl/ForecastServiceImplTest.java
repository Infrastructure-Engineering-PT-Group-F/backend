package io.muehlbachler.bswe.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import io.muehlbachler.bswe.configuration.ApiConfiguration;
import io.muehlbachler.bswe.configuration.impl.ApiConfigurationProperties;
import io.muehlbachler.bswe.error.ApiException;
import io.muehlbachler.bswe.model.forecast.CurrentWeather;
import io.muehlbachler.bswe.model.forecast.Forecast;
import io.muehlbachler.bswe.model.forecast.HourlyForecast;
import io.muehlbachler.bswe.model.forecast.Units;
import io.muehlbachler.bswe.model.location.Coordinates;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;
import org.wiremock.spring.ConfigureWireMock;
import org.wiremock.spring.EnableWireMock;
import org.wiremock.spring.InjectWireMock;

@SpringBootTest
@EnableWireMock(@ConfigureWireMock(name = "forecast-service", baseUrlProperties = "forecast-service.url", portProperties = "forecast-service.port", filesUnderDirectory = "wiremock/forecast-service"))
@ExtendWith(MockitoExtension.class)
public class ForecastServiceImplTest {
  @InjectWireMock("forecast-service")
  private WireMockServer forecastMockServer;

  private ForecastServiceImpl service;

  private ApiConfiguration apiConfiguration;

  @Autowired
  private RestTemplate restTemplate;

  @BeforeEach
  public void setUp() {
    apiConfiguration = new ApiConfigurationProperties(
        new ApiConfiguration.ApiConnectionInformation("http://geocoding.url", 900,
            null),
        new ApiConfiguration.ApiConnectionInformation(
            forecastMockServer.baseUrl() + "/{lon}/{lat}/{days}", 900,
            null),
        new ApiConfiguration.ApiConnectionInformation("http://metar.url", 900,
            null),
        new ApiConfiguration.ApiConnectionInformation("http://nearestAirport.url", 900, null));

    service = new ForecastServiceImpl(apiConfiguration, restTemplate);
  }

  @Test
  public void testFetch() throws ApiException {
    Coordinates coordinates = new Coordinates(1.0, 2.0, 3.0f);
    Forecast forecast = new Forecast();
    forecast.setCoordinates(coordinates);
    forecast.setUnits(new Units("°C", "°C", "%", "°C", "%", "mm", "%", "m", "km/h", "°", "km/h"));
    forecast.setCurrentWeather(new CurrentWeather(5.1f, 1.5f, 81, -0.7f, 0.1f, 12.2f, 43, 26.3f));
    HourlyForecast hourlyForecast1 = new HourlyForecast(LocalDateTime.parse("2025-02-06T00:00"), 3.6f, 0.7f, 94, 2.7f,
        10, 0.1f, 100, 3540, 8.3f, 288, 18.4f);
    HourlyForecast hourlyForecast23 = new HourlyForecast(LocalDateTime.parse("2025-02-06T23:00"), 3.1f, -0.3f, 90, 1.6f,
        0, 0.0f, 100, 6980, 10.5f, 52, 25.6f);

    Forecast result = service.fetch(coordinates);
    assertNotNull(result);
    forecast.setRequestTime(result.getRequestTime());
    assertEquals(forecast, result);
    assertEquals(forecast.getUnits(), result.getUnits());
    assertEquals(forecast.getCurrentWeather(), result.getCurrentWeather());
    assertEquals(24, result.getHourlyForecast().size());
    assertEquals(hourlyForecast1, result.getHourlyForecast().values().toArray()[0]);
    assertNotEquals(hourlyForecast1, result.getHourlyForecast().values().toArray()[23]);
    assertEquals(hourlyForecast23, result.getHourlyForecast().values().toArray()[23]);
    assertNotEquals(hourlyForecast23, result.getHourlyForecast().values().toArray()[0]);

    forecastMockServer.verify(1,
        WireMock.getRequestedFor(WireMock.urlEqualTo("/2.0/1.0/1")));
  }

  @Test
  public void testFetchNoUnits() throws ApiException {
    Coordinates coordinates = new Coordinates(1.0, 3.0, 3.0f);
    Forecast forecast = new Forecast();
    forecast.setCoordinates(coordinates);
    forecast.setCurrentWeather(new CurrentWeather(5.1f, 1.5f, 81, -0.7f, 0.1f, 12.2f, 43, 26.3f));
    HourlyForecast hourlyForecast1 = new HourlyForecast(LocalDateTime.parse("2025-02-06T00:00"), 3.6f, 0.7f, 94, 2.7f,
        10, 0.1f, 100, 3540, 8.3f, 288, 18.4f);
    HourlyForecast hourlyForecast23 = new HourlyForecast(LocalDateTime.parse("2025-02-06T23:00"), 3.1f, -0.3f, 90, 1.6f,
        0, 0.0f, 100, 6980, 10.5f, 52, 25.6f);

    Forecast result = service.fetch(coordinates);
    assertNotNull(result);
    forecast.setRequestTime(result.getRequestTime());
    assertEquals(forecast, result);
    assertEquals(forecast.getUnits(), result.getUnits());
    assertEquals(forecast.getCurrentWeather(), result.getCurrentWeather());
    assertEquals(24, result.getHourlyForecast().size());
    assertEquals(hourlyForecast1, result.getHourlyForecast().values().toArray()[0]);
    assertNotEquals(hourlyForecast1, result.getHourlyForecast().values().toArray()[23]);
    assertEquals(hourlyForecast23, result.getHourlyForecast().values().toArray()[23]);
    assertNotEquals(hourlyForecast23, result.getHourlyForecast().values().toArray()[0]);

    forecastMockServer.verify(1,
        WireMock.getRequestedFor(WireMock.urlEqualTo("/3.0/1.0/1")));
  }

  @Test
  public void testFetchNoCurrent() throws ApiException {
    Coordinates coordinates = new Coordinates(1.0, 4.0, 3.0f);
    Forecast forecast = new Forecast();
    forecast.setCoordinates(coordinates);
    forecast.setUnits(new Units("°C", "°C", "%", "°C", "%", "mm", "%", "m", "km/h", "°", "km/h"));
    HourlyForecast hourlyForecast1 = new HourlyForecast(LocalDateTime.parse("2025-02-06T00:00"), 3.6f, 0.7f, 94, 2.7f,
        10, 0.1f, 100, 3540, 8.3f, 288, 18.4f);
    HourlyForecast hourlyForecast23 = new HourlyForecast(LocalDateTime.parse("2025-02-06T23:00"), 3.1f, -0.3f, 90, 1.6f,
        0, 0.0f, 100, 6980, 10.5f, 52, 25.6f);

    Forecast result = service.fetch(coordinates);
    assertNotNull(result);
    forecast.setRequestTime(result.getRequestTime());
    assertEquals(forecast, result);
    assertEquals(forecast.getUnits(), result.getUnits());
    assertEquals(forecast.getCurrentWeather(), result.getCurrentWeather());
    assertEquals(24, result.getHourlyForecast().size());
    assertEquals(hourlyForecast1, result.getHourlyForecast().values().toArray()[0]);
    assertNotEquals(hourlyForecast1, result.getHourlyForecast().values().toArray()[23]);
    assertEquals(hourlyForecast23, result.getHourlyForecast().values().toArray()[23]);
    assertNotEquals(hourlyForecast23, result.getHourlyForecast().values().toArray()[0]);

    forecastMockServer.verify(1,
        WireMock.getRequestedFor(WireMock.urlEqualTo("/4.0/1.0/1")));
  }

  @Test
  public void testFetchNoHourly() throws ApiException {
    Coordinates coordinates = new Coordinates(1.0, 5.0, 3.0f);
    Forecast forecast = new Forecast();
    forecast.setCoordinates(coordinates);
    forecast.setUnits(new Units("°C", "°C", "%", "°C", "%", "mm", "%", "m", "km/h", "°", "km/h"));
    forecast.setCurrentWeather(new CurrentWeather(5.1f, 1.5f, 81, -0.7f, 0.1f, 12.2f, 43, 26.3f));

    Forecast result = service.fetch(coordinates);
    assertNotNull(result);
    forecast.setRequestTime(result.getRequestTime());
    assertEquals(forecast, result);
    assertEquals(forecast.getUnits(), result.getUnits());
    assertEquals(forecast.getCurrentWeather(), result.getCurrentWeather());
    assertTrue(result.getHourlyForecast().isEmpty());

    forecastMockServer.verify(1,
        WireMock.getRequestedFor(WireMock.urlEqualTo("/5.0/1.0/1")));
  }

  @Test
  public void testFetchMissingProperties() throws ApiException {
    Coordinates coordinates = new Coordinates(1.0, 6.0, 3.0f);
    Forecast forecast = new Forecast();
    forecast.setCoordinates(coordinates);
    forecast.setUnits(new Units("°C", "°C", "%", "°C", "%", "mm", "%", "m", "km/h", "°", "km/h"));
    forecast.setCurrentWeather(new CurrentWeather(5.1f, 1.5f, 81, -0.7f, 0.1f, 12.2f, 43, 26.3f));

    Forecast result = service.fetch(coordinates);
    assertNotNull(result);
    forecast.setRequestTime(result.getRequestTime());
    assertEquals(forecast, result);
    assertEquals(forecast.getUnits(), result.getUnits());
    assertEquals(forecast.getCurrentWeather(), result.getCurrentWeather());
    assertTrue(result.getHourlyForecast().isEmpty());

    forecastMockServer.verify(1,
        WireMock.getRequestedFor(WireMock.urlEqualTo("/6.0/1.0/1")));
  }

  @Test
  public void testFetchInvalidLengths() throws ApiException {
    Coordinates coordinates = new Coordinates(1.0, 7.0, 3.0f);
    Forecast forecast = new Forecast();
    forecast.setCoordinates(coordinates);
    forecast.setUnits(new Units("°C", "°C", "%", "°C", "%", "mm", "%", "m", "km/h", "°", "km/h"));
    forecast.setCurrentWeather(new CurrentWeather(5.1f, 1.5f, 81, -0.7f, 0.1f, 12.2f, 43, 26.3f));

    Forecast result = service.fetch(coordinates);
    assertNotNull(result);
    forecast.setRequestTime(result.getRequestTime());
    assertEquals(forecast, result);
    assertEquals(forecast.getUnits(), result.getUnits());
    assertEquals(forecast.getCurrentWeather(), result.getCurrentWeather());
    assertTrue(result.getHourlyForecast().isEmpty());

    forecastMockServer.verify(1,
        WireMock.getRequestedFor(WireMock.urlEqualTo("/7.0/1.0/1")));
  }

  @Test
  public void testFetchCoordinatesNull() {
    ApiException exception = assertThrows(ApiException.class, () -> service.fetch(null));
    assertEquals(ApiException.ApiExceptionType.FORECAST_FAILED, exception.getType());
  }

  @Test
  public void testFetchCoordinatesNoResult() throws ApiException {
    Coordinates coordinates = new Coordinates(2.0, 2.0, 3.0f);
    Forecast forecast = new Forecast();
    forecast.setCoordinates(coordinates);

    Forecast result = service.fetch(coordinates);
    assertNotNull(result);
    forecast.setRequestTime(result.getRequestTime());
    assertEquals(forecast, result);

    forecastMockServer.verify(1,
        WireMock.getRequestedFor(WireMock.urlEqualTo("/2.0/2.0/1")));
  }

  @Test
  public void testFetchEmpty() {
    ApiException exception = assertThrows(ApiException.class, () -> service.fetch(new Coordinates(3.0, 2.0, 3.0f)));
    assertEquals(ApiException.ApiExceptionType.FORECAST_FAILED, exception.getType());

    forecastMockServer.verify(1,
        WireMock.getRequestedFor(WireMock.urlEqualTo("/2.0/3.0/1")));
  }

  @Test
  public void testFetchUpstreamError() {
    ApiException exception = assertThrows(ApiException.class, () -> service.fetch(new Coordinates(4.0, 2.0, 3.0f)));
    assertEquals(ApiException.ApiExceptionType.FORECAST_FAILED, exception.getType());

    forecastMockServer.verify(1,
        WireMock.getRequestedFor(WireMock.urlEqualTo("/2.0/4.0/1")));
  }

  @Test
  public void getForecastFallback() {
    Forecast result = service.getForecastFallback(new RuntimeException());
    assertNull(result);
  }

  @Test
  public void getCacheTtl() {
    assertEquals(900, service.getCacheTtl());
  }
}
