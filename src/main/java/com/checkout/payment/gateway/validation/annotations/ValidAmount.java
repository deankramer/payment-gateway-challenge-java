package com.checkout.payment.gateway.validation.annotations;

import com.checkout.payment.gateway.validation.validators.AmountValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = AmountValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidAmount {
  String message() default "Amount must be a positive number";
  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};
}
