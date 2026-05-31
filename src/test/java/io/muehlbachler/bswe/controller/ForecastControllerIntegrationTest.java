package io.muehlbachler.bswe.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import io.muehlbachler.bswe.configuration.ApiConfiguration;
import io.muehlbachler.bswe.model.User;
import io.muehlbachler.bswe.model.forecast.CurrentWeather;
import io.muehlbachler.bswe.model.forecast.Forecast;
import io.muehlbachler.bswe.model.forecast.HourlyForecast;
import io.muehlbachler.bswe.model.forecast.Units;
import io.muehlbachler.bswe.model.location.Coordinates;
import io.muehlbachler.bswe.model.location.FavoriteLocation;
import io.muehlbachler.bswe.repository.FavoriteLocationRepository;
import io.muehlbachler.bswe.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.wiremock.spring.ConfigureWireMock;
import org.wiremock.spring.EnableWireMock;
import org.wiremock.spring.InjectWireMock;

@EnableWireMock({
    @ConfigureWireMock(name = "forecast-service", baseUrlProperties = "forecast-service.url", portProperties = "forecast-service.port", filesUnderDirectory = "wiremock/forecast-service")
})
@SpringBootTest
@AutoConfigureMockMvc
class ForecastControllerIntegrationTest {
  @InjectWireMock("forecast-service")
  private WireMockServer forecastMockServer;

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

  private Gson gson = new GsonBuilder()
      .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
      .create();

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
    location.setNearestAirportCoordinates(new Coordinates(11.0, 22.0, 33.0f));
    location = repository.save(location);

    when(apiConfiguration.getForecast())
        .thenReturn(new ApiConfiguration.ApiConnectionInformation(
            forecastMockServer.baseUrl() + "/{lon}/{lat}/{days}", 900,
            null));
  }

  @AfterEach
  void tearDown() {
    repository.deleteAll();
    userRepository.deleteAll();
  }

  @Test
  void testGetInvalidLocation() throws Exception {
    mvc.perform(
        MockMvcRequestBuilders.get("/api/{userId}/{locationId}/forecast/", user.getId(), UUID.randomUUID().toString()))
        .andExpect(MockMvcResultMatchers.status().isNotFound());
  }

  @Test
  void testGetInvalidUser() throws Exception {
    mvc.perform(
        MockMvcRequestBuilders.get("/api/{userId}/{locationId}/forecast/", UUID.randomUUID().toString(),
            location.getId()))
        .andExpect(MockMvcResultMatchers.status().isNotFound());
  }

  @Test
  void testGet() throws Exception {
    Coordinates coordinates = new Coordinates(1.0, 2.0, 3.0f);
    Forecast forecast = new Forecast();
    forecast.setCoordinates(coordinates);
    forecast.setUnits(new Units("°C", "°C", "%", "°C", "%", "mm", "%", "m", "km/h", "°", "km/h"));
    forecast.setCurrentWeather(new CurrentWeather(5.1f, 1.5f, 81, -0.7f, 0.1f, 12.2f, 43, 26.3f));
    HourlyForecast hourlyForecast1 = new HourlyForecast(LocalDateTime.parse("2025-02-06T00:00"), 3.6f, 0.7f, 94, 2.7f,
        10, 0.1f, 100, 3540, 8.3f, 288, 18.4f);
    HourlyForecast hourlyForecast23 = new HourlyForecast(LocalDateTime.parse("2025-02-06T23:00"), 3.1f, -0.3f, 90, 1.6f,
        0, 0.0f, 100, 6980, 10.5f, 52, 25.6f);

    MvcResult result = mvc
        .perform(MockMvcRequestBuilders.get("/api/{userId}/{locationdId}/forecast/", user.getId(), location.getId()))
        .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
    Forecast forecastResult = gson.fromJson(result.getResponse().getContentAsString(),
        Forecast.class);
    assertNotNull(forecastResult);
    forecast.setRequestTime(forecastResult.getRequestTime());
    assertEquals(forecast, forecastResult);
    assertEquals(forecast.getUnits(), forecastResult.getUnits());
    assertEquals(forecast.getCurrentWeather(), forecastResult.getCurrentWeather());
    assertEquals(24, forecastResult.getHourlyForecast().size());
    assertEquals(hourlyForecast1, forecastResult.getHourlyForecast().values().toArray()[0]);
    assertNotEquals(hourlyForecast1, forecastResult.getHourlyForecast().values().toArray()[23]);
    assertEquals(hourlyForecast23, forecastResult.getHourlyForecast().values().toArray()[23]);
    assertNotEquals(hourlyForecast23, forecastResult.getHourlyForecast().values().toArray()[0]);

    verify(apiConfiguration, times(2)).getForecast();

    forecastMockServer.verify(1,
        WireMock.getRequestedFor(WireMock.urlEqualTo("/2.0/1.0/1")));
  }

  @Test
  void testGetNoForecast() throws Exception {
    Coordinates coordinates = new Coordinates(2.0, 2.0, 3.0f);
    location.setCoordinates(coordinates);
    repository.save(location);

    Forecast forecast = new Forecast();
    forecast.setCoordinates(coordinates);

    MvcResult result = mvc
        .perform(MockMvcRequestBuilders.get("/api/{userId}/{locationdId}/forecast/", user.getId(), location.getId()))
        .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
    Forecast forecastResult = gson.fromJson(result.getResponse().getContentAsString(),
        Forecast.class);
    assertNotNull(forecastResult);
    forecast.setRequestTime(forecastResult.getRequestTime());
    assertEquals(forecast, forecastResult);

    verify(apiConfiguration, times(2)).getForecast();

    forecastMockServer.verify(1,
        WireMock.getRequestedFor(WireMock.urlEqualTo("/2.0/2.0/1")));
  }

  /**
   * Adapter for serializing and deserializing {@link LocalDateTime} objects.
   */
  private static class LocalDateTimeAdapter implements JsonSerializer<LocalDateTime>, JsonDeserializer<LocalDateTime> {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @Override
    public JsonElement serialize(
        LocalDateTime localDateTime, Type type, JsonSerializationContext jsonSerializationContext) {
      return new JsonPrimitive(formatter.format(localDateTime)); // "yyyy-MM-dd"
    }

    @Override
    public LocalDateTime deserialize(JsonElement jsonElement, Type type,
        JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
      return LocalDateTime.parse(jsonElement.getAsString(), formatter);
    }
  }
}
