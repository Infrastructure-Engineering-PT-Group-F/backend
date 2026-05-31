package io.muehlbachler.bswe.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import io.muehlbachler.bswe.configuration.ApiConfiguration;
import io.muehlbachler.bswe.configuration.impl.ApiConfigurationProperties;
import io.muehlbachler.bswe.model.location.Coordinates;
import io.muehlbachler.bswe.service.model.nearestairport.NearestAirportResultStation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.wiremock.spring.ConfigureWireMock;
import org.wiremock.spring.EnableWireMock;
import org.wiremock.spring.InjectWireMock;

@SpringBootTest
@EnableWireMock(@ConfigureWireMock(name = "aviation-service", baseUrlProperties = "aviation-service.url", portProperties = "aviation-service.port", filesUnderDirectory = "wiremock/aviation-service"))
@ExtendWith(MockitoExtension.class)
public class AviationServiceImplTest {
  @InjectWireMock("aviation-service")
  private WireMockServer aviationMockServer;

  private AviationServiceImpl service;

  private ApiConfiguration apiConfiguration;

  @Autowired
  private RestTemplate restTemplate;

  @BeforeEach
  public void setUp() {
    apiConfiguration = new ApiConfigurationProperties(
        new ApiConfiguration.ApiConnectionInformation("http://geocoding.url", 900,
            null),
        new ApiConfiguration.ApiConnectionInformation("http://forecast.url", 900,
            null),
        new ApiConfiguration.ApiConnectionInformation("http://metar.url", 900,
            null),
        new ApiConfiguration.ApiConnectionInformation(aviationMockServer.baseUrl() + "/{lon},{lat}", 900,
            new ApiConfiguration.ApiConnectionAuthorization("Authorization", "Token token")));

    service = new AviationServiceImpl(apiConfiguration, restTemplate);
  }

  @Test
  public void testGetNearestAirport() {
    NearestAirportResultStation result = service.getNearestAirport(new Coordinates(11.0, 22.0, 3.0f));
    assertEquals("LOWL", result.getIcao());
    assertEquals(14.193611, result.getLongitude());
    assertEquals(48.233056, result.getLatitude());
    assertEquals(978f, result.getElevation());

    aviationMockServer.verify(1,
        WireMock.getRequestedFor(WireMock.urlEqualTo("/22.0,11.0")));
  }

  @Test
  public void testGetNearestAirportNull() {
    NearestAirportResultStation result = service.getNearestAirport(null);
    assertNull(result);
  }

  @Test
  public void testGetNearestAirportInvalid() {
    NearestAirportResultStation result = service.getNearestAirport(new Coordinates(-1.0, -2.0, 3.0f));
    assertNull(result);

    aviationMockServer.verify(1,
        WireMock.getRequestedFor(WireMock.urlEqualTo("/-2.0,-1.0")));
  }

  @Test
  public void testGetNearestAirportEmpty() {
    NearestAirportResultStation result = service.getNearestAirport(new Coordinates(0.0, 0.0, 3.0f));
    assertNull(result);

    aviationMockServer.verify(1,
        WireMock.getRequestedFor(WireMock.urlEqualTo("/0.0,0.0")));
  }

  @Test
  public void testGetNearestAirportNoResults() {
    NearestAirportResultStation result = service.getNearestAirport(new Coordinates(2.0, 2.0, 3.0f));
    assertNull(result);

    aviationMockServer.verify(1,
        WireMock.getRequestedFor(WireMock.urlEqualTo("/2.0,2.0")));
  }

  @Test
  public void testGetNearestAirportUpstreamError() {
    // the circuit breaker is not initialized
    NearestAirportResultStation result = service.getNearestAirport(new Coordinates(3.0, 3.0, 3.0f));
    assertNull(result);

    aviationMockServer.verify(1,
        WireMock.getRequestedFor(WireMock.urlEqualTo("/3.0,3.0")));
  }

  @Test
  public void testGetNearestAriportFallback() {
    NearestAirportResultStation result = service.getNearestAirportFallback(new RuntimeException());
    assertNull(result);
  }

  @Test
  public void testGetNearestAirportInvalidFormat() {
    NearestAirportResultStation result = service.getNearestAirport(new Coordinates(5.0, 5.0, 3.0f));
    assertNull(result);

    aviationMockServer.verify(1,
        WireMock.getRequestedFor(WireMock.urlEqualTo("/5.0,5.0")));
  }

  @Test
  public void testGetNearestAirportEmptyArray() {
    NearestAirportResultStation result = service.getNearestAirport(new Coordinates(6.0, 6.0, 3.0f));
    assertNull(result);

    aviationMockServer.verify(1,
        WireMock.getRequestedFor(WireMock.urlEqualTo("/6.0,6.0")));
  }
}
