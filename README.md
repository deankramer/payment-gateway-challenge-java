# Instructions for candidates

This is the Java version of the Payment Gateway challenge. If you haven't already read this [README.md](https://github.com/cko-recruitment/) on the details of this exercise, please do so now.

## Requirements
- JDK 17
- Docker

## Template structure

src/ - A skeleton SpringBoot Application

test/ - Some simple JUnit tests

imposters/ - contains the bank simulator configuration. Don't change this

.editorconfig - don't change this. It ensures a consistent set of rules for submissions when reformatting code

docker-compose.yml - configures the bank simulator


## API Documentation
For documentation openAPI is included, and it can be found under the following url: **http://localhost:8090/swagger-ui/index.html**

## Usage

### Create payment endpoint
POST http://localhost:8090/payment

**Example request**
```json
{
  "cardNumber": "2222405343248877",
  "cvv": "123",
  "expiryMonth": 4,
  "expiryYear": 2026,
  "currency": "GBP",
  "amount": 100
}

```

**Example response**
```json
{
  "id": "5a5dab4d-b07c-41a9-a1a8-f7c96c609cb5",
  "status": "Authorized",
  "cardNumberLastFour": "8877",
  "expiryMonth": 4,
  "expiryYear": 2025,
  "currency": "GBP",
  "amount": 100
}
```

### Get payment endpoint
POST http://localhost:8090/payment/{{paymentId}}

**Example response**
```json
{
  "id": "19f3c9f1-1384-45c2-ac36-b41f90111446",
  "status": "Authorized",
  "cardNumberLastFour": "8877",
  "expiryMonth": 4,
  "expiryYear": 2025,
  "currency": "GBP",
  "amount": 100
}
```