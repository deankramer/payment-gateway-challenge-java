package com.checkout.payment.gateway.configuration;

import java.time.Duration;
import com.checkout.payment.gateway.client.AcquirerClient;
import com.checkout.payment.gateway.client.AcquirerClientImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.client.RestTemplate;

@Configuration
@ComponentScan(basePackages = {
        "com.checkout.payment.gateway.controller",
        "com.checkout.payment.gateway.service",
        "com.checkout.payment.gateway.repository",
        "com.checkout.payment.gateway.client",
        "com.checkout.payment.gateway.validation",
        "com.checkout.payment.gateway.exception"})
public class ApplicationConfiguration {

  @Autowired
  private Environment env;

  @Bean
  public RestTemplate restTemplate(RestTemplateBuilder builder) {
    return builder
        .setConnectTimeout(Duration.ofMillis(10000))
        .setReadTimeout(Duration.ofMillis(10000))
        .build();
  }

  @Bean
  public AcquirerClient acquirerClient() {
    return new AcquirerClientImpl(
        env.getProperty("client.acquirer.baseUrl"), restTemplate(new RestTemplateBuilder()));
  }
}
