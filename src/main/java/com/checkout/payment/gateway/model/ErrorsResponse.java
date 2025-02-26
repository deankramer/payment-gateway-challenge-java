package com.checkout.payment.gateway.model;

import java.util.List;

public class ErrorsResponse extends ErrorResponse {

  public static class FieldError {
    private final String field;
    private final String message;

    public FieldError(String field, String message) {
      this.field = field;
      this.message = message;
    }

    public String getField() {
      return field;
    }

    public String getMessage() {
      return message;
    }
  }

  private final List<FieldError> fields;
  private final List<String> messages;

  public ErrorsResponse(String message, List<FieldError> fields, List<String> messages) {
    super(message);
    this.fields = fields;
    this.messages = messages;
  }

  public List<FieldError> getFields() {
    return fields;
  }

  public List<String> getMessages() {
    return messages;
  }
}