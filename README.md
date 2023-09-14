# Store Purchase Application


## Overview

This appilication enables storing  and retrieving of store purchase transactions in to database.
Including the required country currency of the trnasaction will determine the exchange rate for the date of the transaction.

## Table of contents

* [Development]($development)
* [Guidelines]($guidelines)
* [Testing]($testing)



## Development 

This applications was built in VS Code using the following.

* Java Development Kit - Amazon Corretto 17

* Gradle 8.2.1

* Groovy 3.0.17

* H2 embedded database


## Guidelines

Run the application following the following steps

1. Clone this repository

2. Open in IDE such as Visual Studio Code

3. Build application using gradle
```
./gradlew build
```

4. This will also run the Groovy/Spock tests

```
> Task :test

CurrenciesTest > count total currencies is 169 PASSED

CurrenciesTest > USD is not included currencies PASSED

CurrenciesTest > #currency is included currencies list > Australia-Dollar is included currencies list PASSED

CurrenciesTest > #currency is included currencies list > United Kingdom-Pound is included currencies list PASSED

CurrenciesTest > #currency is included currencies list > Canada-Dollar is included currencies list PASSED

CurrenciesTest > #currency is included currencies list > Euro Zone-Euro is included currencies list PASSED

CurrenciesTest > #currency is included currencies list > New Zealand-Dollar is included currencies list PASSED

FiscalDataServiceTest > determines exchange rate PASSED

FiscalDataServiceTest > exchange rate can't be found from fiscal API PASSED

FiscalDataServiceTest > exchange rate can't be found as dates after tran date PASSED

FiscalDataServiceTest > returns actual date less than date param from find currency rate PASSED

FiscalDataServiceTest > returns null when passed null array to find currency rate  PASSED

FiscalDataServiceTest > returns null when cant find date in array to find currency rate PASSED

HelperTest > round amount #amount > round amount 1.1234 PASSED

HelperTest > round amount #amount > round amount 1.555 PASSED

HelperTest > round amount #amount > round amount 12345.233 PASSED

HelperTest > round amount #amount > round amount 9854.9567 PASSED

HelperTest > round amount #amount > round amount 1234 PASSED

HelperTest > round amount #amount > round amount 1 PASSED

HelperTest > encode a string > encode a string [value: Australia-Dollar, expected: Australia-Dollar, #0] PASSED

HelperTest > encode a string > encode a string [value: United Kingdom-Pound, expected: United+Kingdom-Pound, #1] PASSED

HelperTest > encode a string > encode a string [value: Canada-Dollar, expected: Canada-Dollar, #2] PASSED

HelperTest > encode a string > encode a string [value: Euro Zone-Euro, expected: Euro+Zone-Euro, #3] PASSED

HelperTest > encode a string > encode a string [value: New Zealand-Dollar, expected: New+Zealand-Dollar, #4] PASSED

HelperTest > decode a string > decode a string [value: Australia-Dollar, expected: Australia-Dollar, #0] PASSED

HelperTest > decode a string > decode a string [value: United+Kingdom-Pound, expected: United Kingdom-Pound, #1] PASSED

HelperTest > decode a string > decode a string [value: Canada-Dollar, expected: Canada-Dollar, #2] PASSED

HelperTest > decode a string > decode a string [value: Euro%20Zone-Euro, expected: Euro Zone-Euro, #3] PASSED

HelperTest > decode a string > decode a string [value: New+Zealand-Dollar, expected: New Zealand-Dollar, #4] PASSED

RequestValidationTest > all valid so no errors returned PASSED

RequestValidationTest > request supplied is empty return single error PASSED

RequestValidationTest > transaction date not supplied single error PASSED

RequestValidationTest > transaction date is not valid date PASSED

RequestValidationTest > transaction date is in the future PASSED

RequestValidationTest > description is longer than 50 characters PASSED

RequestValidationTest > USD amount is not provided PASSED

RequestValidationTest > USD amount is not a dollar amount PASSED

RequestValidationTest > USD amount has more than 2 decimals PASSED

RequestValidationTest > there are 2 validation errors PASSED

RequestValidationTest > there are 3 validation errors PASSED

RequestValidationTest > request is empty PASSED
```
5. Run the application. F5 within VS Code

6. Test using tool suh as Postman

## Testing
While running the application locally it will run by default on port 8080 using /transactions 
```
http://localhost:8080/transactions
```
### Create store purchase transaction

#### Happy path test
To create a store purchase transaction, POST a json like the following
```
POST
{
  "transactionDate": "2022-12-31",
  "description": "My test transaction.",
  "usdAmount": "1643.99"
}
```
Once submitted the whole transaction and the id are returned.
```
{
    "id": 1,
    "transactionDate": "2022-12-31",
    "description": "My test transaction.",
    "usdAmount": 1643.99
}
```
Note, the amount is now a decimal.

#### **Unhappy test**
The following rules apply when creating a store transaction.

* Null payload
```
{
    "type": "about:blank",
    "title": "Bad Request",
    "status": 400,
    "detail": "Failed to read request",
    "instance": "/transactions"
}
```
* Empty payload
As both **transactionDate** and **usdAmount* are required, both are reported missing.
```
{
    "timestamp": "14-09-2023 02:13:24",
    "status": "BAD_REQUEST",
    "message": "Input validation errors",
    "errors": [
        "Transaction date is required",
        "USD amount is not provided"
    ]
}
```
* Invalid transaction date
```
POST
{
  "transactionDate": "2022-12-32",
  "description": "My test transaction.",
  "usdAmount": "1643.99"
}
```
```
Response
{
    "timestamp": "14-09-2023 02:16:24",
    "status": "BAD_REQUEST",
    "message": "Input validation errors",
    "errors": [
        "Transaction date is not valid date"
    ]
}
```
* Missing transaction date
```
POST
{
  "description": "My test transaction.",
  "usdAmount": "1643.99"
}
```
```
Response
{
    "timestamp": "14-09-2023 02:17:37",
    "status": "BAD_REQUEST",
    "message": "Input validation errors",
    "errors": [
        "Transaction date is required"
    ]
}
```
* Description greater than 50 characters
```
POST
{
  "transactionDate": "2022-12-31",
  "description":"My test transaction post with 51 characters length.",
  "usdAmount": "1643.99"
}
```
```
Response
{
    "timestamp": "14-09-2023 02:19:11",
    "status": "BAD_REQUEST",
    "message": "Input validation errors",
    "errors": [
        "Description is greater than 50 characters"
    ]
}
```
* USD amount missing
```
POST
{
  "transactionDate": "2022-12-31",
  "description":"My test transaction."
}
```
```
Response
{
    "timestamp": "14-09-2023 02:20:10",
    "status": "BAD_REQUEST",
    "message": "Input validation errors",
    "errors": [
        "USD amount is not provided"
    ]
}
```
* USD amount with more than 2 decimals
```
POST
{
  "transactionDate": "2022-12-31",
  "description":"My test transaction.",
  "usdAmount": "1643.991"
}
```
```
{
    "timestamp": "14-09-2023 02:21:44",
    "status": "BAD_REQUEST",
    "message": "Input validation errors",
    "errors": [
        "USD Amount has more than 2 decimals"
    ]
}
```

### Retrieve store purchase transaction
To retrieve an transaction add the id and country currency in the query string.
```
GET
http://localhost:8080/transactions/1/United+Kingdom-Pound
```
```
Response
{
    "id": 1,
    "transactionDate": "2022-12-31",
    "description": "My test transaction.",
    "usdAmount": 1643.99,
    "currency": "United Kingdom-Pound",
    "amount": 1364.51,
    "exchangeRate": 0.83
}
```
If no currency is supplied, defaults to USD.
```
GET
http://localhost:8080/transactions/1
```
```
Response
{
    "id": 1,
    "transactionDate": "2022-12-31",
    "description": "My test transaction.",
    "usdAmount": 1643.99,
    "currency": "United States-Dollar",
    "amount": 1643.99,
    "exchangeRate": 1
}
```
Passing unknown ID retruns error.
```
GET
http://localhost:8080/transactions/99/Australia-Dollar
```
```
Response
{
    "timestamp": "14-09-2023 02:38:25",
    "status": "BAD_REQUEST",
    "message": "Transaction not found!",
    "errors": null
}
```
Give we create a transaction date in the past, the exchange rate my not be found for transaction date and country currency and returns an error.
```
POST
{
  "transactionDate": "1990-12-31",
  "description":"My test transaction.",
  "usdAmount": "1643.99"
}
```
```
Response
{
    "id": 10,
    "transactionDate": "1990-12-31",
    "description": "My test transaction.",
    "usdAmount": 1643.99
}
```
```
GET
http://localhost:8080/transactions/10/Australia-Dollar
```
```
{
    "timestamp": "14-09-2023 02:45:15",
    "status": "BAD_REQUEST",
    "message": "Rate not found for transaction date!",
    "errors": null
}
```
