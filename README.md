# Clean architecture example (template)

###About clean architecture:

1. [Robert C. Martin's Clean Architecture](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)
2. [Clean Architecture with Spring Boot](https://www.baeldung.com/spring-boot-clean-architecture)

### About the application:

1. Spring Boot
2. RestController
3. DataBase: JPA, H2, Liquibase
4. Unit testing

#### OpenAPI Documentation
OpenAPI documentation is exposed on the following endpoint: [http://localhost:8110/v3/api-docs.yaml](http://localhost:8110/v3/api-docs.yaml)

#### Build Info
Build info is exposed on the actuator endpoint.

Go to [http://localhost:8110/actuator](http://localhost:8110/actuator)

#### Healthcheck

Go to [http://localhost:8110/actuator/health](http://localhost:8110/actuator/health)
