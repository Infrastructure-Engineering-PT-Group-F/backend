package io.muehlbachler.bswe.configuration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import io.muehlbachler.bswe.configuration.ApiConfiguration.ApiConnectionAuthorization;
import io.muehlbachler.bswe.configuration.ApiConfiguration.ApiConnectionInformation;
import org.junit.jupiter.api.Test;

public class ApiConfigurationTest {
  @Test
  void testApiConnectionInformationNoArgsConstructor() {
    final ApiConnectionInformation info = new ApiConnectionInformation();
    assertNotNull(info);
  }

  @Test
  void testApiConnectionInformationAllArgsConstructor() {
    final ApiConnectionAuthorization auth = new ApiConnectionAuthorization("header", "value");
    final ApiConnectionInformation info = new ApiConnectionInformation("url", 900, auth);

    assertNotNull(info);
    assertEquals("url", info.getUrl());
    assertEquals(900, info.getTtl());
    assertEquals(auth, info.getAuthorization());
  }

  @Test
  void testApiConnectionInformationSetters() {
    final ApiConnectionAuthorization auth = new ApiConnectionAuthorization("header", "value");
    final ApiConnectionInformation info = new ApiConnectionInformation();

    info.setUrl("url");
    info.setTtl(900);
    info.setAuthorization(auth);

    assertEquals("url", info.getUrl());
    assertEquals(900, info.getTtl());
    assertEquals(auth, info.getAuthorization());
  }

  @Test
  void testApiConnectionAuthorizationNoArgsConstructor() {
    final ApiConnectionAuthorization auth = new ApiConnectionAuthorization();
    assertNotNull(auth);
  }

  @Test
  void testApiConnectionAuthorizationAllArgsConstructor() {
    final ApiConnectionAuthorization auth = new ApiConnectionAuthorization("header", "value");

    assertNotNull(auth);
    assertEquals("header", auth.getHeader());
    assertEquals("value", auth.getValue());
  }

  @Test
  void testApiConnectionAuthorizationSetters() {
    final ApiConnectionAuthorization auth = new ApiConnectionAuthorization();

    auth.setHeader("header");
    auth.setValue("value");

    assertEquals("header", auth.getHeader());
    assertEquals("value", auth.getValue());
  }
}
