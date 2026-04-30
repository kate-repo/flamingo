# Overview

The Tic Tac Toe project includes following services:

* [Game Engine Service](/game-engine-service)
* [Game Session Service](/game-session-service)
* [Game Service UI](/game-service-ui)
* [Eureka Registry Service](/eureka-registry-service)
* [Game Service Gateway](/game-service-gateway)

The project uses

* Java 21
* Maven
* Spring Boot
* Spring Cloud OpenFeign
* Spring Cloud Gateway
* H2 in-memory database
* Eureka
* Vaadin

### How to start

To start Tic Tac Toe project sequentially build all services with maven `mvn clean install` and start built applications
`java -jar <service root dir>/target/<service name>.jar`.
You can find more details in README.md files of every service.

### How to start integration tests

To start integration tests you can use designated maven goal `integration-test`.
Integration tests are also run during `verify` and `install` phases.

### UI access

To access project User Interface go to [http://localhost:8080/game-service-ui/](http://localhost:8080/game-service-ui/).

To access Eureka dashboard to to [http://localhost:8761/](http://localhost:8761/)

Enjoy!
