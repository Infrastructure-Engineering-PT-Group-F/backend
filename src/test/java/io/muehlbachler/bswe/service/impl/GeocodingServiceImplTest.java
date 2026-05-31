package io.muehlbachler.bswe.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import io.muehlbachler.bswe.configuration.ApiConfiguration;
import io.muehlbachler.bswe.configuration.impl.ApiConfigurationProperties;
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
@EnableWireMock(@ConfigureWireMock(name = "geocoding-service", baseUrlProperties = "geocoding-service.url", portProperties = "geocoding-service.port", filesUnderDirectory = "wiremock/geocoding-service"))
@ExtendWith(MockitoExtension.class)
public class GeocodingServiceImplTest {
  @InjectWireMock("geocoding-service")
  private WireMockServer geocodingMockServer;

  private GeocodingServiceImpl service;

  private ApiConfiguration apiConfiguration;

  @Autowired
  private RestTemplate restTemplate;

  @BeforeEach
  public void setUp() {
    apiConfiguration = new ApiConfigurationProperties(
        new ApiConfiguration.ApiConnectionInformation(geocodingMockServer.baseUrl() + "/{name}", 900,
            null),
        new ApiConfiguration.ApiConnectionInformation("http://forecast.url", 900,
            null),
        new ApiConfiguration.ApiConnectionInformation("http://metar.url", 900,
            null),
        new ApiConfiguration.ApiConnectionInformation("http://nearestAirport.url", 900, null));

    service = new GeocodingServiceImpl(apiConfiguration, restTemplate);
  }

  @Test
  public void testFetchCoordinates() {
    Coordinates coordinates = new Coordinates(1.0, 2.0, 3.0f);

    Coordinates result = service.fetchCoordinates("location");
    assertEquals(coordinates, result);

    geocodingMockServer.verify(1,
        WireMock.getRequestedFor(WireMock.urlEqualTo("/location")));
  }

  @Test
  public void testFetchCoordinatesNull() {
    Coordinates result = service.fetchCoordinates(null);
    assertNull(result);
  }

  @Test
  public void testFetchCoordinatesEmptyLocation() {
    Coordinates result = service.fetchCoordinates("");
    assertNull(result);
  }

  @Test
  public void testFetchCoordinatesInvalid() {
    Coordinates result = service.fetchCoordinates("invalid");
    assertNull(result);

    geocodingMockServer.verify(1,
        WireMock.getRequestedFor(WireMock.urlEqualTo("/invalid")));
  }

  @Test
  public void testFetchCoordinatesNoResults() {
    Coordinates result = service.fetchCoordinates("no-result");
    assertNull(result);

    geocodingMockServer.verify(1,
        WireMock.getRequestedFor(WireMock.urlEqualTo("/no-result")));
  }

  @Test
  public void testFetchCoordinatesUpstreamError() {
    // the circuit breaker is not initialized
    Coordinates result = service.fetchCoordinates("unauthenticated");
    assertNull(result);

    geocodingMockServer.verify(1,
        WireMock.getRequestedFor(WireMock.urlEqualTo("/unauthenticated")));
  }

  @Test
  public void testFetchCoordinatesFallback() {
    assertNull(service.fetchCoordinatesFallback(new RuntimeException()));
  }

  @Test
  public void testFetchCoordinatesServerError() {
    Coordinates result = service.fetchCoordinates("server-error");
    assertNull(result);

    geocodingMockServer.verify(1,
        WireMock.getRequestedFor(WireMock.urlEqualTo("/server-error")));
  }

  @Test
  public void testFetchCoordinatesInvalidFormat() {
    Coordinates result = service.fetchCoordinates("invalid-format");
    assertNull(result);

    geocodingMockServer.verify(1,
        WireMock.getRequestedFor(WireMock.urlEqualTo("/invalid-format")));
  }

  @Test
  public void testFetchCoordinatesEmptyResponse() {
    Coordinates result = service.fetchCoordinates("empty-response");
    assertNull(result);

    geocodingMockServer.verify(1,
        WireMock.getRequestedFor(WireMock.urlEqualTo("/empty-response")));
  }
}
