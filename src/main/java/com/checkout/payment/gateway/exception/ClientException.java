package com.checkout.payment.gateway.exception;

public class ClientException extends RuntimeException {

  private final String path;
  private final int status;

  public ClientException(String path, int status, String message, Throwable cause) {
    super(message, cause);
    this.path = path;
    this.status = status;
  }

  public ClientException(String path, int status, String message) {
    this(path, status, message, null);
  }

  public boolean clientError() {
    return status >= 400 && status < 500;
  }

  public boolean serverError() {
    return status >= 500;
  }

  public int getStatus() {
    return status;
  }

  public String toString() {
    return path + " " + status + ":\n" + getMessage();
  }
}
