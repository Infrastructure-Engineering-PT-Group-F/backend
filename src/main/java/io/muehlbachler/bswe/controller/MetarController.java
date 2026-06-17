package io.muehlbachler.bswe.controller;

import io.muehlbachler.bswe.service.MetarService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tools.jackson.databind.JsonNode;

/**
 * Controller to expose the METAR proxy endpoint.
 */
@AllArgsConstructor
@RestController
@RequestMapping("/api/metar")
@CrossOrigin
public class MetarController {
  @Autowired
  private final MetarService metarService;

  /**
   * Returns the METAR observation for a station.
   *
   * @param icao the ICAO code of the station
   * @return the METAR observation
   */
  @GetMapping("/{icao}")
  public ResponseEntity<JsonNode> get(@PathVariable final String icao) {
    final JsonNode metar = metarService.getMetar(icao);
    if (metar == null || metar.isEmpty()) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    return new ResponseEntity<>(metar, HttpStatus.OK);
  }
}
