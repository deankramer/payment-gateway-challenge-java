package com.checkout.payment.gateway.validation.annotations;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.ReportAsSingleViolation;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@NotNull()
@Min(value = 1)
@Max(value = 12)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@Retention(RUNTIME)
@Repeatable(ValidMonth.List.class)
@Constraint(validatedBy = {})
@Documented
@ReportAsSingleViolation
public @interface ValidMonth {

  String message() default "Invalid month";
  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  @Target({ANNOTATION_TYPE})
  @Retention(RUNTIME)
  @Documented
  @interface List {
    ValidMonth[] value();
  }

}
