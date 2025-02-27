package com.checkout.payment.gateway.unit.client;

import com.checkout.payment.gateway.client.AcquirerClient;

import static java.util.UUID.*;

public class AcquirerMockClient implements AcquirerClient {

  private boolean authorized = true;
  private boolean healthy = true;

  public void setAuthorized(boolean authorized) {
    this.authorized = authorized;
  }

  public void setHealthy(boolean healthy) {
    this.healthy = healthy;
  }

  @Override
  public AcquirerPaymentResponse processPayment(AcquirerPaymentRequest request) {
    AcquirerPaymentResponse response;
    return authorized ?
        new AcquirerPaymentResponse(true, randomUUID()) :
        new AcquirerPaymentResponse(false, null);
  }

  @Override
  public boolean healthCheck() {
    return healthy;
  }
}
