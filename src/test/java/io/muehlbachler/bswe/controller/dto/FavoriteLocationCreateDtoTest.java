package io.muehlbachler.bswe.controller.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

public class FavoriteLocationCreateDtoTest {
  @Test
  void testNoArgsConstructor() {
    final FavoriteLocationCreateDto dto = new FavoriteLocationCreateDto();
    assertNotNull(dto);
  }

  @Test
  void testAllArgsConstructor() {
    final FavoriteLocationCreateDto dto = new FavoriteLocationCreateDto("name", "location");
    assertNotNull(dto);
    assertEquals("name", dto.getName());
    assertEquals("location", dto.getLocation());
  }

  @Test
  void testSetters() {
    final FavoriteLocationCreateDto dto = new FavoriteLocationCreateDto();
    dto.setName("name");
    dto.setLocation("location");

    assertEquals("name", dto.getName());
    assertEquals("location", dto.getLocation());
  }
}
