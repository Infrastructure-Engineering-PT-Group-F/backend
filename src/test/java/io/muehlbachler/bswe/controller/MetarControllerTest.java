package io.muehlbachler.bswe.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import io.muehlbachler.bswe.service.MetarService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;

@ExtendWith(MockitoExtension.class)
public class MetarControllerTest {
  private static final JsonMapper MAPPER = JsonMapper.builder().build();

  private MetarController controller;

  @Mock
  private MetarService metarService;

  @BeforeEach
  public void setUp() {
    controller = new MetarController(metarService);

    reset(metarService);
  }

  @AfterEach
  public void tearDown() {
    verifyNoMoreInteractions(metarService);
  }

  @Test
  public void testGet() {
    final JsonNode metar = MAPPER.readTree("{\"station\":\"LOWW\"}");
    when(metarService.getMetar("LOWW")).thenReturn(metar);

    final ResponseEntity<JsonNode> result = controller.get("LOWW");
    assertEquals(HttpStatus.OK, result.getStatusCode());
    assertEquals(metar, result.getBody());

    verify(metarService, times(1)).getMetar("LOWW");
  }

  @Test
  public void testGetNotFound() {
    when(metarService.getMetar("ZZZZ")).thenReturn(null);

    final ResponseEntity<JsonNode> result = controller.get("ZZZZ");
    assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());

    verify(metarService, times(1)).getMetar("ZZZZ");
  }
}
