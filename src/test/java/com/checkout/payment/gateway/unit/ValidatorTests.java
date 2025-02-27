package com.checkout.payment.gateway.unit;

import static com.checkout.payment.gateway.model.PostPaymentRequest.INVALID;
import static jakarta.validation.Validation.buildDefaultValidatorFactory;
import static java.time.LocalDate.now;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.checkout.payment.gateway.model.PostPaymentRequest;
import com.checkout.payment.gateway.validation.annotations.ValidUUID;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.validation.executable.ExecutableValidator;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class ValidatorTests {

  private Validator validator;
  private ExecutableValidator executableValidator;

  @BeforeEach
  public void setUp() {
    validator = buildDefaultValidatorFactory().getValidator();
    executableValidator = validator.forExecutables();
  }

  @ParameterizedTest
  @ValueSource(strings = {"", "1234", "1234567890123", "12345678901234567890"})
  public void whenCardNumberIsValidatedInvalid(String cardNumber) {
    var now = now();
    var violations = validatePayment(cardNumber, now.getMonthValue(), now.getYear(), "123", "USD", 100);
    assertEquals(1, violations.size());
    assertEquals(INVALID, violations.iterator().next().getMessage());
  }

  @ParameterizedTest
  @ValueSource(strings = {"40000000000002", "50000000000009", "5045670000000006", "4042424242424242422"})
  public void whenCardNumberIsValidatedValid(String cardNumber) {
    var now = now();
    var violations = validatePayment(cardNumber, now.getMonthValue(), now.getYear(), "123", "USD", 100);
    assertEquals(0, violations.size());
  }

  @ParameterizedTest
  @ValueSource(strings = {"", "12", "a", "1a21", "12345"})
  public void whenCvvIsValidatedInvalid(String cvv) {
    var now = now();
    var violations = validatePayment("40000000000002", now.getMonthValue(), now.getYear(), cvv, "USD", 100);
    assertEquals(1, violations.size());
    assertEquals(INVALID, violations.iterator().next().getMessage());
  }

  @ParameterizedTest
  @ValueSource(strings = {"123", "1234"})
  public void whenCvvIsValidatedValid(String cvv) {
    var now = now();
    var violations = validatePayment("40000000000002", now.getMonthValue(), now.getYear(), cvv, "USD", 100);
    assertEquals(0, violations.size());
  }

  @ParameterizedTest
  @ValueSource(ints = {-1, 13, 100})
  public void whenMonthIsValidatedInvalid(int month) {
    var now = now();
    var violations = validatePayment("40000000000002", month, now.getYear(), "123", "USD", 100);
    assertEquals(1, violations.size());
    assertEquals(INVALID, violations.iterator().next().getMessage());
  }

  @ParameterizedTest
  @ValueSource(ints = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12})
  public void whenMonthIsValidatedValid(int month) {
    var now = now();
    //Increment year to avoid invalid expiry date violation
    var violations = validatePayment("40000000000002", month, now.getYear()+1, "123", "USD", 100);
    assertEquals(0, violations.size());
  }

  @ParameterizedTest
  @ValueSource(ints = {1900, 1950, 2000, 2020, 2024})
  public void whenYearIsValidatedInvalid(int year) {
    var now = now();
    var violations = validatePayment("40000000000002", now.getMonthValue(), year, "123", "USD", 100);
    assertEquals(1, violations.size());
  }

  @ParameterizedTest
  @ValueSource(ints = {2025, 2026, 2027, 2028})
  public void whenYearIsValidatedValid(int year) {
    var now = now();
    var violations = validatePayment("40000000000002", now.getMonthValue(), year, "123", "USD", 100);
    assertEquals(0, violations.size());
  }

  @ParameterizedTest
  @ValueSource(ints = {-100, -1, 0})
  public void whenAmountIsValidatedInvalid(int amount) {
    var now = now();
    var violations = validatePayment("40000000000002", now.getMonthValue(), now.getYear(), "123", "USD", amount);
    assertEquals(1, violations.size());
  }

  @ParameterizedTest
  @ValueSource(ints = {1, 100, 1000, 10000})
  public void whenAmountIsValidatedValid(int amount) {
    var now = now();
    var violations = validatePayment("40000000000002", now.getMonthValue(), now.getYear(), "123", "USD", amount);
    assertEquals(0, violations.size());
  }

  @ParameterizedTest
  @ValueSource(strings = {"AUD", "CZK", "NOK"})
  public void whenCurrencyIsValidatedInvalid(String currency) {
    var now = now();
    var violations = validatePayment("40000000000002", now.getMonthValue(), now.getYear(), "123", currency, 100);
    assertEquals(1, violations.size());
  }

  @ParameterizedTest
  @ValueSource(strings = {"USD", "GBP", "EUR"})
  public void whenCurrencyIsValidatedValid(String currency) {
    var now = now();
    var violations = validatePayment("40000000000002", now.getMonthValue(), now.getYear(), "123", currency, 100);
    assertEquals(0, violations.size());
  }

  @ParameterizedTest
  @ValueSource(strings = {"", "1234asad", "1234567890123AAAAAAAAAAAAAA", "ABABABABABA"})
  public void whenUUIDIsValidatedInvalid(String uuid) {
    testUUID(uuid, 1);
  }

  @ParameterizedTest
  @ValueSource(strings = {"550e8400-e29b-41d4-a716-446655440000", "550e8400-e29b-41d4-a716-4466554400"})
  public void whenUUIDIsValidatedValid(String uuid) {
    testUUID(uuid, 0);
  }

  public void testUUID(@ValidUUID String uuid) {
  }

  private Set<ConstraintViolation<PostPaymentRequest>> validatePayment(
          String cardNumber,
          int expiryMonth,
          int expiryYear,
          String cvv,
          String currency,
          int amount) {
    return validator.validate(createPaymentRequestDTO(cardNumber, expiryMonth, expiryYear, cvv, currency, amount));
  }

  private void testUUID(String uuid, int expectedViolations) {
    try {
      var violations = executableValidator.validateParameters(
          this, ValidatorTests.class.getMethod("testUUID", String.class), new Object[]{uuid});
      assertEquals(expectedViolations, violations.size());
    } catch (NoSuchMethodException e) {
      e.printStackTrace();
    }
  }

  public static PostPaymentRequest createPaymentRequestDTO(
      String cardNumber,
      int expiryMonth,
      int expiryYear,
      String cvv,
      String currency,
      int amount) {
    var postPaymentRequest = new PostPaymentRequest();
    postPaymentRequest.setCardNumber(cardNumber);
    postPaymentRequest.setExpiryMonth(expiryMonth);
    postPaymentRequest.setExpiryYear(expiryYear);
    postPaymentRequest.setCvv(cvv);
    postPaymentRequest.setCurrency(currency);
    postPaymentRequest.setAmount(amount);
    return postPaymentRequest;
  }

}
