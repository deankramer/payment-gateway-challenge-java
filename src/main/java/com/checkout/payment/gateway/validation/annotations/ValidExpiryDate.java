package com.checkout.payment.gateway.validation.annotations;

import com.checkout.payment.gateway.validation.validators.ExpiryDateValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = ExpiryDateValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidExpiryDate {
  String message() default "Invalid expiry date";
  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};
}
