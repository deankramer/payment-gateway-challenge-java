package com.checkout.payment.gateway.integration;

import com.checkout.payment.gateway.client.GatewayClient;
import com.checkout.payment.gateway.client.GatewayClientImpl;
import com.checkout.payment.gateway.gen.Gen;
import org.springframework.web.client.RestTemplate;

import static com.checkout.payment.gateway.util.TestUtil.section;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Integration {

  private GatewayClient gatewayClient;


  public static void main(String[] args) {
    var gatewayClient = new GatewayClientImpl("http://localhost:8090", new RestTemplate());
    var integration = new Integration(gatewayClient);
    integration.all();
  }

  public Integration(GatewayClient gatewayClient) {
    this.gatewayClient = gatewayClient;
  }

  public void all() {
    section("Health Check", this::health);
    section("Payment", this::payment);
  }

  public void health() {
    var health = gatewayClient.getHealth();
    assertNotNull(health);
    assertTrue(health.getDbStatus());
    assertTrue(health.getAcquirerStatus());
  }

  public void payment() {
    var paymentRequest = Gen.createPaymentRequest();
    var paymentResponse = gatewayClient.processPayment(paymentRequest);
    assertNotNull(paymentResponse);
    assertEquals(paymentRequest.getAmount(), paymentResponse.getAmount());
    assertEquals(paymentRequest.getCurrency(), paymentResponse.getCurrency());
    assertEquals(paymentRequest.getLastFour(), paymentResponse.getCardNumberLastFour());

    var getPaymentResponse = gatewayClient.getPayment(paymentResponse.getId());
    assertNotNull(getPaymentResponse);
    assertEquals(paymentResponse.getId(), getPaymentResponse.getId());
    assertEquals(paymentResponse.getAmount(), getPaymentResponse.getAmount());
    assertEquals(paymentResponse.getCurrency(), getPaymentResponse.getCurrency());
    assertEquals(paymentResponse.getCardNumberLastFour(), getPaymentResponse.getCardNumberLastFour());

  }

}
