package com.checkout.payment.gateway.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.UUID;

public interface AcquirerClient {

  record AcquirerPaymentResponse(boolean authorized, UUID authorizationCode) {}

  record AcquirerPaymentRequest(
      @JsonProperty("card_number") String cardNumber,
      @JsonProperty("expiry_date") String expiryDate,
      String cvv,
      String currency,
      int amount) {}

  AcquirerPaymentResponse processPayment(AcquirerPaymentRequest request);
  boolean healthCheck();

}
