# signup-service
Small MVP of a service in Kotlin to provide users eligibility.

- contains a service in [Ktor framework](https://ktor.io/quickstart/index.html) provinding eligibility
- contains a consumer using [rabbitMq kotlin lib](https://github.com/rabbitmq/rabbitmq-tutorials/tree/master/kotlin)

### Depencences
This project depends on a **rabbitMq** service instance running in order to properly start up its consumer, 
if you are not going to need it, consider commenting the startup line.

I promise I will improve it some day allowing to run the API service independent of the consumer,
until there, this is the **"work around"**.


 
