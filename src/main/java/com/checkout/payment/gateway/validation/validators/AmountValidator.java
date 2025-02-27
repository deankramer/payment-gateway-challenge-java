package com.checkout.payment.gateway.validation.validators;

import com.checkout.payment.gateway.validation.annotations.ValidAmount;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class AmountValidator implements ConstraintValidator<ValidAmount, Integer> {

  @Override
  public boolean isValid(Integer value, ConstraintValidatorContext context) {
    return value != null && value > 0;
  }
}
