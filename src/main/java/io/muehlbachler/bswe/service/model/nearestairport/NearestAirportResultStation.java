package io.muehlbachler.bswe.service.model.nearestairport;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Represents a station in the result of a nearest airport request.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class NearestAirportResultStation {
  private String icao;
  private double longitude;
  private double latitude;
  @JsonProperty("elevation_m")
  private float elevation;
}
