package io.muehlbachler.bswe.model.location;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.muehlbachler.bswe.model.User;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Identifies a user's favorite location.
 * The location's id is a unique identifier represented by a UUID.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString
@Entity
@Table(name = "favorite_locations")
public class FavoriteLocation {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;
  private String givenName;
  private String givenLocation;
  @Embedded
  private Coordinates coordinates;
  private String nearestAirport;
  @AttributeOverrides({
      @AttributeOverride(name = "longitude", column = @Column(name = "nearest_airport_longitude")),
      @AttributeOverride(name = "latitude", column = @Column(name = "nearest_airport_latitude")),
      @AttributeOverride(name = "elevation", column = @Column(name = "nearest_airport_elevation"))
  })
  @Embedded
  private Coordinates nearestAirportCoordinates;
  @JsonIgnore
  @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;
}
