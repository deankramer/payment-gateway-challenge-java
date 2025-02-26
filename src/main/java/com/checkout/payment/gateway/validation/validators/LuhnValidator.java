package com.checkout.payment.gateway.validation.validators;

import com.checkout.payment.gateway.validation.annotations.ValidPan;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import static java.lang.Integer.parseInt;

public class LuhnValidator implements ConstraintValidator<ValidPan, String> {

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    if (value == null || value.isEmpty()) {
      return false;
    }
    return isLuhnValid(value);
  }


  private boolean isLuhnValid(String value) {
    int sum = 0;
    boolean alternate = false;
    for (var i = value.length() - 1; i >= 0; i--) {
      int n = parseInt(value.substring(i, i + 1));
      if (alternate) {
        n *= 2;
        if (n > 9) {
          n -= 9;
        }
      }
      sum += n;
      alternate = !alternate;
    }
    return (sum % 10 == 0);
  }

}
