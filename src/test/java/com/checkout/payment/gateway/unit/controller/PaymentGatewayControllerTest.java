package com.checkout.payment.gateway.unit.controller;

import static com.checkout.payment.gateway.enums.PaymentStatus.AUTHORIZED;
import static com.checkout.payment.gateway.enums.PaymentStatus.DECLINED;
import static com.checkout.payment.gateway.exception.CommonExceptionHandler.INVALID_REQUEST;
import static com.checkout.payment.gateway.exception.CommonExceptionHandler.PAYMENT_NOT_FOUND;
import static com.checkout.payment.gateway.model.PostPaymentRequest.INVALID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.checkout.payment.gateway.model.PostPaymentRequest;
import com.checkout.payment.gateway.model.PostPaymentResponse;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


class PaymentGatewayControllerTest extends BaseTest {

  @Test
  void whenPaymentWithIdExistThenCorrectPaymentIsReturned() throws Exception {
    PostPaymentResponse payment = new PostPaymentResponse();
    payment.setId(UUID.randomUUID());
    payment.setAmount(10);
    payment.setCurrency("USD");
    payment.setStatus(AUTHORIZED);
    payment.setExpiryMonth(12);
    payment.setExpiryYear(2024);
    payment.setCardNumberLastFour("4321");

    paymentsRepository.add(payment);

    mvc.perform(MockMvcRequestBuilders.get("/payment/" + payment.getId()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value(payment.getStatus().getName()))
        .andExpect(jsonPath("$.cardNumberLastFour").value(payment.getCardNumberLastFour()))
        .andExpect(jsonPath("$.expiryMonth").value(payment.getExpiryMonth()))
        .andExpect(jsonPath("$.expiryYear").value(payment.getExpiryYear()))
        .andExpect(jsonPath("$.currency").value(payment.getCurrency()))
        .andExpect(jsonPath("$.amount").value(payment.getAmount()));
  }

  @Test
  void whenPaymentWithIdDoesNotExistThen404IsReturned() throws Exception {
    mvc.perform(MockMvcRequestBuilders.get("/payment/" + UUID.randomUUID()))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.messages").value(PAYMENT_NOT_FOUND));
  }

  @Test
  void whenPaymentIsAuthorizedThen201IsReturned() throws Exception {
    acquirerClient.setAuthorized(true);
    var response = createAndCheckPayment();
    assertEquals(AUTHORIZED, response.getStatus());
  }

  @Test
  void whenPaymentIsDeclinedThen201IsReturned() throws Exception {
    acquirerClient.setAuthorized(false);
    var response = createAndCheckPayment();
    assertEquals(DECLINED, response.getStatus());
  }

  @Test
  void whenPaymentIsRejectedThen400IsReturned() throws Exception {
    var paymentRequest = new PostPaymentRequest();
    paymentRequest.setCardNumber("2");
    paymentRequest.setCvv("123");
    paymentRequest.setExpiryMonth(1);
    paymentRequest.setExpiryYear(2026);
    paymentRequest.setAmount(100);
    paymentRequest.setCurrency("GBP");
    mvc.perform(MockMvcRequestBuilders.post("/payment")
            .contentType("application/json")
            .content(toJsonBody(paymentRequest)))
        .andExpect(jsonPath("$.message").value(INVALID_REQUEST))
        .andExpect(jsonPath("$.fields[0].field").value("cardNumber"))
        .andExpect(jsonPath("$.fields[0].message").value(INVALID))
        .andExpect(status().is4xxClientError());
  }

}
