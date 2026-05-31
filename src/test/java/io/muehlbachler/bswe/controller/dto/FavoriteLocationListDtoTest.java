package io.muehlbachler.bswe.controller.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import io.muehlbachler.bswe.model.location.FavoriteLocation;
import org.junit.jupiter.api.Test;

class FavoriteLocationListDtoTest {
  @Test
  void testNoArgsConstructor() {
    final FavoriteLocationListDto dto = new FavoriteLocationListDto();
    assertNotNull(dto);
  }

  @Test
  void testAllArgsConstructor() {
    final List<FavoriteLocation> locations = new ArrayList<>();
    final FavoriteLocationListDto dto = new FavoriteLocationListDto(locations);
    assertNotNull(dto);
    assertEquals(locations, dto.getLocations());
  }

  @Test
  void testSetters() {
    final List<FavoriteLocation> locations = new ArrayList<>();
    final FavoriteLocationListDto dto = new FavoriteLocationListDto();
    dto.setLocations(locations);

    assertEquals(locations, dto.getLocations());
  }
}
