package com.checkout.payment.gateway.model;

import com.checkout.payment.gateway.util.Currency;
import com.checkout.payment.gateway.validation.annotations.ValidCvv;
import com.checkout.payment.gateway.validation.annotations.ValidMonth;
import com.checkout.payment.gateway.validation.annotations.ValidPan;
import com.checkout.payment.gateway.validation.annotations.ValidExpiryDate;
import com.checkout.payment.gateway.validation.annotations.ValidYear;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;

import static com.checkout.payment.gateway.model.PostPaymentRequest.INVALID_EXPIRY_DATE;

@ValidExpiryDate(message = INVALID_EXPIRY_DATE)
public class PostPaymentRequest implements Serializable {

  public static final String INVALID = "invalid";
  public static final String INVALID_EXPIRY_DATE = "Invalid expiry date";

  @ValidPan(message = INVALID)
  private String cardNumber;

  @ValidMonth(message = INVALID)
  private int expiryMonth;

  @ValidYear(message = INVALID)
  private int expiryYear;

  @NotNull(message = INVALID)
  private Currency currency;

  @NotNull(message = INVALID)
  private int amount;

  @ValidCvv(message = INVALID)
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
