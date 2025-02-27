package com.checkout.payment.gateway.gen;

import com.checkout.payment.gateway.model.PostPaymentRequest;
import java.time.LocalDateTime;

import static com.checkout.payment.gateway.util.Currency.GREAT_BRITISH_POUND;

public class Gen {

  public Gen() {}

  public static PostPaymentRequest createPaymentRequest() {
    var now = LocalDateTime.now();
    var paymentRequest = new PostPaymentRequest();
    paymentRequest.setCardNumber("4242424242424242");
    paymentRequest.setCvv("123");
    paymentRequest.setExpiryMonth(now.getMonthValue());
    paymentRequest.setExpiryYear(now.getYear());
    paymentRequest.setAmount(100);
    paymentRequest.setCurrency(GREAT_BRITISH_POUND.getAlpha());
    return paymentRequest;
  }

}
