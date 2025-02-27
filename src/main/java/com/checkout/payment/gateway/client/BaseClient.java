package com.checkout.payment.gateway.client;

import com.checkout.payment.gateway.exception.ClientException;
import com.checkout.payment.gateway.util.Util.Field;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;
import java.util.Map;

import static com.checkout.payment.gateway.util.LoggerContext.Direction.OUT;
import static com.checkout.payment.gateway.util.LoggerContext.setRequestContext;
import static com.checkout.payment.gateway.util.LoggerContext.setResponseContext;
import static com.checkout.payment.gateway.util.Util.maybe;
import static com.checkout.payment.gateway.util.Util.to;
import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;

public class BaseClient {

  private final String baseUrl;
  private final RestTemplate client;
  private final Map<String, String> defaultHeaders;
  private final Logger LOG = LoggerFactory.getLogger(this.getClass());

  public BaseClient(String baseUrl, RestTemplate client) {
    this.baseUrl = baseUrl;
    this.client = client;
    defaultHeaders = new HashMap<>();
    addHeaders(
        to("Content-Type", "application/json"),
        to("Accept", "application/json"));
  }

  @SafeVarargs
  public final void addHeaders(Field<String, String>... headers) {
    for (Field<String, String> header : headers) {
      defaultHeaders.put(header.key, header.value);
    }
  }

  public final void removeHeader(String key) {
    defaultHeaders.remove(key);
  }

  public <T> T get(
      String path,
      Class<T> responseType,
      Object... uriVariables) {
    return exe(path, GET, null, responseType, uriVariables);
  }

  public <T> T post(
      String path,
      Object request,
      Class<T> responseType,
      Object... uriVariables) {
    return exe(path, POST, request, responseType, uriVariables);
  }

  public <T> T put(
      String path,
      Object request,
      Class<T> responseType,
      Object... uriVariables) {
    return exe(path, PUT, request, responseType, uriVariables);
  }

  public void delete(
      String path,
      Object... uriVariables) {
    exe(path, DELETE, null, Void.class, uriVariables);
  }


  public <T> T exe(
      String path,
      HttpMethod method,
      Object request,
      Class<T> responseType,
      Object... uriVariables) {
    var httpMethod = method.name();
    setRequestContext(httpMethod, OUT);
    LOG.info(path);
    var response = client.exchange(baseUrl + path, method, entity(request), responseType, uriVariables);
    var status = response.getStatusCode().value();
    setResponseContext(httpMethod, OUT, status);
    LOG.info(path);
    if (response.getStatusCode().value() >= 300) {
      throw new ClientException(path, status, maybe(response.getBody(), Object::toString));
    }
    return response.getBody();
  }

  private <T> HttpEntity<T> entity(T request) {
    var headers = new HttpHeaders();
    for (var entry : defaultHeaders.entrySet()) {
      headers.add(entry.getKey(), entry.getValue());
    }
    return request == null ? new HttpEntity<>(headers) : new HttpEntity<>(request, headers);
  }

}
