package com.checkout.payment.gateway.model;

import com.checkout.payment.gateway.util.Currency;
import com.checkout.payment.gateway.validation.annotations.ValidCvv;
import com.checkout.payment.gateway.validation.annotations.ValidPan;
import com.checkout.payment.gateway.validation.annotations.ValidExpiryDate;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;

@ValidExpiryDate
public class PostPaymentRequest implements Serializable {

  public static final String INVALID_CARD_NUMBER = "Invalid card number";
  public static final String INVALID_EXPIRY_DATE = "Invalid expiry date";
  public static final String INVALID_CURRENCY = "Invalid currency";
  public static final String INVALID_AMOUNT = "Invalid amount";
  public static final String INVALID_CVV = "Invalid CVV";

  @JsonProperty("card_number")
  @ValidPan(message = INVALID_CARD_NUMBER)
  private String cardNumber;

  @JsonProperty("expiry_month")
  private int expiryMonth;

  @JsonProperty("expiry_year")
  private int expiryYear;

  @NotNull(message = INVALID_CURRENCY)
  private Currency currency;

  @NotNull(message = INVALID_AMOUNT)
  private int amount;

  @ValidCvv(message = INVALID_CVV)
  private String cvv;

  public String getCardNumber() {
    return cardNumber;
  }

  public void setCardNumber(String cardNumber) {
    this.cardNumber = cardNumber;
  }

  public int getExpiryMonth() {
    return expiryMonth;
  }

  public void setExpiryMonth(int expiryMonth) {
    this.expiryMonth = expiryMonth;
  }

  public int getExpiryYear() {
    return expiryYear;
  }

  public void setExpiryYear(int expiryYear) {
    this.expiryYear = expiryYear;
  }

  public Currency getCurrency() {
    return currency;
  }

  public void setCurrency(String currency) {
    this.currency = Currency.getByAlpha(currency);
  }

  public int getAmount() {
    return amount;
  }

  public void setAmount(int amount) {
    this.amount = amount;
  }

  public String getCvv() {
    return cvv;
  }

  public void setCvv(String cvv) {
    this.cvv = cvv;
  }

  @JsonIgnore
  public String getExpiryDate() {
    return String.format("%d/%d", expiryMonth, expiryYear);
  }

  @Override
  public String toString() {
    return "PostPaymentRequest{" +
        "cardNumber=" + cardNumber +
        ", expiryMonth=" + expiryMonth +
        ", expiryYear=" + expiryYear +
        ", currency='" + currency + '\'' +
        ", amount=" + amount +
        ", cvv=" + cvv +
        '}';
  }
}
