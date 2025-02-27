package com.checkout.payment.gateway.service;

import static com.checkout.payment.gateway.util.Util.xf;
import static java.util.concurrent.Executors.newFixedThreadPool;

import com.checkout.payment.gateway.client.AcquirerClient;
import com.checkout.payment.gateway.model.HealthResponse;
import com.checkout.payment.gateway.repository.PaymentsRepository;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import com.checkout.payment.gateway.util.Util.F;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HealthService {

  private static final Logger LOG = LoggerFactory.getLogger(HealthService.class);
  private final PaymentsRepository paymentsRepository;
  private final ExecutorService threadPool;
  private final AcquirerClient acquirerClient;
  public static final String FAIL_PAIN = "2222463723248112";
  public static final String FAIL_DATE = "02/2028";
  public static final String FAIL_CURRENCY = "USD";
  public static final int FAIL_AMOUNT = 1;
  public static final String FAIL_CVV = "123";

  public HealthService(
      @Autowired PaymentsRepository paymentsRepository,
      @Autowired AcquirerClient acquirerClient) {
    this.paymentsRepository = paymentsRepository;
    this.acquirerClient = acquirerClient;
    threadPool = newFixedThreadPool(2);
  }

  public HealthResponse getHealth() {
    //Get the Logging Context, to be used by the threadpool
    var ctx = MDC.getCopyOfContextMap();
    var db = threadPool.submit(() -> runCheck(ctx, this::checkDb));
    var acquirer = threadPool.submit(() -> runCheck(ctx, this::checkAcquirer));
    return xf(() -> new HealthResponse(db.get(), acquirer.get()));
  }

  private static boolean runCheck(Map<String, String> ctx, F<Boolean> a) {
    MDC.setContextMap(ctx);
    return a.x();
  }

  private boolean checkDb() {
      try {
        paymentsRepository.get(UUID.randomUUID());
      } catch (Exception e) {
        LOG.error("DB Error", e);
        return false;
      }

      return true;
  }

  private boolean checkAcquirer() {
    try {
      var dummyPayment = new AcquirerClient.AcquirerPaymentRequest(
          FAIL_PAIN,
          FAIL_DATE,
          FAIL_CVV,
          FAIL_CURRENCY,
          FAIL_AMOUNT);
      //Expected Failure Case
      var response = acquirerClient.processPayment(dummyPayment);
      if (response == null) {
        return false;
      }
    } catch (Exception e) {
      LOG.error("Acquirer Error", e);
      return false;
    }
    return true;
  }

}
