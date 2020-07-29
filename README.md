# signup-service
Small MVP of a service in Kotlin to provide users eligibility.
- contains a service in [Ktor framework](https://ktor.io/quickstart/index.html) provinding eligibility
- contains a consumer using [rabbitMq kotlin lib](https://github.com/rabbitmq/rabbitmq-tutorials/tree/master/kotlin)
- contains a consumer using [kafka java lib](https://github.com/apache/kafka/tree/trunk/examples/src/main/java/kafka/examples)

This project is sub-divided in workers and API

### Depencencies

##### API

The **api** service depends on other services to communicate with, consider a specific mock created fo for it: [mock-signup-api-py](https://github.com/RafaelPereiraSantos/mock-signup-api-py)

##### Workers  

This **project** workers depends on:
 - A **rabbitMq** service instance running in order to properly start up its consumer
 - A **Kafka** service instance running in order to properly start up its consumer
 
 ###Note
 Make sure to have an **.env** files in the root of each sub-project with the values described inthe **.env-template**


 
