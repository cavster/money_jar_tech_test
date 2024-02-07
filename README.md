# Demo Banking App


## Getting Started

Follow these instructions to set up and run the Demo banking app on your local machine.

### Prerequisites

- Java 8 or later
- Scala 2.12 or later
- sbt (Scala Build Tool) 1.4.0 or later

### Run the application

sbt run


### Run the tests

sbt test

### Using Docker

docker build -t demo-banking-app .
docker run -it demo-banking-app

1. Create a New Bank Account for a Customer

curl -X POST \
http://localhost:9000/createAccount \
-H 'Content-Type: application/json' \
-d '{
"customerId": {"id": 1},
"initialBalance": {"amountInPennies": 1000}
}'

2. Transfer Funds Between Two Customers
   curl -X POST \
   http://localhost:9000/transactions \
   -H 'Content-Type: application/json' \
   -d '{
   "fromAccountId": {"accountId": {"id": 1}},
   "toAccountId": {"accountId": {"id": 2}},
   "amountInPence": {"amountInPennies": 500}
   }'
3. Check Balance for an Account
   curl -X GET http://localhost:9000/balance/1
4.  Get All Transactions for an Account
curl -X GET http://localhost:9000/accounts/1/transactions

