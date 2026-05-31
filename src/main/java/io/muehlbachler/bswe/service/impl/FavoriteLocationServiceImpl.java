package io.muehlbachler.bswe.service.impl;

import java.util.List;

import io.muehlbachler.bswe.error.ApiException;
import io.muehlbachler.bswe.model.location.Coordinates;
import io.muehlbachler.bswe.model.location.FavoriteLocation;
import io.muehlbachler.bswe.repository.FavoriteLocationRepository;
import io.muehlbachler.bswe.service.AviationService;
import io.muehlbachler.bswe.service.FavoriteLocationService;
import io.muehlbachler.bswe.service.GeocodingService;
import io.muehlbachler.bswe.service.UserService;
import io.muehlbachler.bswe.service.model.nearestairport.NearestAirportResultStation;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of the {@link FavoriteLocationService} interface.
 */
@AllArgsConstructor
@Service
@Transactional
public class FavoriteLocationServiceImpl implements FavoriteLocationService {
  private static final Logger LOG = LoggerFactory.getLogger(FavoriteLocationServiceImpl.class);

  @Autowired
  private final GeocodingService geocodingService;
  @Autowired
  private final AviationService aviationService;
  @Autowired
  private final UserService userService;
  @Autowired
  private final FavoriteLocationRepository locationRepository;

  @Override
  public FavoriteLocation save(final FavoriteLocation favoriteLocation) throws ApiException {
    if (isLocationInValid(favoriteLocation)) {
      throw new ApiException(ApiException.ApiExceptionType.NO_DATA);
    }

    final Coordinates coordinates = geocodingService.fetchCoordinates(favoriteLocation.getGivenLocation());
    if (coordinates == null) {
      throw new ApiException(ApiException.ApiExceptionType.GEOCODING_FAILED);
    }
    favoriteLocation.setCoordinates(coordinates);

    final NearestAirportResultStation nearestAirport = aviationService.getNearestAirport(coordinates);
    if (nearestAirport == null || nearestAirport.getIcao() == null || nearestAirport.getIcao().isEmpty()) {
      throw new ApiException(ApiException.ApiExceptionType.NO_AIRPORT_FOUND);
    }
    favoriteLocation.setNearestAirport(nearestAirport.getIcao());
    favoriteLocation
        .setNearestAirportCoordinates(new Coordinates(nearestAirport.getLongitude(), nearestAirport.getLatitude(),
            nearestAirport.getElevation()));

    try {
      return locationRepository.save(favoriteLocation);
    } catch (OptimisticLockingFailureException | IllegalArgumentException e) {
      LOG.error("failed to save favorite location", e);
      throw new ApiException(ApiException.ApiExceptionType.SAVE_ERROR, e);
    }
  }

  @Override
  public List<FavoriteLocation> list(final String userId) {
    if (userId == null || userId.isEmpty() || !userService.exists(userId)) {
      return null;
    }

    return locationRepository.findAllByUserId(userId);
  }

  @Override
  public FavoriteLocation get(final String userId, final String locationId) {
    if (userId == null || locationId == null || userId.isEmpty() || locationId.isEmpty()
        || !userService.exists(userId)) {
      return null;
    }

    return locationRepository.findByIdAndUserId(locationId, userId);
  }

  @Override
  public boolean delete(final String userId, final String locationId) {
    if (userId == null || locationId == null || userId.isEmpty() || locationId.isEmpty()
        || !userService.exists(userId)) {
      return false;
    }

    locationRepository.deleteByIdAndUserId(locationId, userId);
    return true;
  }

  /**
   * Checks if the given favorite location is invalid.
   *
   * @param favoriteLocation the favorite location to check
   * @return true if the favorite location is invalid, false otherwise
   */
  private boolean isLocationInValid(final FavoriteLocation favoriteLocation) {
    return favoriteLocation == null || favoriteLocation.getUser() == null || favoriteLocation.getUser().getId() == null
        || favoriteLocation.getUser().getId().isEmpty() || !userService.exists(favoriteLocation.getUser().getId())
        || favoriteLocation.getGivenLocation() == null || favoriteLocation.getGivenLocation().isEmpty();
  }
}
