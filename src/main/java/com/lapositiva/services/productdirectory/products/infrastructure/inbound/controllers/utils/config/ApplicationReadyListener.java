package com.lapositiva.services.productdirectory.products.infrastructure.inbound.controllers.utils.config;

import com.lapositiva.services.productdirectory.products.infrastructure.inbound.controllers.utils.utils.PropertyUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * Application Ready Listener.
 * This class is used to configure the properties resolver.
 */
@Component
@Slf4j
public class ApplicationReadyListener implements ApplicationListener<ApplicationReadyEvent> {

  /**
   * Configures the properties resolver.
   *
   * @param event the application ready event.
   */
  @Override
  public void onApplicationEvent(final ApplicationReadyEvent event) {
    log.trace("Configuring properties resolver...");
    final Environment environment = event.getApplicationContext().getEnvironment();
    PropertyUtils.setResolver(environment);
  }

}