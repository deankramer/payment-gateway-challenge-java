package com.checkout.payment.gateway.util;

public class Util {

  public interface A { void x(); }

  public interface F<R> { R x(); }
  public interface FX<R> { R x() throws Exception; }
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

  public static <T> T xf(FX<T> f) {
    try {
      return f.x();
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }

}
