# Overview

Game Service Service is a service that manage game sessions and automates the gameplay by generating moves for both players.

The project has two modules:

* [Game Session Service](game-session-service-server) that is a Spring Boot app containing all service logic
* [Game Session Service Api Client](game-session-service-api-client) that provides Spring Cloud OpenFeign declarative API client for the service

### Database

The project uses an H2 in-memory database for game state management.
Database schema and tables are created automatically on application start based on JPA mapping.
To connect to database console use these details:

* URL http://localhost:8082/h2-console
* database `game-session-service`
* user `sa`
* password `password`

### How to start

The project is built by Maven so follow these steps:

* Navigate to the project root directory in your command line window
* To build run `mvn clean install`
* To start run `java -jar game-session-service-server/target/game-session-service-server-1.0-SNAPSHOT.jar` 


