package com.checkout.payment.gateway.unit.controller;

import static com.checkout.payment.gateway.gen.Gen.createPaymentRequest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.checkout.payment.gateway.model.PostPaymentResponse;
import com.checkout.payment.gateway.repository.PaymentsRepository;
import com.checkout.payment.gateway.unit.Configuration;
import com.checkout.payment.gateway.unit.client.AcquirerMockClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

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
