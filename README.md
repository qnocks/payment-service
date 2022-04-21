# Payment Service

## Overview

The purpose of an application is to accept payments from a wide amount of different payment providers and broadcast the result of successfully finished payment transactions.

The solution will constantly be appended with new payment provides. The microservices architecture was chosen to minimise efforts to add new payment providers, scale load for them individually and decouple system components.

## Usage

### Requirements

* JDK 11
* Docker Engine
* Docker Compose

To run this project using gradle wrapper set up docker container and pass the environment variables for auth. \
**Note:** *auth.jwt.secret* has to be large then 265 bytes

```
$ cd /payment-service
$ ./gradlew build
$  docker-compose up
$ ./gradlew bootRun --args='--auth.jwt.secret=secret --auth.jwt.expired=5000 --auth.refresh.expired=50000'    
```

## Design

### Component Diagram

![Core Payment Gateway-Components](./docs/Core%20Payment%20Gateway-Components.drawio.png)

**Payment Service**

The core application of a Payment Gateway. Responsible for management and lifecycle of transactions for every type of payment provider.

**Payment Service Adapter**

Adapter, that encapsulates the business flow of a distinct payment service and implements it using Payment Service's API\
The transaction flow of each distinct payment service vary, so a sequence diagram will be specified for each adapter separately

**Core Service**

<div style="font-size: small;">

**External Account**\
Holder of information about user accounts. Can be used as a validator of user existence.

**External Transaction**\
Is a component interested in the successful completion of the transaction, and expects to receive an amount of money and user, who participated in the payment.

**Authorization**\
Responsible for issuing authorization tokens for communication with Core Service

**Administration Panel**\
Portal for system administrators to view and manage transactions. Required for debugging and solving issues.
</div>

### Sequence Diagrams

**Payment Service**

Create Transaction

![Core Payment Gateway-Create Transaction Sequence](./docs/Core%20Payment%20Gateway-Create%20Transaction%20Sequence.drawio.png)

Update Transaction

![Core Payment Gateway-Update Transaction Status Sequence](./docs/Core%20Payment%20Gateway-Update%20Transaction%20Status%20Sequence.drawio.png)

| **Transaction Status** | **Description**                                                            | **Allowed Transition** |
|------------------------|----------------------------------------------------------------------------|------------------------|
| INITIAL                | The transaction gets created with this status                              | <li>COMPLETED</li><li>FAILED</li> |
| FAILED                 | This indicates that transaction processing was failed during the lifecycle |                        |
| COMPLETED              | This indicates that the transaction was successfully passed the lifecycle  |                        |

Replenish transaction

![Core Payment Gateway-Replenish Transaction Sequence](./docs/Core%20Payment%20Gateway-Replenish%20Transaction%20Sequence.drawio.png)

Replenish is a background job, that launches with a fixed delay.\
On replenish failure next attempt will be calculated as an exponential function from attempt count.\
Retry attempts count is configurable.

Search transactions

![Core Payment Gateway-Transaction Search Sequence](./docs/Core%20Payment%20Gateway-Transaction%20Search%20Sequence.png)

Check account existence

![Core Payment Gateway-Account Exists Sequence](./docs/Core%20Payment%20Gateway-Account%20Exists%20Sequence.png)

### Data Diagram

![Core Payment Gateway-Data.drawio](./docs/Core%20Payment%20Gateway-Data.drawio.png)

### Deployment Diagram

![Deployment Diagram.drawio](./docs/Deployment%20Diagram.drawio.png)

### API Specification

**Check existing account**\
Indicates an account with provided id exists\
**URL**: https://{payment_host}/{account_id}\
**Method**: POST\
**Response**\
Success 200:
```json
{
  "firstName": "string",
  "lastName": "string",
  "middleName": "string"
}
```

**Search transaction**\
Retrieves transactions with provided external id\
**URL**: https://{payment_host}/transactions\
**Method**: GET\
**Parameters**
* external_id=123
* provider=test

**Response**\
Success 200:
```json
[
  {
    "id": 0,
    "externalId": "string",
    "status": "INITIAL",
    "provider": "string",
    "additionalData": "string"
  }
]
```

**Update transaction**\
Updates transaction according to allowed dataflow\
**URL**: https://{payment_host}/transactions\
**Method**: PUT\
**Body:**
```json
{
  "id": 0,
  "externalId": "string",
  "status": "INITIAL",
  "provider": "string",
  "additionalData": "string"
}
```
**Response**\
Success 200:
```json
{
  "id": 0,
  "externalId": "string",
  "status": "INITIAL",
  "provider": "string",
  "additionalData": "string"
}
```

**Create transaction**\
Creates transaction with the provided amount in Initial state\
**URL**: https://{payment_host}/transactions\
**Method**: POST\
**Body:**
```json
{
  "id": 0,
  "externalId": "string",
  "provider": "string",
  "status": "INITIAL",
  "amount": {
    "amount": 0,
    "currency": "string"
  },
  "commissionAmount": {
    "amount": 0,
    "currency": "string"
  },
  "user": "string",
  "timestamp": 0,
  "providerTimestamp": 0,
  "additionalData": "string"
}
```
**Response**\
Success 200:
```json
{
  "id": 0,
  "externalId": "string",
  "status": "INITIAL",
  "provider": "string",
  "additionalData": "string"
}
```

**Login**\
Authenticates user with provided credentials\
**URL**: https://{payment_host}/auth/login\
**Method**: POST\
**Body:**
```json
{
  "username": "string",
  "password": "string"
}
```
**Response**\
Success 200:
```json
{
  "username": "string",
  "accessToken": "string",
  "type": "string",
  "refreshToken": "string"
}
```

**Refresh token**\
Generates new access token\
**URL**: https://{payment_host}/auth/refresh\
**Method**: POST\
**Body:**
```json
{
  "refreshToken": "string"
}
```
**Response**\
Success 200:
```json
{
  "accessToken": "string",
  "type": "string",
  "refreshToken": "string"
}
```

**Logout**\
Deleting existing session\
**URL**: https://{payment_host}/auth/logout\
**Method**: POST\
**Body:**
```json
{
  "username": "string"
}
```
**Response**\
Success 200

**Search transaction**\
Search for transactions based on provided parameters\
**URL**: https://{payment_host}/admin/transactions\
**Method**: GET\
**Parameters**
* page=0
* pageSize=3
* sort=id
* order=DESC
* value=id

**Response**\
Success 200:
```json
[
  {
    "id": 0,
    "externalId": "string",
    "provider": "string",
    "status": "INITIAL",
    "amount": {
      "amount": 0,
      "currency": "string"
    },
    "commissionAmount": {
      "amount": 0,
      "currency": "string"
    },
    "user": "string",
    "timestamp": 0,
    "providerTimestamp": 0,
    "additionalData": "string"
  }
]
```

**Update transaction**\
Updates not system transaction fields\
**URL**: https://{payment_host}/admin/transactions\
**Method**: PUT\
**Body:**
```json
{
  "id": 0,
  "externalId": "string",
  "provider": "string",
  "status": "INITIAL",
  "amount": {
    "amount": 0,
    "currency": "string"
  },
  "commissionAmount": {
    "amount": 0,
    "currency": "string"
  },
  "user": "string",
  "timestamp": 0,
  "providerTimestamp": 0,
  "additionalData": "string"
}
```
**Response**\
Success 200:
```json
{
  "id": 0,
  "externalId": "string",
  "provider": "string",
  "status": "INITIAL",
  "amount": {
    "amount": 0,
    "currency": "string"
  },
  "commissionAmount": {
    "amount": 0,
    "currency": "string"
  },
  "user": "string",
  "timestamp": 0,
  "providerTimestamp": 0,
  "additionalData": "string"
}
```

**Complete transaction**\
Mark transaction as completed and initiate replenishment process\
**URL**: https://{payment_host}/admin/transactions\
**Method**: POST\
**Parameters**
* externalId=test
* provider=test

**Response**\
Success 200:
```json
{
  "id": 0,
  "externalId": "string",
  "provider": "string",
  "status": "INITIAL",
  "amount": {
    "amount": 0,
    "currency": "string"
  },
  "commissionAmount": {
    "amount": 0,
    "currency": "string"
  },
  "user": "string",
  "timestamp": 0,
  "providerTimestamp": 0,
  "additionalData": "string"
}
```
