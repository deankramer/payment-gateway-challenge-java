package com.checkout.payment.gateway.util;

import com.checkout.payment.gateway.util.Util.Field;
import org.slf4j.MDC;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.checkout.payment.gateway.util.Util.to;
import static java.lang.String.valueOf;

public class LoggerContext {

  private static final ThreadLocal<LoggerContext> threadLocalInstance = ThreadLocal.withInitial(() -> null);

  public enum Direction {
    IN, OUT
  }

  private LoggerContext(String cr) {
    setContext(to("cr", cr));
  }

  public static LoggerContext generateNewContext() {
    MDC.clear();
    var context = new LoggerContext(UUID.randomUUID().toString());
    threadLocalInstance.set(context);
    return context;
  }

  public static LoggerContext getCurrentOrNewContext() {
    var context = threadLocalInstance.get();
    if (context == null) {
      context = generateNewContext();
    }
    return context;
  }

  public static LoggerContext  setRequestContext(String method, Direction direction) {
    var context = getCurrentOrNewContext();
    setContext(
        to("ty", "REQ"),
        to("rty", direction.name()),
        to("mthd", method));
    threadLocalInstance.set(context);
    return context;
  }

  public static LoggerContext setResponseContext(String method, Direction direction, int status) {
    var context = getCurrentOrNewContext();
    setContext(
        to("ty", "RES"),
        to("rty", direction.name()),
        to("mthd", method),
        to("st", valueOf(status)));
    threadLocalInstance.set(context);
    return context;
  }

  public static String getContext(String key) {
    return MDC.get(key);
  }

  @SafeVarargs
  public static void setContext(Field<String, String>... fields) {
    for (var field : fields) {
      MDC.put(field.key, field.value);
    }
  }

  public static void removeContext(String field) {
    MDC.remove(field);
  }

  public static void clear() {
    MDC.clear();
    threadLocalInstance.remove();
  }

}
