package io.muehlbachler.bswe.service.model.forecast;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.SequencedMap;

import io.muehlbachler.bswe.model.forecast.HourlyForecast;
import org.junit.jupiter.api.Test;

public class ForecastResultHourlyForecastTest {
  @Test
  void testNoArgsConstructor() {
    ForecastResultHourlyForecast forecast = new ForecastResultHourlyForecast();

    assertNotNull(forecast);
    assertNull(forecast.getTime());
    assertNull(forecast.getTemperature2m());
    assertNull(forecast.getRelativeHumidity2m());
    assertNull(forecast.getDewPoint2m());
    assertNull(forecast.getApparentTemperature());
    assertNull(forecast.getPrecipitationProbability());
    assertNull(forecast.getPrecipitation());
    assertNull(forecast.getCloudCover());
    assertNull(forecast.getVisibility());
    assertNull(forecast.getWindSpeed10m());
    assertNull(forecast.getWindDirection10m());
    assertNull(forecast.getWindGusts10m());
  }

  @Test
  void testAllArgsConstructor() {
    LocalDateTime[] time = new LocalDateTime[] { LocalDateTime.now() };
    float[] temp = new float[] { 20.5f };
    int[] humidity = new int[] { 65 };
    float[] dewPoint = new float[] { 15.3f };
    float[] feelsLike = new float[] { 19.8f };
    int[] precipProb = new int[] { 30 };
    float[] precip = new float[] { 0.5f };
    int[] cloudCover = new int[] { 75 };
    int[] visibility = new int[] { 10000 };
    float[] windSpeed = new float[] { 12.3f };
    int[] windDir = new int[] { 180 };
    float[] windGusts = new float[] { 15.7f };

    ForecastResultHourlyForecast forecast = new ForecastResultHourlyForecast(
        time, temp, humidity, dewPoint, feelsLike, precipProb,
        precip, cloudCover, visibility, windSpeed, windDir, windGusts);

    assertArrayEquals(time, forecast.getTime());
    assertArrayEquals(temp, forecast.getTemperature2m());
    assertArrayEquals(humidity, forecast.getRelativeHumidity2m());
    assertArrayEquals(dewPoint, forecast.getDewPoint2m());
    assertArrayEquals(feelsLike, forecast.getApparentTemperature());
    assertArrayEquals(precipProb, forecast.getPrecipitationProbability());
    assertArrayEquals(precip, forecast.getPrecipitation());
    assertArrayEquals(cloudCover, forecast.getCloudCover());
    assertArrayEquals(visibility, forecast.getVisibility());
    assertArrayEquals(windSpeed, forecast.getWindSpeed10m());
    assertArrayEquals(windDir, forecast.getWindDirection10m());
    assertArrayEquals(windGusts, forecast.getWindGusts10m());
  }

  @Test
  void testToString() {
    LocalDateTime now = LocalDateTime.now();
    ForecastResultHourlyForecast forecast = new ForecastResultHourlyForecast(
        new LocalDateTime[] { now },
        new float[] { 20.5f },
        new int[] { 65 },
        new float[] { 15.3f },
        new float[] { 19.8f },
        new int[] { 30 },
        new float[] { 0.5f },
        new int[] { 75 },
        new int[] { 10000 },
        new float[] { 12.3f },
        new int[] { 180 },
        new float[] { 15.7f });

    String toString = forecast.toString();
    assertNotNull(toString);
    assertTrue(toString.contains("20.5"));
    assertTrue(toString.contains("65"));
    assertTrue(toString.contains("180"));
  }

  @Test
  public void testTransformToHourlyForecasts() {
    LocalDateTime dateTime = LocalDateTime.of(2021, 1, 1, 0, 0);
    ForecastResultHourlyForecast forecastResultHourlyForecast = new ForecastResultHourlyForecast();
    forecastResultHourlyForecast.setTime(new LocalDateTime[] { dateTime });
    forecastResultHourlyForecast.setTemperature2m(new float[] { 1.0f });
    forecastResultHourlyForecast.setRelativeHumidity2m(new int[] { 2 });
    forecastResultHourlyForecast.setDewPoint2m(new float[] { 3.0f });
    forecastResultHourlyForecast.setApparentTemperature(new float[] { 4.0f });
    forecastResultHourlyForecast.setPrecipitationProbability(new int[] { 5 });
    forecastResultHourlyForecast.setPrecipitation(new float[] { 6.0f });
    forecastResultHourlyForecast.setCloudCover(new int[] { 7 });
    forecastResultHourlyForecast.setVisibility(new int[] { 8 });
    forecastResultHourlyForecast.setWindSpeed10m(new float[] { 9.0f });
    forecastResultHourlyForecast.setWindDirection10m(new int[] { 10 });
    forecastResultHourlyForecast.setWindGusts10m(new float[] { 11.0f });

    SequencedMap<LocalDateTime, HourlyForecast> hourlyForecast = forecastResultHourlyForecast
        .transformToHourlyForecasts();

    assertEquals(1, hourlyForecast.size());
    assertEquals(1.0f, hourlyForecast.get(dateTime).getTemperature());
    assertEquals(2, hourlyForecast.get(dateTime).getHumidity());
    assertEquals(3.0f, hourlyForecast.get(dateTime).getDewPoint());
    assertEquals(4.0f, hourlyForecast.get(dateTime).getFeelsLike());
    assertEquals(5, hourlyForecast.get(dateTime).getPrecipitationProbability());
    assertEquals(6.0f, hourlyForecast.get(dateTime).getPrecipitation());
    assertEquals(7, hourlyForecast.get(dateTime).getCloudCover());
    assertEquals(8, hourlyForecast.get(dateTime).getVisibility());
    assertEquals(9.0f, hourlyForecast.get(dateTime).getWindSpeed());
    assertEquals(10, hourlyForecast.get(dateTime).getWindDirection());
    assertEquals(11.0f, hourlyForecast.get(dateTime).getWindGusts());
  }

  @Test
  public void testTransformToHourlyForecastsWithNullValues() {
    ForecastResultHourlyForecast forecastResultHourlyForecast = new ForecastResultHourlyForecast();

    SequencedMap<LocalDateTime, HourlyForecast> hourlyForecast = forecastResultHourlyForecast
        .transformToHourlyForecasts();

    assertTrue(hourlyForecast.isEmpty());
  }

  @Test
  public void testTransformToHourlyForecastsWithNullTime() {
    ForecastResultHourlyForecast forecastResultHourlyForecast = new ForecastResultHourlyForecast();
    forecastResultHourlyForecast.setTime(null);
    forecastResultHourlyForecast.setTemperature2m(new float[] { 1.0f });
    forecastResultHourlyForecast.setRelativeHumidity2m(new int[] { 2 });
    forecastResultHourlyForecast.setDewPoint2m(new float[] { 3.0f });
    forecastResultHourlyForecast.setApparentTemperature(new float[] { 4.0f });
    forecastResultHourlyForecast.setPrecipitationProbability(new int[] { 5 });
    forecastResultHourlyForecast.setPrecipitation(new float[] { 6.0f });
    forecastResultHourlyForecast.setCloudCover(new int[] { 7 });
    forecastResultHourlyForecast.setVisibility(new int[] { 8 });
    forecastResultHourlyForecast.setWindSpeed10m(new float[] { 9.0f });
    forecastResultHourlyForecast.setWindDirection10m(new int[] { 10 });
    forecastResultHourlyForecast.setWindGusts10m(new float[] { 11.0f });

    SequencedMap<LocalDateTime, HourlyForecast> hourlyForecast = forecastResultHourlyForecast
        .transformToHourlyForecasts();

    assertTrue(hourlyForecast.isEmpty());
  }

  @Test
  public void testTransformToHourlyForecastsWithNullTemperature2m() {
    LocalDateTime dateTime = LocalDateTime.of(2021, 1, 1, 0, 0);
    ForecastResultHourlyForecast forecastResultHourlyForecast = new ForecastResultHourlyForecast();
    forecastResultHourlyForecast.setTime(new LocalDateTime[] { dateTime });
    forecastResultHourlyForecast.setTemperature2m(null);
    forecastResultHourlyForecast.setRelativeHumidity2m(new int[] { 2 });
    forecastResultHourlyForecast.setDewPoint2m(new float[] { 3.0f });
    forecastResultHourlyForecast.setApparentTemperature(new float[] { 4.0f });
    forecastResultHourlyForecast.setPrecipitationProbability(new int[] { 5 });
    forecastResultHourlyForecast.setPrecipitation(new float[] { 6.0f });
    forecastResultHourlyForecast.setCloudCover(new int[] { 7 });
    forecastResultHourlyForecast.setVisibility(new int[] { 8 });
    forecastResultHourlyForecast.setWindSpeed10m(new float[] { 9.0f });
    forecastResultHourlyForecast.setWindDirection10m(new int[] { 10 });
    forecastResultHourlyForecast.setWindGusts10m(new float[] { 11.0f });

    SequencedMap<LocalDateTime, HourlyForecast> hourlyForecast = forecastResultHourlyForecast
        .transformToHourlyForecasts();

    assertTrue(hourlyForecast.isEmpty());
  }

  @Test
  public void testTransformToHourlyForecastsWithNullRelativeHumidity2m() {
    LocalDateTime dateTime = LocalDateTime.of(2021, 1, 1, 0, 0);
    ForecastResultHourlyForecast forecastResultHourlyForecast = new ForecastResultHourlyForecast();
    forecastResultHourlyForecast.setTime(new LocalDateTime[] { dateTime });
    forecastResultHourlyForecast.setTemperature2m(new float[] { 1.0f });
    forecastResultHourlyForecast.setRelativeHumidity2m(null);
    forecastResultHourlyForecast.setDewPoint2m(new float[] { 3.0f });
    forecastResultHourlyForecast.setApparentTemperature(new float[] { 4.0f });
    forecastResultHourlyForecast.setPrecipitationProbability(new int[] { 5 });
    forecastResultHourlyForecast.setPrecipitation(new float[] { 6.0f });
    forecastResultHourlyForecast.setCloudCover(new int[] { 7 });
    forecastResultHourlyForecast.setVisibility(new int[] { 8 });
    forecastResultHourlyForecast.setWindSpeed10m(new float[] { 9.0f });
    forecastResultHourlyForecast.setWindDirection10m(new int[] { 10 });
    forecastResultHourlyForecast.setWindGusts10m(new float[] { 11.0f });

    SequencedMap<LocalDateTime, HourlyForecast> hourlyForecast = forecastResultHourlyForecast
        .transformToHourlyForecasts();

    assertTrue(hourlyForecast.isEmpty());
  }

  @Test
  public void testTransformToHourlyForecastsWithNullDewPoint2m() {
    LocalDateTime dateTime = LocalDateTime.of(2021, 1, 1, 0, 0);
    ForecastResultHourlyForecast forecastResultHourlyForecast = new ForecastResultHourlyForecast();
    forecastResultHourlyForecast.setTime(new LocalDateTime[] { dateTime });
    forecastResultHourlyForecast.setTemperature2m(new float[] { 1.0f });
    forecastResultHourlyForecast.setRelativeHumidity2m(new int[] { 2 });
    forecastResultHourlyForecast.setDewPoint2m(null);
    forecastResultHourlyForecast.setApparentTemperature(new float[] { 4.0f });
    forecastResultHourlyForecast.setPrecipitationProbability(new int[] { 5 });
    forecastResultHourlyForecast.setPrecipitation(new float[] { 6.0f });
    forecastResultHourlyForecast.setCloudCover(new int[] { 7 });
    forecastResultHourlyForecast.setVisibility(new int[] { 8 });
    forecastResultHourlyForecast.setWindSpeed10m(new float[] { 9.0f });
    forecastResultHourlyForecast.setWindDirection10m(new int[] { 10 });
    forecastResultHourlyForecast.setWindGusts10m(new float[] { 11.0f });

    SequencedMap<LocalDateTime, HourlyForecast> hourlyForecast = forecastResultHourlyForecast
        .transformToHourlyForecasts();

    assertTrue(hourlyForecast.isEmpty());
  }

  @Test
  public void testTransformToHourlyForecastsWithNullApparentTemperature() {
    LocalDateTime dateTime = LocalDateTime.of(2021, 1, 1, 0, 0);
    ForecastResultHourlyForecast forecastResultHourlyForecast = new ForecastResultHourlyForecast();
    forecastResultHourlyForecast.setTime(new LocalDateTime[] { dateTime });
    forecastResultHourlyForecast.setTemperature2m(new float[] { 1.0f });
    forecastResultHourlyForecast.setRelativeHumidity2m(new int[] { 2 });
    forecastResultHourlyForecast.setDewPoint2m(new float[] { 3.0f });
    forecastResultHourlyForecast.setApparentTemperature(null);
    forecastResultHourlyForecast.setPrecipitationProbability(new int[] { 5 });
    forecastResultHourlyForecast.setPrecipitation(new float[] { 6.0f });
    forecastResultHourlyForecast.setCloudCover(new int[] { 7 });
    forecastResultHourlyForecast.setVisibility(new int[] { 8 });
    forecastResultHourlyForecast.setWindSpeed10m(new float[] { 9.0f });
    forecastResultHourlyForecast.setWindDirection10m(new int[] { 10 });
    forecastResultHourlyForecast.setWindGusts10m(new float[] { 11.0f });

    SequencedMap<LocalDateTime, HourlyForecast> hourlyForecast = forecastResultHourlyForecast
        .transformToHourlyForecasts();

    assertTrue(hourlyForecast.isEmpty());
  }

  @Test
  public void testTransformToHourlyForecastsWithNullPrecipitationProbability() {
    LocalDateTime dateTime = LocalDateTime.of(2021, 1, 1, 0, 0);
    ForecastResultHourlyForecast forecastResultHourlyForecast = new ForecastResultHourlyForecast();
    forecastResultHourlyForecast.setTime(new LocalDateTime[] { dateTime });
    forecastResultHourlyForecast.setTemperature2m(new float[] { 1.0f });
    forecastResultHourlyForecast.setRelativeHumidity2m(new int[] { 2 });
    forecastResultHourlyForecast.setDewPoint2m(new float[] { 3.0f });
    forecastResultHourlyForecast.setApparentTemperature(new float[] { 4.0f });
    forecastResultHourlyForecast.setPrecipitationProbability(null);
    forecastResultHourlyForecast.setPrecipitation(new float[] { 6.0f });
    forecastResultHourlyForecast.setCloudCover(new int[] { 7 });
    forecastResultHourlyForecast.setVisibility(new int[] { 8 });
    forecastResultHourlyForecast.setWindSpeed10m(new float[] { 9.0f });
    forecastResultHourlyForecast.setWindDirection10m(new int[] { 10 });
    forecastResultHourlyForecast.setWindGusts10m(new float[] { 11.0f });

    SequencedMap<LocalDateTime, HourlyForecast> hourlyForecast = forecastResultHourlyForecast
        .transformToHourlyForecasts();

    assertTrue(hourlyForecast.isEmpty());
  }

  @Test
  public void testTransformToHourlyForecastsWithNullPrecipitation() {
    LocalDateTime dateTime = LocalDateTime.of(2021, 1, 1, 0, 0);
    ForecastResultHourlyForecast forecastResultHourlyForecast = new ForecastResultHourlyForecast();
    forecastResultHourlyForecast.setTime(new LocalDateTime[] { dateTime });
    forecastResultHourlyForecast.setTemperature2m(new float[] { 1.0f });
    forecastResultHourlyForecast.setRelativeHumidity2m(new int[] { 2 });
    forecastResultHourlyForecast.setDewPoint2m(new float[] { 3.0f });
    forecastResultHourlyForecast.setApparentTemperature(new float[] { 4.0f });
    forecastResultHourlyForecast.setPrecipitationProbability(new int[] { 5 });
    forecastResultHourlyForecast.setPrecipitation(null);
    forecastResultHourlyForecast.setCloudCover(new int[] { 7 });
    forecastResultHourlyForecast.setVisibility(new int[] { 8 });
    forecastResultHourlyForecast.setWindSpeed10m(new float[] { 9.0f });
    forecastResultHourlyForecast.setWindDirection10m(new int[] { 10 });
    forecastResultHourlyForecast.setWindGusts10m(new float[] { 11.0f });

    SequencedMap<LocalDateTime, HourlyForecast> hourlyForecast = forecastResultHourlyForecast
        .transformToHourlyForecasts();

    assertTrue(hourlyForecast.isEmpty());
  }

  @Test
  public void testTransformToHourlyForecastsWithNullCloudCover() {
    LocalDateTime dateTime = LocalDateTime.of(2021, 1, 1, 0, 0);
    ForecastResultHourlyForecast forecastResultHourlyForecast = new ForecastResultHourlyForecast();
    forecastResultHourlyForecast.setTime(new LocalDateTime[] { dateTime });
    forecastResultHourlyForecast.setTemperature2m(new float[] { 1.0f });
    forecastResultHourlyForecast.setRelativeHumidity2m(new int[] { 2 });
    forecastResultHourlyForecast.setDewPoint2m(new float[] { 3.0f });
    forecastResultHourlyForecast.setApparentTemperature(new float[] { 4.0f });
    forecastResultHourlyForecast.setPrecipitationProbability(new int[] { 5 });
    forecastResultHourlyForecast.setPrecipitation(new float[] { 6.0f });
    forecastResultHourlyForecast.setCloudCover(null);
    forecastResultHourlyForecast.setVisibility(new int[] { 8 });
    forecastResultHourlyForecast.setWindSpeed10m(new float[] { 9.0f });
    forecastResultHourlyForecast.setWindDirection10m(new int[] { 10 });
    forecastResultHourlyForecast.setWindGusts10m(new float[] { 11.0f });

    SequencedMap<LocalDateTime, HourlyForecast> hourlyForecast = forecastResultHourlyForecast
        .transformToHourlyForecasts();

    assertTrue(hourlyForecast.isEmpty());
  }

  @Test
  public void testTransformToHourlyForecastsWithNullVisibility() {
    LocalDateTime dateTime = LocalDateTime.of(2021, 1, 1, 0, 0);
    ForecastResultHourlyForecast forecastResultHourlyForecast = new ForecastResultHourlyForecast();
    forecastResultHourlyForecast.setTime(new LocalDateTime[] { dateTime });
    forecastResultHourlyForecast.setTemperature2m(new float[] { 1.0f });
    forecastResultHourlyForecast.setRelativeHumidity2m(new int[] { 2 });
    forecastResultHourlyForecast.setDewPoint2m(new float[] { 3.0f });
    forecastResultHourlyForecast.setApparentTemperature(new float[] { 4.0f });
    forecastResultHourlyForecast.setPrecipitationProbability(new int[] { 5 });
    forecastResultHourlyForecast.setPrecipitation(new float[] { 6.0f });
    forecastResultHourlyForecast.setCloudCover(new int[] { 7 });
    forecastResultHourlyForecast.setVisibility(null);
    forecastResultHourlyForecast.setWindSpeed10m(new float[] { 9.0f });
    forecastResultHourlyForecast.setWindDirection10m(new int[] { 10 });
    forecastResultHourlyForecast.setWindGusts10m(new float[] { 11.0f });

    SequencedMap<LocalDateTime, HourlyForecast> hourlyForecast = forecastResultHourlyForecast
        .transformToHourlyForecasts();

    assertTrue(hourlyForecast.isEmpty());
  }

  @Test
  public void testTransformToHourlyForecastsWithNullWindSpeed10m() {
    LocalDateTime dateTime = LocalDateTime.of(2021, 1, 1, 0, 0);
    ForecastResultHourlyForecast forecastResultHourlyForecast = new ForecastResultHourlyForecast();
    forecastResultHourlyForecast.setTime(new LocalDateTime[] { dateTime });
    forecastResultHourlyForecast.setTemperature2m(new float[] { 1.0f });
    forecastResultHourlyForecast.setRelativeHumidity2m(new int[] { 2 });
    forecastResultHourlyForecast.setDewPoint2m(new float[] { 3.0f });
    forecastResultHourlyForecast.setApparentTemperature(new float[] { 4.0f });
    forecastResultHourlyForecast.setPrecipitationProbability(new int[] { 5 });
    forecastResultHourlyForecast.setPrecipitation(new float[] { 6.0f });
    forecastResultHourlyForecast.setCloudCover(new int[] { 7 });
    forecastResultHourlyForecast.setVisibility(new int[] { 8 });
    forecastResultHourlyForecast.setWindSpeed10m(null);
    forecastResultHourlyForecast.setWindDirection10m(new int[] { 10 });
    forecastResultHourlyForecast.setWindGusts10m(new float[] { 11.0f });

    SequencedMap<LocalDateTime, HourlyForecast> hourlyForecast = forecastResultHourlyForecast
        .transformToHourlyForecasts();

    assertTrue(hourlyForecast.isEmpty());
  }

  @Test
  public void testTransformToHourlyForecastsWithNullWindDirection10m() {
    LocalDateTime dateTime = LocalDateTime.of(2021, 1, 1, 0, 0);
    ForecastResultHourlyForecast forecastResultHourlyForecast = new ForecastResultHourlyForecast();
    forecastResultHourlyForecast.setTime(new LocalDateTime[] { dateTime });
    forecastResultHourlyForecast.setTemperature2m(new float[] { 1.0f });
    forecastResultHourlyForecast.setRelativeHumidity2m(new int[] { 2 });
    forecastResultHourlyForecast.setDewPoint2m(new float[] { 3.0f });
    forecastResultHourlyForecast.setApparentTemperature(new float[] { 4.0f });
    forecastResultHourlyForecast.setPrecipitationProbability(new int[] { 5 });
    forecastResultHourlyForecast.setPrecipitation(new float[] { 6.0f });
    forecastResultHourlyForecast.setCloudCover(new int[] { 7 });
    forecastResultHourlyForecast.setVisibility(new int[] { 8 });
    forecastResultHourlyForecast.setWindSpeed10m(new float[] { 9.0f });
    forecastResultHourlyForecast.setWindDirection10m(null);
    forecastResultHourlyForecast.setWindGusts10m(new float[] { 11.0f });

    SequencedMap<LocalDateTime, HourlyForecast> hourlyForecast = forecastResultHourlyForecast
        .transformToHourlyForecasts();

    assertTrue(hourlyForecast.isEmpty());
  }

  @Test
  public void testTransformToHourlyForecastsWithNullWindGusts10m() {
    LocalDateTime dateTime = LocalDateTime.of(2021, 1, 1, 0, 0);
    ForecastResultHourlyForecast forecastResultHourlyForecast = new ForecastResultHourlyForecast();
    forecastResultHourlyForecast.setTime(new LocalDateTime[] { dateTime });
    forecastResultHourlyForecast.setTemperature2m(new float[] { 1.0f });
    forecastResultHourlyForecast.setRelativeHumidity2m(new int[] { 2 });
    forecastResultHourlyForecast.setDewPoint2m(new float[] { 3.0f });
    forecastResultHourlyForecast.setApparentTemperature(new float[] { 4.0f });
    forecastResultHourlyForecast.setPrecipitationProbability(new int[] { 5 });
    forecastResultHourlyForecast.setPrecipitation(new float[] { 6.0f });
    forecastResultHourlyForecast.setCloudCover(new int[] { 7 });
    forecastResultHourlyForecast.setVisibility(new int[] { 8 });
    forecastResultHourlyForecast.setWindSpeed10m(new float[] { 9.0f });
    forecastResultHourlyForecast.setWindDirection10m(new int[] { 10 });
    forecastResultHourlyForecast.setWindGusts10m(null);

    SequencedMap<LocalDateTime, HourlyForecast> hourlyForecast = forecastResultHourlyForecast
        .transformToHourlyForecasts();

    assertTrue(hourlyForecast.isEmpty());
  }

  @Test
  public void testTransformToHourlyForecastsWithEmptyTime() {
    ForecastResultHourlyForecast forecastResultHourlyForecast = new ForecastResultHourlyForecast();
    forecastResultHourlyForecast.setTime(new LocalDateTime[] {});
    forecastResultHourlyForecast.setTemperature2m(new float[] { 1.0f });
    forecastResultHourlyForecast.setRelativeHumidity2m(new int[] { 2 });
    forecastResultHourlyForecast.setDewPoint2m(new float[] { 3.0f });
    forecastResultHourlyForecast.setApparentTemperature(new float[] { 4.0f });
    forecastResultHourlyForecast.setPrecipitationProbability(new int[] { 5 });
    forecastResultHourlyForecast.setPrecipitation(new float[] { 6.0f });
    forecastResultHourlyForecast.setCloudCover(new int[] { 7 });
    forecastResultHourlyForecast.setVisibility(new int[] { 8 });
    forecastResultHourlyForecast.setWindSpeed10m(new float[] { 9.0f });
    forecastResultHourlyForecast.setWindDirection10m(new int[] { 10 });
    forecastResultHourlyForecast.setWindGusts10m(new float[] { 11.0f });

    SequencedMap<LocalDateTime, HourlyForecast> hourlyForecast = forecastResultHourlyForecast
        .transformToHourlyForecasts();

    assertTrue(hourlyForecast.isEmpty());
  }

  @Test
  public void testTransformToHourlyForecastsWithEmptyTemperature2m() {
    LocalDateTime dateTime = LocalDateTime.of(2021, 1, 1, 0, 0);
    ForecastResultHourlyForecast forecastResultHourlyForecast = new ForecastResultHourlyForecast();
    forecastResultHourlyForecast.setTime(new LocalDateTime[] { dateTime });
    forecastResultHourlyForecast.setTemperature2m(new float[] {});
    forecastResultHourlyForecast.setRelativeHumidity2m(new int[] { 2 });
    forecastResultHourlyForecast.setDewPoint2m(new float[] { 3.0f });
    forecastResultHourlyForecast.setApparentTemperature(new float[] { 4.0f });
    forecastResultHourlyForecast.setPrecipitationProbability(new int[] { 5 });
    forecastResultHourlyForecast.setPrecipitation(new float[] { 6.0f });
    forecastResultHourlyForecast.setCloudCover(new int[] { 7 });
    forecastResultHourlyForecast.setVisibility(new int[] { 8 });
    forecastResultHourlyForecast.setWindSpeed10m(new float[] { 9.0f });
    forecastResultHourlyForecast.setWindDirection10m(new int[] { 10 });
    forecastResultHourlyForecast.setWindGusts10m(new float[] { 11.0f });

    SequencedMap<LocalDateTime, HourlyForecast> hourlyForecast = forecastResultHourlyForecast
        .transformToHourlyForecasts();

    assertTrue(hourlyForecast.isEmpty());
  }

  @Test
  public void testTransformToHourlyForecastsWithEmptyRelativeHumidity2m() {
    LocalDateTime dateTime = LocalDateTime.of(2021, 1, 1, 0, 0);
    ForecastResultHourlyForecast forecastResultHourlyForecast = new ForecastResultHourlyForecast();
    forecastResultHourlyForecast.setTime(new LocalDateTime[] { dateTime });
    forecastResultHourlyForecast.setTemperature2m(new float[] { 1.0f });
    forecastResultHourlyForecast.setRelativeHumidity2m(new int[] {});
    forecastResultHourlyForecast.setDewPoint2m(new float[] { 3.0f });
    forecastResultHourlyForecast.setApparentTemperature(new float[] { 4.0f });
    forecastResultHourlyForecast.setPrecipitationProbability(new int[] { 5 });
    forecastResultHourlyForecast.setPrecipitation(new float[] { 6.0f });
    forecastResultHourlyForecast.setCloudCover(new int[] { 7 });
    forecastResultHourlyForecast.setVisibility(new int[] { 8 });
    forecastResultHourlyForecast.setWindSpeed10m(new float[] { 9.0f });
    forecastResultHourlyForecast.setWindDirection10m(new int[] { 10 });
    forecastResultHourlyForecast.setWindGusts10m(new float[] { 11.0f });

    SequencedMap<LocalDateTime, HourlyForecast> hourlyForecast = forecastResultHourlyForecast
        .transformToHourlyForecasts();

    assertTrue(hourlyForecast.isEmpty());
  }

  @Test
  public void testTransformToHourlyForecastsWithEmptyDewPoint2m() {
    LocalDateTime dateTime = LocalDateTime.of(2021, 1, 1, 0, 0);
    ForecastResultHourlyForecast forecastResultHourlyForecast = new ForecastResultHourlyForecast();
    forecastResultHourlyForecast.setTime(new LocalDateTime[] { dateTime });
    forecastResultHourlyForecast.setTemperature2m(new float[] { 1.0f });
    forecastResultHourlyForecast.setRelativeHumidity2m(new int[] { 2 });
    forecastResultHourlyForecast.setDewPoint2m(new float[] {});
    forecastResultHourlyForecast.setApparentTemperature(new float[] { 4.0f });
    forecastResultHourlyForecast.setPrecipitationProbability(new int[] { 5 });
    forecastResultHourlyForecast.setPrecipitation(new float[] { 6.0f });
    forecastResultHourlyForecast.setCloudCover(new int[] { 7 });
    forecastResultHourlyForecast.setVisibility(new int[] { 8 });
    forecastResultHourlyForecast.setWindSpeed10m(new float[] { 9.0f });
    forecastResultHourlyForecast.setWindDirection10m(new int[] { 10 });
    forecastResultHourlyForecast.setWindGusts10m(new float[] { 11.0f });

    SequencedMap<LocalDateTime, HourlyForecast> hourlyForecast = forecastResultHourlyForecast
        .transformToHourlyForecasts();

    assertTrue(hourlyForecast.isEmpty());
  }

  @Test
  public void testTransformToHourlyForecastsWithEmptyApparentTemperature() {
    LocalDateTime dateTime = LocalDateTime.of(2021, 1, 1, 0, 0);
    ForecastResultHourlyForecast forecastResultHourlyForecast = new ForecastResultHourlyForecast();
    forecastResultHourlyForecast.setTime(new LocalDateTime[] { dateTime });
    forecastResultHourlyForecast.setTemperature2m(new float[] { 1.0f });
    forecastResultHourlyForecast.setRelativeHumidity2m(new int[] { 2 });
    forecastResultHourlyForecast.setDewPoint2m(new float[] { 3.0f });
    forecastResultHourlyForecast.setApparentTemperature(new float[] {});
    forecastResultHourlyForecast.setPrecipitationProbability(new int[] { 5 });
    forecastResultHourlyForecast.setPrecipitation(new float[] { 6.0f });
    forecastResultHourlyForecast.setCloudCover(new int[] { 7 });
    forecastResultHourlyForecast.setVisibility(new int[] { 8 });
    forecastResultHourlyForecast.setWindSpeed10m(new float[] { 9.0f });
    forecastResultHourlyForecast.setWindDirection10m(new int[] { 10 });
    forecastResultHourlyForecast.setWindGusts10m(new float[] { 11.0f });

    SequencedMap<LocalDateTime, HourlyForecast> hourlyForecast = forecastResultHourlyForecast
        .transformToHourlyForecasts();

    assertTrue(hourlyForecast.isEmpty());
  }

  @Test
  public void testTransformToHourlyForecastsWithEmptyPrecipitationProbability() {
    LocalDateTime dateTime = LocalDateTime.of(2021, 1, 1, 0, 0);
    ForecastResultHourlyForecast forecastResultHourlyForecast = new ForecastResultHourlyForecast();
    forecastResultHourlyForecast.setTime(new LocalDateTime[] { dateTime });
    forecastResultHourlyForecast.setTemperature2m(new float[] { 1.0f });
    forecastResultHourlyForecast.setRelativeHumidity2m(new int[] { 2 });
    forecastResultHourlyForecast.setDewPoint2m(new float[] { 3.0f });
    forecastResultHourlyForecast.setApparentTemperature(new float[] { 4.0f });
    forecastResultHourlyForecast.setPrecipitationProbability(new int[] {});
    forecastResultHourlyForecast.setPrecipitation(new float[] { 6.0f });
    forecastResultHourlyForecast.setCloudCover(new int[] { 7 });
    forecastResultHourlyForecast.setVisibility(new int[] { 8 });
    forecastResultHourlyForecast.setWindSpeed10m(new float[] { 9.0f });
    forecastResultHourlyForecast.setWindDirection10m(new int[] { 10 });
    forecastResultHourlyForecast.setWindGusts10m(new float[] { 11.0f });

    SequencedMap<LocalDateTime, HourlyForecast> hourlyForecast = forecastResultHourlyForecast
        .transformToHourlyForecasts();

    assertTrue(hourlyForecast.isEmpty());
  }

  @Test
  public void testTransformToHourlyForecastsWithEmptyPrecipitation() {
    LocalDateTime dateTime = LocalDateTime.of(2021, 1, 1, 0, 0);
    ForecastResultHourlyForecast forecastResultHourlyForecast = new ForecastResultHourlyForecast();
    forecastResultHourlyForecast.setTime(new LocalDateTime[] { dateTime });
    forecastResultHourlyForecast.setTemperature2m(new float[] { 1.0f });
    forecastResultHourlyForecast.setRelativeHumidity2m(new int[] { 2 });
    forecastResultHourlyForecast.setDewPoint2m(new float[] { 3.0f });
    forecastResultHourlyForecast.setApparentTemperature(new float[] { 4.0f });
    forecastResultHourlyForecast.setPrecipitationProbability(new int[] { 5 });
    forecastResultHourlyForecast.setPrecipitation(new float[] {});
    forecastResultHourlyForecast.setCloudCover(new int[] { 7 });
    forecastResultHourlyForecast.setVisibility(new int[] { 8 });
    forecastResultHourlyForecast.setWindSpeed10m(new float[] { 9.0f });
    forecastResultHourlyForecast.setWindDirection10m(new int[] { 10 });
    forecastResultHourlyForecast.setWindGusts10m(new float[] { 11.0f });

    SequencedMap<LocalDateTime, HourlyForecast> hourlyForecast = forecastResultHourlyForecast
        .transformToHourlyForecasts();

    assertTrue(hourlyForecast.isEmpty());
  }

  @Test
  public void testTransformToHourlyForecastsWithEmptyCloudCover() {
    LocalDateTime dateTime = LocalDateTime.of(2021, 1, 1, 0, 0);
    ForecastResultHourlyForecast forecastResultHourlyForecast = new ForecastResultHourlyForecast();
    forecastResultHourlyForecast.setTime(new LocalDateTime[] { dateTime });
    forecastResultHourlyForecast.setTemperature2m(new float[] { 1.0f });
    forecastResultHourlyForecast.setRelativeHumidity2m(new int[] { 2 });
    forecastResultHourlyForecast.setDewPoint2m(new float[] { 3.0f });
    forecastResultHourlyForecast.setApparentTemperature(new float[] { 4.0f });
    forecastResultHourlyForecast.setPrecipitationProbability(new int[] { 5 });
    forecastResultHourlyForecast.setPrecipitation(new float[] { 6.0f });
    forecastResultHourlyForecast.setCloudCover(new int[] {});
    forecastResultHourlyForecast.setVisibility(new int[] { 8 });
    forecastResultHourlyForecast.setWindSpeed10m(new float[] { 9.0f });
    forecastResultHourlyForecast.setWindDirection10m(new int[] { 10 });
    forecastResultHourlyForecast.setWindGusts10m(new float[] { 11.0f });

    SequencedMap<LocalDateTime, HourlyForecast> hourlyForecast = forecastResultHourlyForecast
        .transformToHourlyForecasts();

    assertTrue(hourlyForecast.isEmpty());
  }

  @Test
  public void testTransformToHourlyForecastsWithEmptyVisibility() {
    LocalDateTime dateTime = LocalDateTime.of(2021, 1, 1, 0, 0);
    ForecastResultHourlyForecast forecastResultHourlyForecast = new ForecastResultHourlyForecast();
    forecastResultHourlyForecast.setTime(new LocalDateTime[] { dateTime });
    forecastResultHourlyForecast.setTemperature2m(new float[] { 1.0f });
    forecastResultHourlyForecast.setRelativeHumidity2m(new int[] { 2 });
    forecastResultHourlyForecast.setDewPoint2m(new float[] { 3.0f });
    forecastResultHourlyForecast.setApparentTemperature(new float[] { 4.0f });
    forecastResultHourlyForecast.setPrecipitationProbability(new int[] { 5 });
    forecastResultHourlyForecast.setPrecipitation(new float[] { 6.0f });
    forecastResultHourlyForecast.setCloudCover(new int[] { 7 });
    forecastResultHourlyForecast.setVisibility(new int[] {});
    forecastResultHourlyForecast.setWindSpeed10m(new float[] { 9.0f });
    forecastResultHourlyForecast.setWindDirection10m(new int[] { 10 });
    forecastResultHourlyForecast.setWindGusts10m(new float[] { 11.0f });

    SequencedMap<LocalDateTime, HourlyForecast> hourlyForecast = forecastResultHourlyForecast
        .transformToHourlyForecasts();

    assertTrue(hourlyForecast.isEmpty());
  }

  @Test
  public void testTransformToHourlyForecastsWithEmptyWindSpeed10m() {
    LocalDateTime dateTime = LocalDateTime.of(2021, 1, 1, 0, 0);
    ForecastResultHourlyForecast forecastResultHourlyForecast = new ForecastResultHourlyForecast();
    forecastResultHourlyForecast.setTime(new LocalDateTime[] { dateTime });
    forecastResultHourlyForecast.setTemperature2m(new float[] { 1.0f });
    forecastResultHourlyForecast.setRelativeHumidity2m(new int[] { 2 });
    forecastResultHourlyForecast.setDewPoint2m(new float[] { 3.0f });
    forecastResultHourlyForecast.setApparentTemperature(new float[] { 4.0f });
    forecastResultHourlyForecast.setPrecipitationProbability(new int[] { 5 });
    forecastResultHourlyForecast.setPrecipitation(new float[] { 6.0f });
    forecastResultHourlyForecast.setCloudCover(new int[] { 7 });
    forecastResultHourlyForecast.setVisibility(new int[] { 8 });
    forecastResultHourlyForecast.setWindSpeed10m(new float[] {});
    forecastResultHourlyForecast.setWindDirection10m(new int[] { 10 });
    forecastResultHourlyForecast.setWindGusts10m(new float[] { 11.0f });

    SequencedMap<LocalDateTime, HourlyForecast> hourlyForecast = forecastResultHourlyForecast
        .transformToHourlyForecasts();

    assertTrue(hourlyForecast.isEmpty());
  }

  @Test
  public void testTransformToHourlyForecastsWithEmptyWindDirection10m() {
    LocalDateTime dateTime = LocalDateTime.of(2021, 1, 1, 0, 0);
    ForecastResultHourlyForecast forecastResultHourlyForecast = new ForecastResultHourlyForecast();
    forecastResultHourlyForecast.setTime(new LocalDateTime[] { dateTime });
    forecastResultHourlyForecast.setTemperature2m(new float[] { 1.0f });
    forecastResultHourlyForecast.setRelativeHumidity2m(new int[] { 2 });
    forecastResultHourlyForecast.setDewPoint2m(new float[] { 3.0f });
    forecastResultHourlyForecast.setApparentTemperature(new float[] { 4.0f });
    forecastResultHourlyForecast.setPrecipitationProbability(new int[] { 5 });
    forecastResultHourlyForecast.setPrecipitation(new float[] { 6.0f });
    forecastResultHourlyForecast.setCloudCover(new int[] { 7 });
    forecastResultHourlyForecast.setVisibility(new int[] { 8 });
    forecastResultHourlyForecast.setWindSpeed10m(new float[] { 9.0f });
    forecastResultHourlyForecast.setWindDirection10m(new int[] {});
    forecastResultHourlyForecast.setWindGusts10m(new float[] { 11.0f });

    SequencedMap<LocalDateTime, HourlyForecast> hourlyForecast = forecastResultHourlyForecast
        .transformToHourlyForecasts();

    assertTrue(hourlyForecast.isEmpty());
  }

  @Test
  public void testTransformToHourlyForecastsWithEmptyWindGusts10m() {
    LocalDateTime dateTime = LocalDateTime.of(2021, 1, 1, 0, 0);
    ForecastResultHourlyForecast forecastResultHourlyForecast = new ForecastResultHourlyForecast();
    forecastResultHourlyForecast.setTime(new LocalDateTime[] { dateTime });
    forecastResultHourlyForecast.setTemperature2m(new float[] { 1.0f });
    forecastResultHourlyForecast.setRelativeHumidity2m(new int[] { 2 });
    forecastResultHourlyForecast.setDewPoint2m(new float[] { 3.0f });
    forecastResultHourlyForecast.setApparentTemperature(new float[] { 4.0f });
    forecastResultHourlyForecast.setPrecipitationProbability(new int[] { 5 });
    forecastResultHourlyForecast.setPrecipitation(new float[] { 6.0f });
    forecastResultHourlyForecast.setCloudCover(new int[] { 7 });
    forecastResultHourlyForecast.setVisibility(new int[] { 8 });
    forecastResultHourlyForecast.setWindSpeed10m(new float[] { 9.0f });
    forecastResultHourlyForecast.setWindDirection10m(new int[] { 10 });
    forecastResultHourlyForecast.setWindGusts10m(new float[] {});

    SequencedMap<LocalDateTime, HourlyForecast> hourlyForecast = forecastResultHourlyForecast
        .transformToHourlyForecasts();

    assertTrue(hourlyForecast.isEmpty());
  }
}
