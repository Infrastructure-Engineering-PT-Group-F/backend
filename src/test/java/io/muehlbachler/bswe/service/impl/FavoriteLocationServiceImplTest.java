package io.muehlbachler.bswe.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import io.muehlbachler.bswe.error.ApiException;
import io.muehlbachler.bswe.model.User;
import io.muehlbachler.bswe.model.location.Coordinates;
import io.muehlbachler.bswe.model.location.FavoriteLocation;
import io.muehlbachler.bswe.repository.FavoriteLocationRepository;
import io.muehlbachler.bswe.service.AviationService;
import io.muehlbachler.bswe.service.GeocodingService;
import io.muehlbachler.bswe.service.UserService;
import io.muehlbachler.bswe.service.model.nearestairport.NearestAirportResultStation;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.OptimisticLockingFailureException;

@ExtendWith(MockitoExtension.class)
public class FavoriteLocationServiceImplTest {
  private FavoriteLocationServiceImpl service;

  private FavoriteLocation location;

  @Mock
  private GeocodingService geocodingService;
  @Mock
  private AviationService aviationService;
  @Mock
  private UserService userService;
  @Mock
  private FavoriteLocationRepository favoriteLocationRepository;

  @BeforeEach
  public void setUp() {
    reset(geocodingService, aviationService, userService, favoriteLocationRepository);

    location = new FavoriteLocation();
    location.setId("id");
    location.setGivenLocation("location");
    location.setUser(User.withId("userId"));

    service = new FavoriteLocationServiceImpl(geocodingService, aviationService, userService,
        favoriteLocationRepository);
  }

  @AfterEach
  public void tearDown() {
    verifyNoMoreInteractions(geocodingService, aviationService, userService, favoriteLocationRepository);
  }

  @Test
  public void testSave() throws ApiException {
    Coordinates coordinates = new Coordinates(1.0, 2.0, 3.0f);
    NearestAirportResultStation nearestAirport = new NearestAirportResultStation("airport", 4.0, 5.0, 6.0f);
    FavoriteLocation savedLocation = new FavoriteLocation();
    savedLocation.setId("id");
    savedLocation.setGivenLocation("location");
    savedLocation.setUser(User.withId("userId"));
    savedLocation.setCoordinates(coordinates);
    savedLocation.setNearestAirport(nearestAirport.getIcao());
    savedLocation
        .setNearestAirportCoordinates(new Coordinates(nearestAirport.getLongitude(), nearestAirport.getLatitude(),
            nearestAirport.getElevation()));

    when(userService.exists("userId")).thenReturn(true);
    when(geocodingService.fetchCoordinates("location")).thenReturn(coordinates);
    when(aviationService.getNearestAirport(coordinates)).thenReturn(nearestAirport);
    when(favoriteLocationRepository.save(savedLocation)).thenReturn(savedLocation);

    FavoriteLocation favoriteLocation = service.save(location);

    assertEquals(coordinates, favoriteLocation.getCoordinates());
    assertEquals(nearestAirport.getIcao(), favoriteLocation.getNearestAirport());
    assertEquals(new Coordinates(4.0, 5.0, 6.0f), favoriteLocation.getNearestAirportCoordinates());

    verify(userService, times(1)).exists("userId");
    verify(geocodingService, times(1)).fetchCoordinates("location");
    verify(aviationService, times(1)).getNearestAirport(coordinates);
    verify(favoriteLocationRepository, times(1)).save(favoriteLocation);
  }

  @Test
  public void testSaveNullLocation() {
    FavoriteLocation location = new FavoriteLocation();
    location.setUser(User.withId("userId"));

    when(userService.exists("userId")).thenReturn(true);

    ApiException exception = assertThrows(ApiException.class, () -> service.save(location));
    assertEquals(ApiException.ApiExceptionType.NO_DATA, exception.getType());

    verify(userService, times(1)).exists("userId");
  }

  @Test
  public void testSaveEmptyLocation() {
    FavoriteLocation location = new FavoriteLocation();
    location.setUser(User.withId("userId"));
    location.setGivenLocation("");

    when(userService.exists("userId")).thenReturn(true);

    ApiException exception = assertThrows(ApiException.class, () -> service.save(location));
    assertEquals(ApiException.ApiExceptionType.NO_DATA, exception.getType());

    verify(userService, times(1)).exists("userId");
  }

  @Test
  public void testSaveNull() {
    ApiException exception = assertThrows(ApiException.class, () -> service.save(null));
    assertEquals(ApiException.ApiExceptionType.NO_DATA, exception.getType());
  }

  @Test
  public void testSaveNoUser() {
    ApiException exception = assertThrows(ApiException.class, () -> service.save(new FavoriteLocation()));
    assertEquals(ApiException.ApiExceptionType.NO_DATA, exception.getType());
  }

  @Test
  public void testSaveNullUserId() {
    FavoriteLocation nullUserLocation = new FavoriteLocation();
    nullUserLocation.setUser(new User());
    ApiException exception = assertThrows(ApiException.class, () -> service.save(nullUserLocation));
    assertEquals(ApiException.ApiExceptionType.NO_DATA, exception.getType());
  }

  @Test
  public void testSaveEmptyUserId() {
    FavoriteLocation emptyUserLocation = new FavoriteLocation();
    emptyUserLocation.setUser(User.withId(""));
    ApiException exception = assertThrows(ApiException.class, () -> service.save(emptyUserLocation));
    assertEquals(ApiException.ApiExceptionType.NO_DATA, exception.getType());
  }

  @Test
  public void testSaveInvalidUser() {
    when(userService.exists("userId")).thenReturn(false);

    ApiException exception = assertThrows(ApiException.class, () -> service.save(location));
    assertEquals(ApiException.ApiExceptionType.NO_DATA, exception.getType());

    verify(userService, times(1)).exists("userId");
  }

  @Test
  public void testSaveCoordinatesNull() {
    when(userService.exists("userId")).thenReturn(true);
    when(geocodingService.fetchCoordinates("location")).thenReturn(null);

    ApiException exception = assertThrows(ApiException.class, () -> service.save(location));
    assertEquals(ApiException.ApiExceptionType.GEOCODING_FAILED, exception.getType());

    verify(userService, times(1)).exists("userId");
    verify(geocodingService, times(1)).fetchCoordinates("location");
  }

  @Test
  public void testSaveNearestAirportNull() {
    Coordinates coordinates = new Coordinates(1.0, 2.0, 3.0f);

    when(userService.exists("userId")).thenReturn(true);
    when(geocodingService.fetchCoordinates("location")).thenReturn(coordinates);
    when(aviationService.getNearestAirport(coordinates)).thenReturn(null);

    ApiException exception = assertThrows(ApiException.class, () -> service.save(location));
    assertEquals(ApiException.ApiExceptionType.NO_AIRPORT_FOUND, exception.getType());

    verify(userService, times(1)).exists("userId");
    verify(geocodingService, times(1)).fetchCoordinates("location");
    verify(aviationService, times(1)).getNearestAirport(new Coordinates(1.0, 2.0, 3.0f));
  }

  @Test
  public void testSaveNearestAirportEmptyIcao() {
    Coordinates coordinates = new Coordinates(1.0, 2.0, 3.0f);
    NearestAirportResultStation nearestAirport = new NearestAirportResultStation("", 4.0, 5.0, 6.0f);

    when(userService.exists("userId")).thenReturn(true);
    when(geocodingService.fetchCoordinates("location")).thenReturn(coordinates);
    when(aviationService.getNearestAirport(coordinates)).thenReturn(nearestAirport);

    ApiException exception = assertThrows(ApiException.class, () -> service.save(location));
    assertEquals(ApiException.ApiExceptionType.NO_AIRPORT_FOUND, exception.getType());

    verify(userService, times(1)).exists("userId");
    verify(geocodingService, times(1)).fetchCoordinates("location");
    verify(aviationService, times(1)).getNearestAirport(coordinates);
  }

  @Test
  public void testSaveNearestAirportNullIcao() {
    Coordinates coordinates = new Coordinates(1.0, 2.0, 3.0f);
    NearestAirportResultStation nearestAirport = new NearestAirportResultStation(null, 4.0, 5.0, 6.0f);

    when(userService.exists("userId")).thenReturn(true);
    when(geocodingService.fetchCoordinates("location")).thenReturn(coordinates);
    when(aviationService.getNearestAirport(coordinates)).thenReturn(nearestAirport);

    ApiException exception = assertThrows(ApiException.class, () -> service.save(location));
    assertEquals(ApiException.ApiExceptionType.NO_AIRPORT_FOUND, exception.getType());

    verify(userService, times(1)).exists("userId");
    verify(geocodingService, times(1)).fetchCoordinates("location");
    verify(aviationService, times(1)).getNearestAirport(coordinates);
  }

  @Test
  public void testSaveVerifyAirportCoordinates() throws ApiException {
    Coordinates coordinates = new Coordinates(1.0, 2.0, 3.0f);
    NearestAirportResultStation nearestAirport = new NearestAirportResultStation("LOWW", 16.5, 48.1, 183.0f);

    when(userService.exists("userId")).thenReturn(true);
    when(geocodingService.fetchCoordinates("location")).thenReturn(coordinates);
    when(aviationService.getNearestAirport(coordinates)).thenReturn(nearestAirport);
    when(favoriteLocationRepository.save(location)).thenReturn(location);

    FavoriteLocation result = service.save(location);

    assertEquals("LOWW", result.getNearestAirport());
    assertNotNull(result.getNearestAirportCoordinates());
    assertEquals(16.5, result.getNearestAirportCoordinates().getLongitude());
    assertEquals(48.1, result.getNearestAirportCoordinates().getLatitude());
    assertEquals(183.0f, result.getNearestAirportCoordinates().getElevation());

    verify(userService, times(1)).exists("userId");
    verify(geocodingService, times(1)).fetchCoordinates("location");
    verify(aviationService, times(1)).getNearestAirport(coordinates);
    verify(favoriteLocationRepository, times(1)).save(location);
  }

  @Test
  public void testSaveRepositoryErrorOptimisticLock() {
    Coordinates coordinates = new Coordinates(1.0, 2.0, 3.0f);
    NearestAirportResultStation nearestAirport = new NearestAirportResultStation("airport", 4.0, 5.0, 6.0f);

    when(userService.exists("userId")).thenReturn(true);
    when(geocodingService.fetchCoordinates("location")).thenReturn(coordinates);
    when(aviationService.getNearestAirport(coordinates)).thenReturn(nearestAirport);
    when(favoriteLocationRepository.save(location)).thenThrow(new OptimisticLockingFailureException("error"));

    ApiException exception = assertThrows(ApiException.class, () -> service.save(location));

    assertEquals(ApiException.ApiExceptionType.SAVE_ERROR, exception.getType());

    verify(userService, times(1)).exists("userId");
    verify(geocodingService, times(1)).fetchCoordinates("location");
    verify(aviationService, times(1)).getNearestAirport(coordinates);
    verify(favoriteLocationRepository, times(1)).save(location);
  }

  @Test
  public void testSaveRepositoryErrorIllegalArgument() {
    Coordinates coordinates = new Coordinates(1.0, 2.0, 3.0f);
    NearestAirportResultStation nearestAirport = new NearestAirportResultStation("airport", 4.0, 5.0, 6.0f);

    when(userService.exists("userId")).thenReturn(true);
    when(geocodingService.fetchCoordinates("location")).thenReturn(coordinates);
    when(aviationService.getNearestAirport(coordinates)).thenReturn(nearestAirport);
    when(favoriteLocationRepository.save(location)).thenThrow(new IllegalArgumentException("error"));

    ApiException exception = assertThrows(ApiException.class, () -> service.save(location));

    assertEquals(ApiException.ApiExceptionType.SAVE_ERROR, exception.getType());

    verify(userService, times(1)).exists("userId");
    verify(geocodingService, times(1)).fetchCoordinates("location");
    verify(aviationService, times(1)).getNearestAirport(coordinates);
    verify(favoriteLocationRepository, times(1)).save(location);
  }

  @Test
  public void testList() {
    when(userService.exists("userId")).thenReturn(true);
    when(favoriteLocationRepository.findAllByUserId("userId")).thenReturn(Collections.singletonList(location));

    List<FavoriteLocation> result = service.list("userId");
    assertEquals(1, result.size());
    assertEquals(location, result.get(0));

    verify(userService, times(1)).exists("userId");
    verify(favoriteLocationRepository, times(1)).findAllByUserId("userId");
  }

  @Test
  public void testListNullUserId() {
    assertNull(service.list(null));
  }

  @Test
  public void testListEmptyUserId() {
    assertNull(service.list(""));
  }

  @Test
  public void testListInvalidUser() {
    when(userService.exists("userId")).thenReturn(false);

    assertNull(service.list("userId"));

    verify(userService, times(1)).exists("userId");
  }

  @Test
  public void testListEmpty() {
    when(userService.exists("userId")).thenReturn(true);
    when(favoriteLocationRepository.findAllByUserId("userId")).thenReturn(Collections.emptyList());

    List<FavoriteLocation> result = service.list("userId");
    assertTrue(result.isEmpty());

    verify(userService, times(1)).exists("userId");
    verify(favoriteLocationRepository, times(1)).findAllByUserId("userId");
  }

  @Test
  public void testListNull() {
    when(userService.exists("userId")).thenReturn(true);
    when(favoriteLocationRepository.findAllByUserId("userId")).thenReturn(null);

    List<FavoriteLocation> result = service.list("userId");
    assertNull(result);

    verify(userService, times(1)).exists("userId");
    verify(favoriteLocationRepository, times(1)).findAllByUserId("userId");
  }

  @Test
  public void testGet() {
    when(userService.exists("userId")).thenReturn(true);
    when(favoriteLocationRepository.findByIdAndUserId("id", "userId")).thenReturn(location);

    FavoriteLocation result = service.get("userId", "id");
    assertEquals(location, result);

    verify(userService, times(1)).exists("userId");
    verify(favoriteLocationRepository, times(1)).findByIdAndUserId("id", "userId");
  }

  @Test
  public void testGetNullUserId() {
    assertNull(service.get(null, "location"));
  }

  @Test
  public void testGetNullLocationId() {
    assertNull(service.get("userId", null));
  }

  @Test
  public void testGetEmptyLocationId() {
    assertNull(service.get("userId", ""));
  }

  @Test
  public void testGetEmptyUserId() {
    assertNull(service.get("", "location"));
  }

  @Test
  public void testGetInvalidUser() {
    when(userService.exists("userId")).thenReturn(false);

    assertNull(service.get("userId", "id"));

    verify(userService, times(1)).exists("userId");
  }

  @Test
  public void testGetNull() {
    when(userService.exists("userId")).thenReturn(true);
    when(favoriteLocationRepository.findByIdAndUserId("id", "userId")).thenReturn(null);

    FavoriteLocation result = service.get("userId", "id");
    assertNull(result);

    verify(userService, times(1)).exists("userId");
    verify(favoriteLocationRepository, times(1)).findByIdAndUserId("id", "userId");
  }

  @Test
  public void testDelete() {
    when(userService.exists("userId")).thenReturn(true);

    assertTrue(service.delete("userId", "id"));

    verify(userService, times(1)).exists("userId");
    verify(favoriteLocationRepository, times(1)).deleteByIdAndUserId("id", "userId");
  }

  @Test
  public void testDeleteNullUserId() {
    assertFalse(service.delete(null, "location"));
  }

  @Test
  public void testDeleteNullLocationId() {
    assertFalse(service.delete("userId", null));
  }

  @Test
  public void testDeleteEmptyUserId() {
    assertFalse(service.delete("", "location"));
  }

  @Test
  public void testDeleteEmptyLocationId() {
    assertFalse(service.delete("userId", ""));
  }

  @Test
  public void testDeleteInvalidUser() {
    when(userService.exists("userId")).thenReturn(false);

    assertFalse(service.delete("userId", "id"));

    verify(userService, times(1)).exists("userId");
  }
}
