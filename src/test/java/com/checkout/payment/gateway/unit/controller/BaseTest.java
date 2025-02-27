package com.checkout.payment.gateway.unit.controller;

import com.checkout.payment.gateway.model.PostPaymentRequest;
import com.checkout.payment.gateway.model.PostPaymentResponse;
import com.checkout.payment.gateway.repository.PaymentsRepository;
import com.checkout.payment.gateway.unit.Configuration;
import com.checkout.payment.gateway.unit.client.AcquirerMockClient;
import com.checkout.payment.gateway.util.Currency;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(classes = {Configuration.class})
public class BaseTest {
  @Autowired
  protected MockMvc mvc;
  @Autowired
  PaymentsRepository paymentsRepository;

  @Autowired
  AcquirerMockClient acquirerClient;


  protected PostPaymentResponse createAndCheckPayment() throws Exception {
    var request = createPaymentRequest();
    var result = mvc.perform(MockMvcRequestBuilders.post("/payment")
          .contentType("application/json")
          .content(toJsonBody(request)))
        .andExpect(status().isCreated())
        .andReturn();
    var response = fromJsonBody(result.getResponse().getContentAsString(), PostPaymentResponse.class);
    assertEquals(request.getAmount(), response.getAmount());
    assertEquals(request.getCurrency(), response.getCurrency());
    assertEquals(request.getExpiryMonth(), response.getExpiryMonth());
    assertEquals(request.getExpiryYear(), response.getExpiryYear());
    assertEquals(request.getLastFour(), response.getCardNumberLastFour());
    return fromJsonBody(result.getResponse().getContentAsString(), PostPaymentResponse.class);
  }


  protected static PostPaymentRequest createPaymentRequest() {
    var now = LocalDateTime.now();
    var paymentRequest = new PostPaymentRequest();
    paymentRequest.setCardNumber("4242424242424242");
    paymentRequest.setCvv("123");
    paymentRequest.setExpiryMonth(now.getMonthValue());
    paymentRequest.setExpiryYear(now.getYear());
    paymentRequest.setAmount(100);
    paymentRequest.setCurrency(Currency.GREAT_BRITISH_POUND.getAlpha());
    return paymentRequest;
  }

  protected <T> T fromJsonBody(String body, Class<T> clazz) throws Exception {
    var mapper = new ObjectMapper();
    return mapper.readValue(body, clazz);
  }

  protected String toJsonBody(Object obj) throws Exception {
    var mapper = new ObjectMapper();
    mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
    return mapper.writer().withDefaultPrettyPrinter().writeValueAsString(obj);
  }

}
