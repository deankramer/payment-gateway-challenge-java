package com.checkout.payment.gateway.util;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;

public enum Currency {

  //Can extend this enum to include more currencies
  EURO("EUR", "978", "€"),
  GREAT_BRITISH_POUND("GBP", "826", "£"),
  US_DOLLAR("USD", "840", "$");

  private static Map<String, Currency> alphas;
  private static Map<String, Currency> nums;
  private final String alpha;
  private final String num;
  private final String symbol;

  static {
    alphas = java.util.Arrays.stream(Currency.class.getEnumConstants()).collect(
        toMap(Currency::getAlpha, currency -> currency));
    nums = Arrays.stream(Currency.class.getEnumConstants()).collect(
        toMap(Currency::getNum, currency -> currency));
  }

  Currency(String alpha, String num, String symbol) {
    this.alpha = alpha;
    this.num = num;
    this.symbol = symbol;
  }

  public static Currency getByAlpha(String alpha) {
    return alphas.get(alpha);
  }

  public static Currency getByNum(String num) {
    return nums.get(num);
  }

  public static String alphaFromNum(String num) {
    return getByAlpha(num).getAlpha();
  }

  public String getAlpha() {
    return alpha;
  }

  public String getNum() {
    return num;
  }

  public String getSymbol() {
    return symbol;
  }

}
