package com.lapositiva.services.productdirectory.products.infrastructure.inbound.controllers.utils.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import static com.fasterxml.jackson.core.JsonGenerator.Feature.IGNORE_UNKNOWN;
import static com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_COMMENTS;
import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import static com.fasterxml.jackson.databind.MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES;
import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS;

public interface JacksonMapperFactory {

  /**
   * Method to create a jackson builder.
   *
   * @return A builder to create jackson mapper.
   */
  static Jackson2ObjectMapperBuilder builder() {
    return new Jackson2ObjectMapperBuilder().modules(new JavaTimeModule(), new Jdk8Module())
        .serializationInclusion(JsonInclude.Include.NON_NULL).dateFormat(new StdDateFormat())
        .featuresToDisable(FAIL_ON_UNKNOWN_PROPERTIES).featuresToDisable(WRITE_DATES_AS_TIMESTAMPS)
        .featuresToEnable(ACCEPT_CASE_INSENSITIVE_PROPERTIES).featuresToEnable(IGNORE_UNKNOWN)
        .featuresToEnable(ALLOW_COMMENTS);
  }

  /**
   * Method to create a new object mapper.
   *
   * @return A new object mapper.
   */
  static ObjectMapper newObjectMapper() {
    return builder().createXmlMapper(false).build();
  }
}
