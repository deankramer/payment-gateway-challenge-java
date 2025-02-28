package com.checkout.payment.gateway.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import com.checkout.payment.gateway.model.ErrorResponse;
import com.checkout.payment.gateway.model.ErrorsResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class CommonExceptionHandler {

  public static final String INTERNAL_SERVER_ERROR_MESSAGE = "Internal server error";
  public static final String INVALID_REQUEST = "Invalid request";
  public static final String PAYMENT_NOT_FOUND = "Payment not found";

  private static final Logger LOG = LoggerFactory.getLogger(CommonExceptionHandler.class);

  public static class MissingEntityException extends RuntimeException {
    public MissingEntityException(String message) {
      super(message);
    }
  }

  @ExceptionHandler(EventProcessingException.class)
  public ResponseEntity<ErrorResponse> handleException(EventProcessingException ex) {
    LOG.error("Exception happened", ex);
    return new ResponseEntity<>(new ErrorResponse(ex.getMessage()),
        NOT_FOUND);
  }

  @ExceptionHandler(MissingEntityException.class)
  public ResponseEntity<ErrorResponse> handleException(MissingEntityException ex) {
    LOG.error("Entity not found", ex);
    return new ResponseEntity<>(new ErrorsResponse(INVALID_REQUEST, List.of(), List.of(ex.getMessage())), NOT_FOUND);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  @ResponseStatus(BAD_REQUEST)
  @ApiResponses(value = {
      @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content(schema = @Schema(implementation = ErrorsResponse.class)))
  })
  public @ResponseBody
  ResponseEntity<ErrorResponse> validationError(ConstraintViolationException ex) {
    var violations = ex.getConstraintViolations();
    var fieldErrorList = new ArrayList<ErrorsResponse.FieldError>();
    var errorList = new ArrayList<String>();
    violations.forEach(error -> {
      var field = error.getPropertyPath().toString();
      var message = error.getMessage();
      fieldErrorList.add(new ErrorsResponse.FieldError(field, message));
    });
    return new ResponseEntity<>(new ErrorsResponse(INVALID_REQUEST, fieldErrorList, errorList), BAD_REQUEST);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseBody
  @ApiResponses(value = {
      @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content(schema = @Schema(implementation = ErrorsResponse.class)))
  })
  public ResponseEntity<ErrorResponse> validationError(MethodArgumentNotValidException ex) {
    var fieldErrorList = new ArrayList<ErrorsResponse.FieldError>();
    var errorList = new ArrayList<String>();
    ex.getBindingResult().getAllErrors().forEach(error -> {
      if (error instanceof FieldError) {
        var field = ((FieldError) error).getField();
        var message = error.getDefaultMessage();
        fieldErrorList.add(new ErrorsResponse.FieldError(field, message));
      } else {
        errorList.add(error.getDefaultMessage());
      }
    });

    return new ResponseEntity<>(new ErrorsResponse(INVALID_REQUEST, fieldErrorList, errorList), BAD_REQUEST);
  }

  @ExceptionHandler(Throwable.class)
  @ResponseStatus(INTERNAL_SERVER_ERROR)
  public @ResponseBody
  ResponseEntity<ErrorResponse> handleException(Throwable t) {
    LOG.error(INTERNAL_SERVER_ERROR_MESSAGE, t);
    return new ResponseEntity<>(new ErrorResponse(INTERNAL_SERVER_ERROR_MESSAGE),
        INTERNAL_SERVER_ERROR);
  }


}
