package io.muehlbachler.bswe.repository;

import java.util.List;

import io.muehlbachler.bswe.model.location.FavoriteLocation;
import org.springframework.data.repository.CrudRepository;

/**
 * CRUD Repository for the {@link FavoriteLocation} entity.
 */
public interface FavoriteLocationRepository extends CrudRepository<FavoriteLocation, String> {
  /**
   * Finds all favorite locations by user id.
   *
   * @param userId the user id
   * @return the list of favorite locations
   */
  List<FavoriteLocation> findAllByUserId(String userId);

  /**
   * Finds a favorite location by id and user id.
   *
   * @param id     the location id
   * @param userId the user id
   * @return the favorite location
   */
  FavoriteLocation findByIdAndUserId(String id, String userId);

  /**
   * Deletes a favorite location by id and user id.
   *
   * @param id     the location id
   * @param userId the user id
   */
  void deleteByIdAndUserId(String id, String userId);
}
