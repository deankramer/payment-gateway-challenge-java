package com.checkout.payment.gateway.validation.annotations;

import com.checkout.payment.gateway.validation.validators.LuhnValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.ReportAsSingleViolation;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@NotNull()
@NotBlank()
@Size(min = 3, max = 4)
@Pattern(regexp = "\\d+")
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@Retention(RUNTIME)
@Repeatable(ValidCvv.List.class)
@Constraint(validatedBy = {})
@Documented
@ReportAsSingleViolation
public @interface ValidCvv {

  String message() default "{ValidCvv.message}";
  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  @Target({ANNOTATION_TYPE})
  @Retention(RUNTIME)
  @Documented
  @interface List {
    ValidCvv[] value();
  }

}
