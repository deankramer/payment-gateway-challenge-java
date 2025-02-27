package com.checkout.payment.gateway.unit.controller;


import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class HealthControllerTest extends BaseTest {

  @Test
  void whenHealthCheckSuccessAnd200Returned() throws Exception {
    mvc.perform(MockMvcRequestBuilders.get("/health"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.dbStatus").value(true))
        .andExpect(jsonPath("$.acquirerStatus").value(true));
  }

}
