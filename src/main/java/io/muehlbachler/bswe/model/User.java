package io.muehlbachler.bswe.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.muehlbachler.bswe.model.location.FavoriteLocation;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Identifies a user.
 * The user's id is a unique identifier represented by a UUID.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString
@Entity
@Table(name = "users")
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;
  @Column(unique = true, nullable = false)
  private String username;
  @JsonIgnore
  @OneToMany(mappedBy = "user", cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
  private List<FavoriteLocation> favoriteLocations;

  /**
   * Creates a new user with the given id.
   *
   * @param id the user id
   * @return the user with the given id
   */
  public static User withId(final String id) {
    final User user = new User();
    user.setId(id);
    return user;
  }
}
