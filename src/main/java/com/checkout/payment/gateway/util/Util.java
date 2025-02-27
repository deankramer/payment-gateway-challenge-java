package com.checkout.payment.gateway.util;

public class Util {

  public interface F1<R, T1> { R x(T1 t1); }

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

  public static <T, R> R maybe(T t, F1<R, T> f) {
    return t == null ? null : f.x(t);
  }

}
