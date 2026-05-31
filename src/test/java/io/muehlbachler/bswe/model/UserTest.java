package io.muehlbachler.bswe.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import io.muehlbachler.bswe.model.location.FavoriteLocation;
import org.junit.jupiter.api.Test;

class UserTest {
  @Test
  void testDefaultConstructor() {
    User user = new User();
    assertNull(user.getId());
    assertNull(user.getUsername());
    assertNull(user.getFavoriteLocations());
  }

  @Test
  void testParameterizedConstructor() {
    String id = "test-id";
    String username = "testuser";
    List<FavoriteLocation> favoriteLocations = new ArrayList<FavoriteLocation>();

    User user = new User(id, username, favoriteLocations);

    assertEquals(id, user.getId());
    assertEquals(username, user.getUsername());
    assertEquals(favoriteLocations, user.getFavoriteLocations());
  }

  @Test
  void testWithId() {
    String id = "test-id";
    User user = User.withId(id);

    assertEquals(id, user.getId());
    assertNull(user.getUsername());
    assertNull(user.getFavoriteLocations());
  }

  @Test
  void testEqualsAndHashCode() {
    User user1 = new User("id1", "user1", null);
    User user2 = new User("id1", "user2", null);
    User user3 = new User("id2", "user1", null);

    assertEquals(user1, user2);
    assertNotEquals(user1, user3);
    assertEquals(user1.hashCode(), user2.hashCode());
    assertNotEquals(user1.hashCode(), user3.hashCode());
  }

  @Test
  void testToString() {
    User user = new User("test-id", "testuser", new ArrayList<>());
    String toString = user.toString();

    assertTrue(toString.contains("test-id"));
    assertTrue(toString.contains("testuser"));
  }
}
