package io.muehlbachler.bswe.model.location;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.muehlbachler.bswe.model.User;
import org.junit.jupiter.api.Test;

public class FavoriteLocationTest {
  @Test
  void testDefaultConstructor() {
    FavoriteLocation location = new FavoriteLocation();

    assertNull(location.getId());
    assertNull(location.getGivenName());
    assertNull(location.getGivenLocation());
    assertNull(location.getCoordinates());
    assertNull(location.getNearestAirport());
    assertNull(location.getNearestAirportCoordinates());
    assertNull(location.getUser());
  }

  @Test
  void testParameterizedConstructor() {
    String id = "test-id";
    String givenName = "Home";
    String givenLocation = "Vienna";
    Coordinates coordinates = new Coordinates(15.5, 48.2, 300.5f);
    String nearestAirport = "VIE";
    Coordinates airportCoordinates = new Coordinates(16.5, 48.1, 183.0f);
    User user = User.withId("user-id");

    FavoriteLocation location = new FavoriteLocation(id, givenName, givenLocation,
        coordinates, nearestAirport, airportCoordinates, user);

    assertEquals(id, location.getId());
    assertEquals(givenName, location.getGivenName());
    assertEquals(givenLocation, location.getGivenLocation());
    assertEquals(coordinates, location.getCoordinates());
    assertEquals(nearestAirport, location.getNearestAirport());
    assertEquals(airportCoordinates, location.getNearestAirportCoordinates());
    assertEquals(user, location.getUser());
  }

  @Test
  void testSettersAndGetters() {
    FavoriteLocation location = new FavoriteLocation();

    String id = "test-id";
    String givenName = "Home";
    String givenLocation = "Vienna";
    Coordinates coordinates = new Coordinates(15.5, 48.2, 300.5f);
    String nearestAirport = "VIE";
    Coordinates airportCoordinates = new Coordinates(16.5, 48.1, 183.0f);
    User user = User.withId("user-id");

    location.setId(id);
    location.setGivenName(givenName);
    location.setGivenLocation(givenLocation);
    location.setCoordinates(coordinates);
    location.setNearestAirport(nearestAirport);
    location.setNearestAirportCoordinates(airportCoordinates);
    location.setUser(user);

    assertEquals(id, location.getId());
    assertEquals(givenName, location.getGivenName());
    assertEquals(givenLocation, location.getGivenLocation());
    assertEquals(coordinates, location.getCoordinates());
    assertEquals(nearestAirport, location.getNearestAirport());
    assertEquals(airportCoordinates, location.getNearestAirportCoordinates());
    assertEquals(user, location.getUser());
  }

  @Test
  void testEqualsAndHashCode() {
    FavoriteLocation location1 = new FavoriteLocation("id1", "name1", "loc1", null, null, null, null);
    FavoriteLocation location2 = new FavoriteLocation("id1", "name2", "loc2", null, null, null, null);
    FavoriteLocation location3 = new FavoriteLocation("id2", "name1", "loc1", null, null, null, null);

    assertEquals(location1, location2);
    assertNotEquals(location1, location3);
    assertEquals(location1.hashCode(), location2.hashCode());
    assertNotEquals(location1.hashCode(), location3.hashCode());
  }

  @Test
  void testToString() {
    FavoriteLocation location = new FavoriteLocation("test-id", "Home", "Vienna",
        new Coordinates(15.5, 48.2, 300.5f), "VIE", new Coordinates(16.5, 48.1, 183.0f), User.withId("user-id"));

    String toString = location.toString();

    assertTrue(toString.contains("test-id"));
    assertTrue(toString.contains("Home"));
    assertTrue(toString.contains("Vienna"));
    assertTrue(toString.contains("VIE"));
  }
}
