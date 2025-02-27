package com.checkout.payment.gateway.service;

import com.checkout.payment.gateway.client.AcquirerClient;
import com.checkout.payment.gateway.client.AcquirerClient.AcquirerPaymentRequest;
import com.checkout.payment.gateway.client.AcquirerClient.AcquirerPaymentResponse;
import com.checkout.payment.gateway.enums.PaymentStatus;
import com.checkout.payment.gateway.exception.CommonExceptionHandler.MissingEntityException;
import com.checkout.payment.gateway.exception.EventProcessingException;
import com.checkout.payment.gateway.model.PostPaymentRequest;
import com.checkout.payment.gateway.model.PostPaymentResponse;
import com.checkout.payment.gateway.repository.PaymentsRepository;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import static com.checkout.payment.gateway.enums.PaymentStatus.*;
import static com.checkout.payment.gateway.exception.CommonExceptionHandler.PAYMENT_NOT_FOUND;
import static java.lang.Integer.parseInt;

@Service
public class PaymentGatewayService {

  private static final Logger LOG = LoggerFactory.getLogger(PaymentGatewayService.class);

  private final PaymentsRepository paymentsRepository;
  private final AcquirerClient acquirerClient;

  public PaymentGatewayService(PaymentsRepository paymentsRepository, AcquirerClient acquirerClient) {
    this.paymentsRepository = paymentsRepository;
    this.acquirerClient = acquirerClient;
  }

  public PostPaymentResponse getPaymentById(UUID id) {
    LOG.debug("Requesting access to payment ID: {}", id);
    return paymentsRepository.get(id).orElseThrow(() -> new MissingEntityException(PAYMENT_NOT_FOUND));
  }

  public PostPaymentResponse processPayment(PostPaymentRequest paymentRequest) {
    var paymentId = UUID.randomUUID();
    LOG.debug("Processing payment request ID: {}", paymentId);
    var acquirerResponse = acquirerClient.processPayment(mapToAcquirerPaymentRequest(paymentRequest));
    var paymentResponse = mapToPostPaymentResponse(paymentId, acquirerResponse, paymentRequest);
    LOG.debug("Payment ID: {} Status: {}", paymentId, paymentResponse.getStatus());
    paymentsRepository.add(paymentResponse);
    return paymentResponse;
  }

  private static AcquirerPaymentRequest mapToAcquirerPaymentRequest(PostPaymentRequest paymentRequest) {
    return new AcquirerPaymentRequest(
        paymentRequest.getCardNumber(),
        paymentRequest.getExpiryDate(),
        paymentRequest.getCvv(),
        paymentRequest.getCurrency(),
        paymentRequest.getAmount());
  }

  private static PostPaymentResponse mapToPostPaymentResponse(
      UUID paymentId, AcquirerPaymentResponse acquirerResponse, PostPaymentRequest paymentRequest) {
    var response = new PostPaymentResponse();
    response.setId(paymentId);
    response.setCardNumberLastFour(paymentRequest.getLastFour());
    response.setStatus(acquirerResponse.authorized() ? AUTHORIZED : DECLINED);
    response.setCurrency(paymentRequest.getCurrency());
    response.setAmount(paymentRequest.getAmount());
    var expiryDate = paymentRequest.getExpiryDate().split("/");
    response.setExpiryMonth(parseInt(expiryDate[0]));
    response.setExpiryYear(parseInt(expiryDate[1]));
    return response;
  }

}
