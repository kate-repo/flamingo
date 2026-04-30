# Overview

Game Engine Service is a service that implements core Tic Tac Toe logic and manages the game state.

The project has two modules:

* [Game Engine Service](game-engine-service-server) that is a Spring Boot app containing all service logic
* [Game Engine Service Api Client](game-engine-service-api-client) that provides Spring Cloud OpenFeign declarative API client for the service

### Database

The project uses an H2 in-memory database for game state management.
Database schema and tables are created automatically on application start based on JPA mapping.
To connect to database console use these details:

* URL http://localhost:8081/h2-console
* database `game-engine-service`
* user `sa`
* password `password`

### How to start

The project is built by Maven so follow these steps:

* Navigate to the project root directory in your command line window
* To build run `mvn clean install`
* To start run `java -jar game-engine-service-server/target/game-engine-service-server-1.0-SNAPSHOT.jar` 
~~~~


