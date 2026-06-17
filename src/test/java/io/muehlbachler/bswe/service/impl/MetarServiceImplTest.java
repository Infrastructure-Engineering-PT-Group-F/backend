package io.muehlbachler.bswe.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import io.muehlbachler.bswe.configuration.ApiConfiguration;
import io.muehlbachler.bswe.configuration.ApiConfiguration.ApiConnectionAuthorization;
import io.muehlbachler.bswe.configuration.ApiConfiguration.ApiConnectionInformation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;

@ExtendWith(MockitoExtension.class)
public class MetarServiceImplTest {
  private static final JsonMapper MAPPER = JsonMapper.builder().build();
  private static final String URL = "http://metar.url/{icao}";

  @Mock
  private ApiConfiguration apiConfiguration;
  @Mock
  private RestTemplate restTemplate;

  private MetarServiceImpl service;

  @BeforeEach
  public void setUp() {
    service = new MetarServiceImpl(apiConfiguration, restTemplate);
  }

  private void stubConfig() {
    when(apiConfiguration.getMetar()).thenReturn(new ApiConnectionInformation(
        URL, 900, new ApiConnectionAuthorization("Authorization", "Token token")));
  }

  @Test
  public void testGetMetar() {
    stubConfig();
    final JsonNode body = MAPPER.readTree("{\"station\":\"LOWL\",\"flight_rules\":\"VFR\"}");
    when(restTemplate.exchange(eq(URL), eq(HttpMethod.GET), any(HttpEntity.class),
        eq(JsonNode.class), eq("LOWL")))
        .thenReturn(new ResponseEntity<>(body, HttpStatus.OK));

    final JsonNode result = service.getMetar("LOWL");
    assertEquals(body, result);

    verify(restTemplate).exchange(eq(URL), eq(HttpMethod.GET), any(HttpEntity.class),
        eq(JsonNode.class), eq("LOWL"));
  }

  @Test
  public void testGetMetarNull() {
    final JsonNode result = service.getMetar(null);
    assertNull(result);
    verifyNoInteractions(restTemplate);
  }

  @Test
  public void testGetMetarInvalidFormat() {
    final JsonNode result = service.getMetar("TOOLONG");
    assertNull(result);
    verifyNoInteractions(restTemplate);
  }

  @Test
  public void testGetMetarUpstreamError() {
    stubConfig();
    when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class),
        eq(JsonNode.class), anyString()))
        .thenThrow(new RestClientException("boom"));

    final JsonNode result = service.getMetar("LOWW");
    assertNull(result);
  }

  @Test
  public void testGetMetarNoBody() {
    stubConfig();
    when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class),
        eq(JsonNode.class), anyString()))
        .thenReturn(new ResponseEntity<>(HttpStatus.OK));

    final JsonNode result = service.getMetar("EDDF");
    assertNull(result);
  }

  @Test
  public void testGetMetarFallback() {
    final JsonNode result = service.getMetarFallback(new RuntimeException());
    assertNull(result);
  }
}
