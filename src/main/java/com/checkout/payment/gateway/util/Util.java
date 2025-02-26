package com.checkout.payment.gateway.util;

public class Util {

  public static class Field<K, V> {
    public final K key;
    public final V value;

    public Field(K _key, V _value) {
      key = _key;
      value = _value;
    }
  }

  public static <K,V> Field<K,V> to(K key, V value) {
    return new Field<>(key, value);
  }

}
