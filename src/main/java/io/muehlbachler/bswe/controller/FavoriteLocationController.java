package io.muehlbachler.bswe.controller;

import java.util.List;

import io.muehlbachler.bswe.controller.dto.FavoriteLocationCreateDto;
import io.muehlbachler.bswe.controller.dto.FavoriteLocationListDto;
import io.muehlbachler.bswe.error.ApiException;
import io.muehlbachler.bswe.model.User;
import io.muehlbachler.bswe.model.location.FavoriteLocation;
import io.muehlbachler.bswe.service.FavoriteLocationService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller to expose favorite location endpoints.
 */
@AllArgsConstructor
@RestController
@RequestMapping("/api/{userId}/favorite")
@CrossOrigin
public class FavoriteLocationController {
  @Autowired
  private final FavoriteLocationService favoriteLocationService;

  /**
   * Creates a new favorite location.
   *
   * @param userId   the user id
   * @param location the favorite location to create
   * @return the created favorite location
   */
  @PostMapping("/")
  public ResponseEntity<FavoriteLocation> create(@PathVariable final String userId,
      @RequestBody final FavoriteLocationCreateDto location) {
    if (location == null) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    final FavoriteLocation favoriteLocation = new FavoriteLocation();
    favoriteLocation.setGivenName(location.getName());
    favoriteLocation.setGivenLocation(location.getLocation());
    favoriteLocation.setUser(User.withId(userId));

    try {
      final FavoriteLocation createdFavoriteLocation = favoriteLocationService.save(favoriteLocation);
      if (createdFavoriteLocation == null) {
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
      }

      return new ResponseEntity<>(createdFavoriteLocation, HttpStatus.CREATED);
    } catch (final ApiException e) {
      return new ResponseEntity<>(e.getHttpStatus());
    }
  }

  /**
   * Lists all favorite locations.
   *
   * @param userId the user id
   * @return the list of favorite locations
   */
  @GetMapping("/")
  public ResponseEntity<FavoriteLocationListDto> list(@PathVariable final String userId) {
    final List<FavoriteLocation> favoriteLocations = favoriteLocationService.list(userId);
    if (favoriteLocations == null) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    return new ResponseEntity<>(new FavoriteLocationListDto(favoriteLocations), HttpStatus.OK);
  }

  /**
   * Gets a favorite location by id.
   *
   * @param userId the user id
   * @param id     the favorite location id
   * @return the favorite location
   */
  @GetMapping("/{id}")
  public ResponseEntity<FavoriteLocation> get(@PathVariable final String userId, @PathVariable final String id) {
    final FavoriteLocation favoriteLocation = favoriteLocationService.get(userId, id);
    if (favoriteLocation == null) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    return new ResponseEntity<>(favoriteLocation, HttpStatus.OK);
  }

  /**
   * Deletes a favorite location by id.
   *
   * @param userId the user id
   * @param id     the favorite location id
   * @return the response entity
   */
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable final String userId, @PathVariable final String id) {
    return new ResponseEntity<>(
        favoriteLocationService.delete(userId, id) ? HttpStatus.NO_CONTENT : HttpStatus.NOT_FOUND);
  }
}
