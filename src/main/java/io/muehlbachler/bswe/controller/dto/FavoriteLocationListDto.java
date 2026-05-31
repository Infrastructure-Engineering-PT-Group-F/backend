package io.muehlbachler.bswe.controller.dto;

import java.util.List;

import io.muehlbachler.bswe.model.location.FavoriteLocation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO to list favorite locations.
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FavoriteLocationListDto {
  private List<FavoriteLocation> locations;
}
