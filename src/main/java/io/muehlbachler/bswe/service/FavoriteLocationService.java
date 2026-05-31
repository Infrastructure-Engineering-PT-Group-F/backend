package io.muehlbachler.bswe.service;

import java.util.List;

import io.muehlbachler.bswe.error.ApiException;
import io.muehlbachler.bswe.model.location.FavoriteLocation;

/**
 * A service to handle all {@link FavoriteLocation} related actions.
 */
public interface FavoriteLocationService {
  /**
   * Enriches a {@link FavoriteLocation}, and saves it to the database.
   *
   * @param favoriteLocation the {@link FavoriteLocation} to save
   * @return the saved {@link FavoriteLocation}
   * @throws ApiException if an API error occurs
   */
  FavoriteLocation save(FavoriteLocation favoriteLocation) throws ApiException;

  /**
   * Lists all favorite locations.
   *
   * @param userId the user id
   * @return the list of favorite locations
   */
  List<FavoriteLocation> list(String userId);

  /**
   * Gets a favorite location by its id.
   *
   * @param userId     the user id
   * @param locationId the location id
   * @return the favorite location
   */
  FavoriteLocation get(String userId, String locationId);

  /**
   * Deletes a favorite location by its id.
   *
   * @param userId     the user id
   * @param locationId the location id
   * @return true if the location was deleted, false otherwise
   */
  boolean delete(String userId, String locationId);
}
