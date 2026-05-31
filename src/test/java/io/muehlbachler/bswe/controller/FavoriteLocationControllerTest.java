package io.muehlbachler.bswe.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

import java.util.Collections;
import java.util.List;

import io.muehlbachler.bswe.controller.dto.FavoriteLocationCreateDto;
import io.muehlbachler.bswe.controller.dto.FavoriteLocationListDto;
import io.muehlbachler.bswe.error.ApiException;
import io.muehlbachler.bswe.model.User;
import io.muehlbachler.bswe.model.location.Coordinates;
import io.muehlbachler.bswe.model.location.FavoriteLocation;
import io.muehlbachler.bswe.service.FavoriteLocationService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
public class FavoriteLocationControllerTest {
  private FavoriteLocationController controller;

  private FavoriteLocation location;

  @Mock
  private FavoriteLocationService favoriteLocationService;

  @BeforeEach
  public void setUp() {
    location = new FavoriteLocation();
    location.setId("id");
    location.setGivenLocation("location");
    location.setUser(User.withId("userId"));

    controller = new FavoriteLocationController(favoriteLocationService);

    reset(favoriteLocationService);
  }

  @AfterEach
  public void tearDown() {
    verifyNoMoreInteractions(favoriteLocationService);
  }

  @Test
  public void testList() {
    List<FavoriteLocation> locations = Collections.singletonList(new FavoriteLocation());
    when(favoriteLocationService.list("userId")).thenReturn(locations);

    ResponseEntity<FavoriteLocationListDto> response = controller.list("userId");
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(locations, response.getBody().getLocations());

    verify(favoriteLocationService, times(1)).list("userId");
  }

  @Test
  public void testListEmpty() {
    when(favoriteLocationService.list("userId")).thenReturn(Collections.emptyList());

    ResponseEntity<FavoriteLocationListDto> result = controller.list("userId");
    assertEquals(HttpStatus.OK, result.getStatusCode());

    FavoriteLocationListDto results = result.getBody();
    assertNotNull(results);
    assertTrue(results.getLocations().isEmpty());

    verify(favoriteLocationService, times(1)).list("userId");
  }

  @Test
  public void testListNull() {
    when(favoriteLocationService.list("userId")).thenReturn(null);

    ResponseEntity<FavoriteLocationListDto> result = controller.list("userId");
    assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());

    verify(favoriteLocationService, times(1)).list("userId");
  }

  @Test
  public void testGet() {
    FavoriteLocation location = new FavoriteLocation();
    when(favoriteLocationService.get("userId", "locationId")).thenReturn(location);

    ResponseEntity<FavoriteLocation> response = controller.get("userId", "locationId");
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(location, response.getBody());

    verify(favoriteLocationService, times(1)).get("userId", "locationId");
  }

  @Test
  public void testGetNull() {
    when(favoriteLocationService.get("userId", "id")).thenReturn(null);

    ResponseEntity<FavoriteLocation> result = controller.get("userId", "id");
    assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());

    verify(favoriteLocationService, times(1)).get("userId", "id");
  }

  @Test
  public void testDelete() {
    when(favoriteLocationService.delete("userId", "locationId")).thenReturn(true);

    ResponseEntity<Void> response = controller.delete("userId", "locationId");
    assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

    verify(favoriteLocationService, times(1)).delete("userId", "locationId");
  }

  @Test
  public void testDeleteFails() {
    when(favoriteLocationService.delete("userId", "id")).thenReturn(false);

    ResponseEntity<Void> result = controller.delete("userId", "id");
    assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());

    verify(favoriteLocationService, times(1)).delete("userId", "id");
  }

  @Test
  public void testCreate() throws ApiException {
    FavoriteLocationCreateDto createDto = new FavoriteLocationCreateDto();
    createDto.setName("name");
    createDto.setLocation("location");

    FavoriteLocation location = new FavoriteLocation();
    when(favoriteLocationService.save(any())).thenReturn(location);

    ResponseEntity<FavoriteLocation> response = controller.create("userId", createDto);
    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    assertEquals(location, response.getBody());

    verify(favoriteLocationService, times(1)).save(any());
  }

  @Test
  public void testCreateNull() throws ApiException {
    ResponseEntity<FavoriteLocation> result = controller.create("userId", null);
    assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
  }

  @Test
  public void testCreateNullSave() throws ApiException {
    FavoriteLocationCreateDto createDto = new FavoriteLocationCreateDto();
    createDto.setName("name");
    createDto.setLocation("location");

    when(favoriteLocationService.save(new FavoriteLocation())).thenReturn(null);

    ResponseEntity<FavoriteLocation> result = controller.create("userId", createDto);
    assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    assertNull(result.getBody());

    verify(favoriteLocationService, times(1)).save(new FavoriteLocation());
  }

  @Test
  public void testCreateFailed() throws ApiException {
    FavoriteLocationCreateDto createDto = new FavoriteLocationCreateDto();
    createDto.setName("name");
    createDto.setLocation("location");

    when(favoriteLocationService.save(new FavoriteLocation()))
        .thenThrow(new ApiException(ApiException.ApiExceptionType.SAVE_ERROR));

    ResponseEntity<FavoriteLocation> result = controller.create("userId", createDto);
    assertEquals(HttpStatus.CONFLICT, result.getStatusCode());

    verify(favoriteLocationService, times(1)).save(new FavoriteLocation());
  }

  @Test
  public void testCreateWithCoordinates() throws ApiException {
    FavoriteLocationCreateDto createDto = new FavoriteLocationCreateDto();
    createDto.setName("name");
    createDto.setLocation("location");

    FavoriteLocation location = new FavoriteLocation();
    location.setGivenName("name");
    location.setGivenLocation("location");
    location.setCoordinates(new Coordinates(1.0, 2.0, 3.0f));
    location.setNearestAirport("LOWW");
    location.setNearestAirportCoordinates(new Coordinates(16.5, 48.1, 183.0f));

    when(favoriteLocationService.save(any())).thenReturn(location);

    ResponseEntity<FavoriteLocation> response = controller.create("userId", createDto);
    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    assertEquals(location, response.getBody());
    assertEquals(new Coordinates(16.5, 48.1, 183.0f), response.getBody().getNearestAirportCoordinates());

    verify(favoriteLocationService, times(1)).save(any());
  }

  @Test
  public void testListWithCoordinates() {
    FavoriteLocation location = new FavoriteLocation();
    location.setCoordinates(new Coordinates(1.0, 2.0, 3.0f));
    location.setNearestAirport("LOWW");
    location.setNearestAirportCoordinates(new Coordinates(16.5, 48.1, 183.0f));

    List<FavoriteLocation> locations = Collections.singletonList(location);
    when(favoriteLocationService.list("userId")).thenReturn(locations);

    ResponseEntity<FavoriteLocationListDto> response = controller.list("userId");
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(locations, response.getBody().getLocations());
    assertEquals(new Coordinates(16.5, 48.1, 183.0f),
        response.getBody().getLocations().get(0).getNearestAirportCoordinates());

    verify(favoriteLocationService, times(1)).list("userId");
  }
}
