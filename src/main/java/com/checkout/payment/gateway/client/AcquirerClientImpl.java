package com.checkout.payment.gateway.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

public class AcquirerClientImpl extends BaseClient implements AcquirerClient {

  private static final String PAYMENTS = "/payments";

  @Autowired
  public AcquirerClientImpl(String baseUrl, RestTemplate restTemplate) {
    super(baseUrl, restTemplate);
  }

  @Override
  public AcquirerPaymentResponse processPayment(AcquirerPaymentRequest request) {
    return post(PAYMENTS, request, AcquirerPaymentResponse.class);
  }

  @Override
  public boolean healthCheck() {
    return false;
  }

}
