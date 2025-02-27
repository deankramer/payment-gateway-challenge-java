package com.checkout.payment.gateway.client;

import com.checkout.payment.gateway.model.HealthResponse;
import com.checkout.payment.gateway.model.PostPaymentRequest;
import com.checkout.payment.gateway.model.PostPaymentResponse;
import org.springframework.web.client.RestTemplate;
import java.util.UUID;

public class GatewayClientImpl extends BaseClient implements GatewayClient {

  private static final String PAYMENT = "/payment";
  private static final String PAYMENT_= "/payment/";

  public GatewayClientImpl(String baseUrl, RestTemplate client) {
    super(baseUrl, client);
  }

  public PostPaymentResponse getPayment(UUID id) {
    return get(PAYMENT_ + id, PostPaymentResponse.class);
  }

  public PostPaymentResponse processPayment(PostPaymentRequest paymentRequest) {
    return post(PAYMENT, paymentRequest, PostPaymentResponse.class);
  }

  public HealthResponse getHealth() {
    return get("/health", HealthResponse.class);
  }
}
