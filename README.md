# money-transfer-service - JAVA VERSION

A RESTful API for money transfers between users accounts


#### Stack ####
* Java
* Vertx
* H2 in-memory Database
* Gradle
* JUnit/Vertx-unit


#### How to deploy ####
* Get a clone from the repo
* cd /project_location/project_folder
* ./gradlew shadowJar
* cd build/libs
* java -jar MoneyTransferService-1.0.0-all.jar - Application will up and run on port 8000


#### How to test ####
Application consists of 2 endpoints
    1. user endpoint to create, update, delete and get user accounts
    2. transaction endpoint to get transaction details an invoke and transaction between 2 accounts

First you have to add some user accounts to the system. You can use user-account endpoint for this

Add account 1
curl -X POST \
  http://localhost:8000/user \
  -H 'Content-Type: application/json' \
  -d '{
	"accountHolder": "Indunil",
	"balance":100
}'

Add account 2
curl -X POST \
  http://localhost:8000/user \
  -H 'Content-Type: application/json' \
  -d '{
	"accountHolder": "Asanka",
	"balance":200
}'

This will do a basic validation to check whether balance amount is positive (>=0), if not it will show a formatted error message
If request succeeded, it will return the account numbers(unique keys) of the newly created accounts


If you need to visit the user account details, you have to invoke the following command the with account number of the account

curl -X GET http://localhost:8000/user/10000000 

This also will do a basic validation to check whether specified account is existing, if not it will show a formatted error message

Other than these 2, you can do 
    update(to update an user account), 
    delete(to delete an user account) 
    and getAll(get all user accounts) operations with the user endpoint



Most important part, How to invoke a transaction
For this you have to use the transaction endpoint

curl -X POST \
  http://localhost:8000/transaction \
  -H 'Content-Type: application/json' \
  -d '{
	"sender":10000000,
	"receiver":10000001,
	"amount":10
}'

Here you have to provide the account numbers of the sender and the receiver and the transaction amount

At the beginning it will do some validations to check whether and give proper error messages if any validation fails
    1. Sender and Receiver accounts exists
    2. Sender's account balance is enough to do the transaction 
    3. and Transaction amount is positive (>=0)

If all the validations passed, it will do a transactional operation to update the user account of both the sender and receiver
and add transaction details to the transaction table. This will return the transactionId of the new transaction.


If you need to visit the transaction details, you have to invoke the following command the with transaction id of the transaction

curl -X GET http://localhost:8000/transaction/1


* Execute './gradlew test' command to execute unit tests 