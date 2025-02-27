package com.checkout.payment.gateway.model;

import java.io.Serializable;

public class HealthResponse implements Serializable {
  private boolean dbStatus;
  private boolean acquirerStatus;

  public HealthResponse() {
  }

  public HealthResponse(boolean dbStatus, boolean acquirerStatus) {
    this.dbStatus = dbStatus;
    this.acquirerStatus = acquirerStatus;
  }

  public boolean getDbStatus() {
    return dbStatus;
  }

  public void setDbStatus(boolean dbStatus) {
    this.dbStatus = dbStatus;
  }

  public boolean getAcquirerStatus() {
    return acquirerStatus;
  }

  public void setAcquirerStatus(boolean acquirerStatus) {
    this.acquirerStatus = acquirerStatus;
  }
}
