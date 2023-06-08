# Recipes API

An API providing a CRUD and search interface for Recipes.

# Requirements

- [Java 17](https://jdk.java.net/17/),
- [Maven 3](https://maven.apache.org/download.cgi),
- [SonarQube](https://www.sonarsource.com/) (if you want to run code analysis)

## How to run maven-spotless for code formatting (in the terminal)

```./mvnw spotless:apply```

## How to build the project

```./mvnw package```

## How to run tests

```./mvnw test```

## How to run SonarQube analysis

```
./mvnw clean verify sonar:sonar \
-Dsonar.projectKey=Recipes \
-Dsonar.projectName='Recipes' \
-Dsonar.host.url=http://localhost:9000 \
-Dsonar.token={security_token}
```

## How to run the application with Java

```java -jar recipe-0.0.1-SNAPSHOT.jar```

## Run the application with maven wrapper

```./mvnw spring-boot:run```

## Docker

The application can also be built and run using [docker](https://www.docker.com/).
You'll need to install and run docker on localhost to proceed with the following docker commands.

### Building the application with docker

```docker build -t recipes-api .```

### Running the application with docker

```docker run -p 8080:8080 -t recipes-api```

### Swagger-UI:

Once the application is running, a [swagger interface](http://localhost:8080/swagger-ui/index.htm) will become available

## Technologies used

- Java 17,
- Maven 3,
- Spring Boot 3,
- Spring data,
- Lombok,
- H2 database,
- Swagger 3,
- Spotless,
- Jacoco,
- JUnit 5,
- Docker

## Acknowledgments

Carl Mapada, for the [medium article](https://medium.com/@cmmapada/advanced-search-and-filtering-using-spring-data-jpa-specification-and-criteria-api-b6e8f891f2bf) on advanced search and filtering with Spring data.

## Architectural choices

- Chose Spring boot for its rapid application development properties
- Used the very familiar service pattern to interact with data repositories storing the data
- Swagger integrates easily with Spring boot to create an easy-to-use interface to interact with the API
- Used Spring's Specification and Criteria API to perform dynamic queries, as documented by Carl Mapada
- Used modified version of chain of responsibility pattern to pick out search operation

## Areas for additional development not reached due to time

- Add grafana and prometheus for application monitoring and visualisation
- Add Eureka service discovery and an API gateway for external traffic
- Standalone database, instead of in-memory database
- Flyway database migration support

## Code coverage and results of static code analysis

- Jacoco reports that 100% of classes are covered, and 91% of lines
- SonarQube reports no major issues or code smells

