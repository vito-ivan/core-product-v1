package com.lapositiva.services.productdirectory.products.infrastructure.outbound.drools;

import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.runtime.KieContainer;
import org.kie.internal.io.ResourceFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Drools Configuration.
 * This class is used to configure the Drools engine.
 * @see KieServices
 * @see KieBuilder
 * @see KieFileSystem
 * @see KieContainer
 * @see ResourceFactory
 */
@Configuration
public class DroolsConfig {

  /**
   * Kie Container.
   * This method is used to create a Kie Container.
   *
   * @return KieContainer
   */
  @Bean
  public KieContainer kieContainer() {

    KieServices kieServices = KieServices.Factory.get();
    KieFileSystem kieFileSystem = kieServices.newKieFileSystem();
    kieFileSystem.write(ResourceFactory.newClassPathResource("rules.drl"));
    KieBuilder kieBuilder = kieServices.newKieBuilder(kieFileSystem);
    kieBuilder.buildAll();
    return kieServices.newKieContainer(kieServices.getRepository().getDefaultReleaseId());
  }
}
