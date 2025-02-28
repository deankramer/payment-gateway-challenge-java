package com.checkout.payment.gateway.validation.validators;

import static java.time.LocalDate.now;
import static java.time.LocalDate.of;

import com.checkout.payment.gateway.model.PostPaymentRequest;
import com.checkout.payment.gateway.validation.annotations.ValidExpiryDate;
import jakarta.validation.ConstraintValidator;


public class ExpiryDateValidator implements ConstraintValidator<ValidExpiryDate, PostPaymentRequest> {

  @Override
  public boolean isValid(PostPaymentRequest postPaymentRequest, jakarta.validation.ConstraintValidatorContext context) {

    int year = postPaymentRequest.getExpiryYear();
    int month = postPaymentRequest.getExpiryMonth();

    if (year < 2025 || month < 1 || month > 12) {
      return true;
    }
    var cardDate = of(postPaymentRequest.getExpiryYear(), postPaymentRequest.getExpiryMonth(), 1)
        .plusMonths(1)
        .minusDays(1);
    return !now().isAfter(cardDate);
  }

}
