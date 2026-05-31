package io.muehlbachler.bswe;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.JacksonJsonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.json.JsonMapper;

/**
 * Main class for the Spring Boot application.
 */
@SpringBootApplication
@EnableConfigurationProperties
@EnableCaching
public class App {
  /**
   * Main method.
   *
   * @param args the command line arguments
   */
  public static void main(final String[] args) {
    SpringApplication.run(App.class, args);
  }

  /**
   * Creates a new RestTemplate bean.
   *
   * @return a new RestTemplate bean
   */
  @Bean
  RestTemplate restTemplate(@Autowired final JsonMapper jsonMapper) {
    final List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
    messageConverters.add(new JacksonJsonHttpMessageConverter(jsonMapper));

    final RestTemplate restTemplate = new RestTemplate();
    restTemplate.setMessageConverters(messageConverters);
    return restTemplate;
  }

  /**
   * Customizes the Jackson JSON mapper.
   * 
   * @return the customized JsonMapper
   */
  @Bean
  @Primary
  JsonMapper jsonMapper() {
    return JsonMapper.builder()
        .configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false)
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        .configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true)
        .build();
  }
}
