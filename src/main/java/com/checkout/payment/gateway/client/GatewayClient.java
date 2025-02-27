package com.checkout.payment.gateway.client;

import com.checkout.payment.gateway.model.HealthResponse;
import com.checkout.payment.gateway.model.PostPaymentRequest;
import com.checkout.payment.gateway.model.PostPaymentResponse;
import java.util.UUID;

public interface GatewayClient {

  PostPaymentResponse getPayment(UUID id);

  PostPaymentResponse processPayment(PostPaymentRequest paymentRequest);

  HealthResponse getHealth();
}
