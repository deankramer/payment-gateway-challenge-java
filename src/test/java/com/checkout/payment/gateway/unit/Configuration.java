package com.checkout.payment.gateway.unit;

import com.checkout.payment.gateway.client.AcquirerClient;
import com.checkout.payment.gateway.configuration.ApplicationConfiguration;
import com.checkout.payment.gateway.unit.client.AcquirerMockClient;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class Configuration extends ApplicationConfiguration {

  @Bean
  public AcquirerClient acquirerClient() {
    return new AcquirerMockClient();
  }
}
