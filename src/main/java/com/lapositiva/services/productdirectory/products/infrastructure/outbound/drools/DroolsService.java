package com.lapositiva.services.productdirectory.products.infrastructure.outbound.drools;

import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.stereotype.Service;

/**
 * Drools Service.
 * This class is used to execute the Drools rules.
 *
 * @see KieContainer
 * @see KieSession
 */
@Service
public class DroolsService {

  private final KieContainer kieContainer;

  /**
   * Constructor.
   *
   * @param kieContainer0 KieContainer
   */
  public DroolsService(final KieContainer kieContainer0) {

    this.kieContainer = kieContainer0;
  }

  /**
   * Execute Rules.
   * This method is used to execute the Drools rules.
   *
   * @param object Object
   * @param <T>    T
   * @return T
   */
  public <T> T executeRules(final T object) {

    KieSession kieSession = kieContainer.newKieSession();
    kieSession.insert(object);
    kieSession.fireAllRules();
    kieSession.dispose();
    return object;
  }
}