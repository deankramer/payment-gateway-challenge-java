package com.checkout.payment.gateway.util;

import static com.checkout.payment.gateway.util.LoggerContext.*;
import static com.checkout.payment.gateway.util.LoggerContext.Direction.IN;
import static com.checkout.payment.gateway.util.LoggerContext.setRequestContext;
import static com.checkout.payment.gateway.util.LoggerContext.setResponseContext;
import static org.slf4j.LoggerFactory.getLogger;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.slf4j.Logger;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RequestFilter implements Filter {

  private static final Logger logger = getLogger(RequestFilter.class);

  @Override
  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
      FilterChain filterChain) throws IOException, ServletException {
    var request = (HttpServletRequest) servletRequest;
    var path = request.getRequestURI();
    var method = request.getMethod();
    var context = setRequestContext(method, IN);
    logger.info(path);
    filterChain.doFilter(servletRequest, servletResponse);
    var response = (HttpServletResponse) servletResponse;
    setResponseContext(method, IN, response.getStatus());
    logger.info(path);
    clear();
  }
}
