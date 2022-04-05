# Payment Service

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

## Overview

The purpose of an application is to accept payments from a wide amount of different payment providers and broadcast the result of successfully finished payment transactions.

The solution will constantly be appended with new payment provides. The microservices architecture was chosen to minimise efforts to add new payment providers, scale load for them individually and decouple system components.

## Design

### Component Diagram

[Core Payment Gateway-Components](https://wiki.itransition.com/display/RMNCHKEDU/Payment+Service#PaymentService-ComponentDiagram)

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

[Core Payment Gateway-Create Transaction Sequence](https://wiki.itransition.com/display/RMNCHKEDU/Payment+Service#PaymentService-CreateTransaction)

[Core Payment Gateway-Update Transaction Status Sequence](https://wiki.itransition.com/display/RMNCHKEDU/Payment+Service#PaymentService-UpdateTransactionStatus)

| **Transaction Status** | **Description**                                                            | **Allowed Transition** |
|------------------------|----------------------------------------------------------------------------|------------------------|
| INITIAL                | The transaction gets created with this status                              | <li>COMPLETED</li><li>FAILED</li> |
| FAILED                 | This indicates that transaction processing was failed during the lifecycle |                        |
| COMPLETED              | This indicates that the transaction was successfully passed the lifecycle  |                        |


[Core Payment Gateway-Replenish Transaction Sequence](https://wiki.itransition.com/display/RMNCHKEDU/Payment+Service#PaymentService-ReplenishTransaction)

Replenish is a background job, that launches with a fixed delay.\
On replenish failure next attempt will be calculated as an exponential function from attempt count.\
Retry attempts count is configurable.


[Core Payment Gateway-Transaction Search Sequence](https://wiki.itransition.com/display/RMNCHKEDU/Payment+Service#PaymentService-TransactionSearch)

[Core Payment Gateway-Account Exists Sequence](https://wiki.itransition.com/display/RMNCHKEDU/Payment+Service#PaymentService-AccountExists)

### Data Diagram

[Core Payment Gateway-Data.drawio](https://wiki.itransition.com/display/RMNCHKEDU/Payment+Service#PaymentService-DataDiagram)

### Deployment Diagram

[Deployment Diagram.drawio](https://wiki.itransition.com/display/RMNCHKEDU/Payment+Service#PaymentService-DeploymentDiagram)

### API Specification

[Payment Gateway](https://wiki.itransition.com/display/RMNCHKEDU/Payment+Service#PaymentService-PaymentGateway)


