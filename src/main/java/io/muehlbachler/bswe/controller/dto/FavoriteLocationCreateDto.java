package io.muehlbachler.bswe.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO to create a new favorite location.
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FavoriteLocationCreateDto {
  private String name;
  private String location;
}
