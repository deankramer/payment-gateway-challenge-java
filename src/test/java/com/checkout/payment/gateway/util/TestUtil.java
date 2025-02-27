package com.checkout.payment.gateway.util;

import com.checkout.payment.gateway.util.Util.A;
import com.checkout.payment.gateway.util.Util.F;
import com.checkout.payment.gateway.util.Util.F1;
import org.slf4j.MDC;

import java.util.UUID;

import static java.awt.Color.blue;
import static java.lang.String.format;
import static java.lang.System.*;
import static java.lang.System.out;

public class TestUtil {

  private static final String ANSI_RESET = "\u001B[0m";
  private static final String ANSI_RED = "\u001B[31m";
  private static final String ANSI_GREEN = "\u001B[32m";
  private static final String ANSI_BLUE = "\u001B[34m";
  private static final String ANSI_PURPLE = "\u001B[35m";

  public static String colour(String colour, Object object) {
    return colour + object.toString() + ANSI_RESET;
  }

  public static String blue(Object object) {
    return colour(ANSI_BLUE, object);
  }

  public static String purple(Object object) {
    return colour(ANSI_PURPLE, object);
  }

  public static String red(Object object) {
    return colour(ANSI_RED, object);
  }

  public static String green(Object object) {
    return colour(ANSI_GREEN, object);
  }

  private static <R> R element(F1<String, String> colour, int indent, String name, F<R> f) {
    MDC.put("cr", UUID.randomUUID().toString());
    var tabName = indent == 0 ?
        name :
        (" ".repeat((indent - 1) * 2) + (indent > 0 ? "- " : "") + name);
    out.println(colour.x(tabName + "..."));
    R r;
    var then = currentTimeMillis();
    var now = 0L;
    try {
      r = f.x();
      now = currentTimeMillis();
    }
    catch (Throwable t) {
      out.println(red(tabName + " failed"));
      throw t;
    }
    out.println(green(tabName + " done (in " + format("%.3f", (now - then) / 1000.0d) + "s)"));
    MDC.clear();
    return r;
  }

  private static void element(F1<String, String> colour, int indent, String name, A a) {
    element(
        colour,
        indent,
        name,
        () -> {
          a.x();
          return 0;
        });
  }

  public static  <R> R section(String name, F<R> f) {
    return element(TestUtil::blue, 0, name, f);
  }

  public static void section(String name, A a) {
    element(TestUtil::blue, 0, name, a);
  }

}
