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
    var violations = testCardNumber(cardNumber);
    assertEquals(1, violations.size());
    assertEquals(INVALID, violations.iterator().next().getMessage());
  }

  @ParameterizedTest
  @ValueSource(strings = {"40000000000002", "50000000000009", "5045670000000006", "4042424242424242422"})
  public void whenCardNumberIsValidatedValid(String cardNumber) {
    var violations = testCardNumber(cardNumber);
    assertEquals(0, violations.size());
  }

  @ParameterizedTest
  @ValueSource(strings = {"", "12", "a", "1a21", "12345"})
  public void whenCvvIsValidatedInvalid(String cvv) {
    var violations = testCvv(cvv);
    assertEquals(1, violations.size());
    assertEquals(INVALID, violations.iterator().next().getMessage());
  }

  @ParameterizedTest
  @ValueSource(strings = {"123", "1234"})
  public void whenCvvIsValidatedValid(String cvv) {
    var violations = testCvv(cvv);
    assertEquals(0, violations.size());
  }

  @ParameterizedTest
  @ValueSource(ints = {-1, 13, 100})
  public void whenMonthIsValidatedInvalid(int month) {
    var violations = testMonth(month);
    assertEquals(1, violations.size());
    assertEquals(INVALID, violations.iterator().next().getMessage());
  }

  @ParameterizedTest
  @ValueSource(ints = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12})
  public void whenMonthIsValidatedValid(int month) {
    var violations = testMonth(month);
    assertEquals(0, violations.size());
  }

  @ParameterizedTest
  @ValueSource(ints = {1900, 1950, 2000, 2020, 2024})
  public void whenYearIsValidatedInvalid(int year) {
    var violations = testYear(year);
    assertEquals(1, violations.size());
  }

  @ParameterizedTest
  @ValueSource(ints = {2025, 2026, 2027, 2028})
  public void whenYearIsValidatedValid(int year) {
    var violations = testYear(year);
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

  private Set<ConstraintViolation<PostPaymentRequest>> testCardNumber(String cardNumber) {
    var currentDate = now();
    var testPaymentRequest = createPaymentRequestDTO(
        cardNumber,
        currentDate.getMonthValue(),
        currentDate.getYear(),
        "123",
        "USD",
        100);
    return validator.validate(testPaymentRequest);
  }

  private Set<ConstraintViolation<PostPaymentRequest>> testCvv(String cvv) {
    var currentDate = now();
    var testPaymentRequest = createPaymentRequestDTO(
        "4000000000000002",
        currentDate.getMonthValue(),
        currentDate.getYear(),
        cvv,
        "USD",
        100);
    return validator.validate(testPaymentRequest);
  }

  private Set<ConstraintViolation<PostPaymentRequest>> testMonth(int month) {
    var currentDate = now();
    var testPaymentRequest = createPaymentRequestDTO(
        "4000000000000002",
        month,
        currentDate.getYear() + 1,
        "123",
        "USD",
        100);
    return validator.validate(testPaymentRequest);
  }

  private Set<ConstraintViolation<PostPaymentRequest>> testYear(int year) {
    var currentDate = now();
    var testPaymentRequest = createPaymentRequestDTO(
        "4000000000000002",
        currentDate.getMonthValue(),
        year,
        "123",
        "USD",
        100);
    return validator.validate(testPaymentRequest);
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
