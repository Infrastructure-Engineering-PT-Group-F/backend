package io.muehlbachler.bswe.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.UUID;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.google.gson.Gson;
import io.muehlbachler.bswe.configuration.ApiConfiguration;
import io.muehlbachler.bswe.controller.dto.FavoriteLocationCreateDto;
import io.muehlbachler.bswe.controller.dto.FavoriteLocationListDto;
import io.muehlbachler.bswe.model.User;
import io.muehlbachler.bswe.model.location.Coordinates;
import io.muehlbachler.bswe.model.location.FavoriteLocation;
import io.muehlbachler.bswe.repository.FavoriteLocationRepository;
import io.muehlbachler.bswe.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.wiremock.spring.ConfigureWireMock;
import org.wiremock.spring.EnableWireMock;
import org.wiremock.spring.InjectWireMock;

@EnableWireMock({
    @ConfigureWireMock(name = "aviation-service", baseUrlProperties = "aviation-service.url", portProperties = "aviation-service.port", filesUnderDirectory = "wiremock/aviation-service"),
    @ConfigureWireMock(name = "geocoding-service", baseUrlProperties = "geocoding-service.url", portProperties = "geocoding-service.port", filesUnderDirectory = "wiremock/geocoding-service")
})
@SpringBootTest
@AutoConfigureMockMvc
class FavoriteLocationControllerIntegrationTest {
  @InjectWireMock("aviation-service")
  private WireMockServer aviationMockServer;
  @InjectWireMock("geocoding-service")
  private WireMockServer geocodingMockServer;

  @Autowired
  private MockMvc mvc;
  @Autowired
  private FavoriteLocationRepository repository;
  @Autowired
  private UserRepository userRepository;

  @MockitoBean
  private ApiConfiguration apiConfiguration;

  private User user;
  private FavoriteLocation location;
  private FavoriteLocationCreateDto locationDto;

  @BeforeEach
  void setUp() {
    repository.deleteAll();
    userRepository.deleteAll();

    user = new User();
    user.setUsername(UUID.randomUUID().toString());
    user = userRepository.save(user);

    location = new FavoriteLocation();
    location.setUser(user);
    location.setGivenName(UUID.randomUUID().toString());
    location.setGivenLocation("Vienna, Austria");
    location.setCoordinates(new Coordinates(1.0, 2.0, 3.0f));
    location.setNearestAirport("LOWW");
    location.setNearestAirportCoordinates(new Coordinates(16.5, 48.1, 183.0f));

    locationDto = new FavoriteLocationCreateDto();
    locationDto.setName(location.getGivenName());
    locationDto.setLocation(location.getGivenLocation());

    when(apiConfiguration.getGeocoding())
        .thenReturn(
            new ApiConfiguration.ApiConnectionInformation(geocodingMockServer.baseUrl() + "/{name}", 900, null));
    when(apiConfiguration.getNearestAirport())
        .thenReturn(
            new ApiConfiguration.ApiConnectionInformation(aviationMockServer.baseUrl() + "/{lon},{lat}", 900,
                new ApiConfiguration.ApiConnectionAuthorization("Authorization", "Token token")));
  }

  @AfterEach
  void tearDown() {
    repository.deleteAll();
    userRepository.deleteAll();

    verifyNoMoreInteractions(apiConfiguration);
  }

  @Test
  void testList() throws Exception {
    location = repository.save(location);

    MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/api/{userId}/favorite/", user.getId()))
        .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
    FavoriteLocationListDto list = new Gson().fromJson(result.getResponse().getContentAsString(),
        FavoriteLocationListDto.class);
    assertNotNull(list);
    assertEquals(1, list.getLocations().size());
    FavoriteLocation resultLocation = list.getLocations().get(0);
    assertEquals(location.getGivenName(), resultLocation.getGivenName());
    assertEquals(location.getGivenLocation(), resultLocation.getGivenLocation());
    assertEquals(location.getCoordinates(), resultLocation.getCoordinates());
    assertEquals(location.getId(), resultLocation.getId());
    assertEquals(location.getNearestAirport(), resultLocation.getNearestAirport());
    assertEquals(location.getNearestAirportCoordinates(), resultLocation.getNearestAirportCoordinates());
    assertNull(resultLocation.getUser());
  }

  @Test
  void testListInvalidUser() throws Exception {
    mvc.perform(MockMvcRequestBuilders.get("/api/{userId}/favorite/", UUID.randomUUID().toString()))
        .andExpect(MockMvcResultMatchers.status().isNotFound());
  }

  @Test
  void testListEmpty() throws Exception {
    MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/api/{userId}/favorite/",
        user.getId()))
        .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
    FavoriteLocationListDto list = new Gson().fromJson(result.getResponse().getContentAsString(),
        FavoriteLocationListDto.class);
    assertNotNull(list);
    assertTrue(list.getLocations().isEmpty());
  }

  @Test
  void testGet() throws Exception {
    location = repository.save(location);

    MvcResult result = mvc
        .perform(MockMvcRequestBuilders.get("/api/{userId}/favorite/{locationId}", user.getId(), location.getId()))
        .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
    FavoriteLocation favoriteLocation = new Gson().fromJson(result.getResponse().getContentAsString(),
        FavoriteLocation.class);
    assertNotNull(favoriteLocation);
    assertEquals(location.getGivenName(), favoriteLocation.getGivenName());
    assertEquals(location.getGivenLocation(), favoriteLocation.getGivenLocation());
    assertEquals(location.getCoordinates(), favoriteLocation.getCoordinates());
    assertEquals(location.getId(), favoriteLocation.getId());
    assertEquals(location.getNearestAirport(), favoriteLocation.getNearestAirport());
    assertEquals(location.getNearestAirportCoordinates(), favoriteLocation.getNearestAirportCoordinates());
  }

  @Test
  void testGetInvalidUser() throws Exception {
    mvc.perform(MockMvcRequestBuilders.get("/api/{userId}/favorite/{locationId}", UUID.randomUUID().toString(),
        location.getId()))
        .andExpect(MockMvcResultMatchers.status().isNotFound());
  }

  @Test
  void testGetInvalidLocation() throws Exception {
    mvc.perform(MockMvcRequestBuilders.get("/api/{userId}/favorite/{locationId}", user.getId(), UUID.randomUUID()))
        .andExpect(MockMvcResultMatchers.status().isNotFound());
  }

  @Transactional
  @Test
  void testDelete() throws Exception {
    location = repository.save(location);

    mvc.perform(MockMvcRequestBuilders.delete("/api/{userId}/favorite/{locationId}", user.getId(), location.getId()))
        .andExpect(MockMvcResultMatchers.status().isNoContent());

    assertFalse(repository.existsById(location.getId()));
  }

  @Test
  void testDeleteInvalidUser() throws Exception {
    location = repository.save(location);

    mvc.perform(MockMvcRequestBuilders.delete("/api/{userId}/favorite/{locationId}", UUID.randomUUID().toString(),
        location.getId()))
        .andExpect(MockMvcResultMatchers.status().isNotFound());

    assertTrue(repository.existsById(location.getId()));
  }

  @Test
  void testDeleteInvalidLocation() throws Exception {
    mvc.perform(MockMvcRequestBuilders.delete("/api/{userId}/favorite/{locationId}", user.getId(), UUID.randomUUID()))
        .andExpect(MockMvcResultMatchers.status().isNoContent());
  }

  @Test
  void testCreate() throws Exception {
    MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/api/{userId}/favorite/", user.getId())
        .contentType(MediaType.APPLICATION_JSON)
        .content(new Gson().toJson(locationDto)))
        .andExpect(MockMvcResultMatchers.status().isCreated()).andReturn();
    FavoriteLocation favoriteLocation = new Gson().fromJson(result.getResponse().getContentAsString(),
        FavoriteLocation.class);
    assertNotNull(favoriteLocation);
    assertFalse(favoriteLocation.getId().isEmpty());
    assertEquals(locationDto.getName(), favoriteLocation.getGivenName());
    assertEquals(locationDto.getLocation(), favoriteLocation.getGivenLocation());
    assertEquals(11.0, favoriteLocation.getCoordinates().getLongitude());
    assertEquals(22.0, favoriteLocation.getCoordinates().getLatitude());
    assertEquals(33.0f, favoriteLocation.getCoordinates().getElevation());
    assertEquals("LOWL", favoriteLocation.getNearestAirport());
    assertNotNull(favoriteLocation.getNearestAirportCoordinates());
    assertEquals(14.193611, favoriteLocation.getNearestAirportCoordinates().getLongitude());
    assertEquals(48.233056, favoriteLocation.getNearestAirportCoordinates().getLatitude());
    assertEquals(978.0f, favoriteLocation.getNearestAirportCoordinates().getElevation());
    assertTrue(repository.existsById(favoriteLocation.getId()));

    verify(apiConfiguration, times(1)).getGeocoding();
    verify(apiConfiguration, times(3)).getNearestAirport();

    geocodingMockServer.verify(1,
        WireMock.getRequestedFor(WireMock.urlEqualTo("/Vienna,%20Austria")));
    aviationMockServer.verify(1,
        WireMock.getRequestedFor(WireMock.urlEqualTo("/22.0,11.0")));
  }

  @Test
  void testCreateInvalidLocation() throws Exception {
    locationDto.setLocation("invalid");

    mvc.perform(MockMvcRequestBuilders.post("/api/{userId}/favorite/", user.getId())
        .contentType(MediaType.APPLICATION_JSON)
        .content(new Gson().toJson(locationDto)))
        .andExpect(MockMvcResultMatchers.status().isBadRequest());

    verify(apiConfiguration, times(1)).getGeocoding();

    geocodingMockServer.verify(1,
        WireMock.getRequestedFor(WireMock.urlEqualTo("/invalid")));
  }

  @Test
  void testCreateInvalidUser() throws Exception {
    mvc.perform(MockMvcRequestBuilders.post("/api/{userId}/favorite/", UUID.randomUUID().toString())
        .contentType(MediaType.APPLICATION_JSON)
        .content(new Gson().toJson(locationDto)))
        .andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  @Test
  void testCreateNullLocation() throws Exception {
    locationDto.setLocation(null);

    mvc.perform(
        MockMvcRequestBuilders.post("/api/{userId}/favorite/", user.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(new Gson().toJson(locationDto)))
        .andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  @Test
  void testCreateEmptyLocation() throws Exception {
    locationDto.setLocation("");

    mvc.perform(MockMvcRequestBuilders.post("/api/{userId}/favorite/", user.getId())
        .contentType(MediaType.APPLICATION_JSON)
        .content(new Gson().toJson(locationDto)))
        .andExpect(MockMvcResultMatchers.status().isBadRequest());
  }
}
