package io.muehlbachler.bswe.service.impl;

import java.util.regex.Pattern;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.muehlbachler.bswe.configuration.ApiConfiguration;
import io.muehlbachler.bswe.service.MetarService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import tools.jackson.databind.JsonNode;

/**
 * Implementation of the {@link MetarService} interface.
 */
@AllArgsConstructor
@Service
public class MetarServiceImpl implements MetarService {
  private static final Logger LOG = LoggerFactory.getLogger(MetarServiceImpl.class);
  private static final Pattern ICAO_PATTERN = Pattern.compile("[A-Za-z0-9]{4}");

  @Autowired
  private final ApiConfiguration apiConfiguration;
  @Autowired
  private final RestTemplate restTemplate;

  @CircuitBreaker(name = "getMetar", fallbackMethod = "getMetarFallback")
  @Retry(name = "getMetar", fallbackMethod = "getMetarFallback")
  @Override
  public JsonNode getMetar(final String icao) {
    if (icao == null || !ICAO_PATTERN.matcher(icao).matches()) {
      return null;
    }

    LOG.info("fetching METAR for station {}", icao);

    try {
      final HttpHeaders headers = new HttpHeaders();
      headers.add(apiConfiguration.getMetar().getAuthorization().getHeader(),
          apiConfiguration.getMetar().getAuthorization().getValue());
      final HttpEntity<String> request = new HttpEntity<>(headers);
      final ResponseEntity<JsonNode> result = restTemplate.exchange(
          apiConfiguration.getMetar().getUrl(),
          HttpMethod.GET, request, JsonNode.class, icao);
      if (result == null || result.getStatusCode() != HttpStatus.OK || !result.hasBody()) {
        return null;
      }
      return result.getBody();
    } catch (RestClientException e) {
      LOG.error("failed to fetch METAR: {}", e.getMessage());
      return null;
    }
  }

  /**
   * Fallback method for the getMetar method in case of a failure.
   *
   * @param ex the exception that caused the failure
   * @return null
   */
  protected JsonNode getMetarFallback(final Exception ex) {
    LOG.error("failed to fetch METAR", ex);
    return null;
  }
}
