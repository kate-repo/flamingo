# Overview

Game Service UI is a service that represents a visual interface that displays the Tic Tac Toe board and presents the progress of the game as it is played
automatically by the microservices.

### Project Structure

This project has the following structure:

```
src
├── main/java
│   └── [application package]
│       ├── base
│       │   └── ui
│       │       ├── MainLayout.java
│       │       └── ViewTitle.java
│       ├── game
│       │   ├── ui
│       │   │   └── BoardView.java
│       │   │   └── GameView.java
│       │   └── GameService.java                
│       └── Application.java     
├── main/resources
│   ├── META-INF
│   │   └── resources
│   │       ├── icons
│   │       │   └── plugin.svg
│   │       ├── styles.css
│   │       └── view-title.css
│   └── application.yaml 
└── test/java
    └── [application package]
        └── game
            ├── ui
            │   └── BoardViewTest.java
            │   └── GameViewTest.java
            └── GameServiceTest.java                 
```

The main entry point into the application is `Application.java`. This class contains the `main()` method that starts up
the Spring Boot application.

The project follows a *feature-based package structure*, organizing code by *functional units* rather than traditional
architectural layers. It includes two feature packages: `base` and `game`.

* The `base` package contains classes meant for reuse across different features, either through composition or
  inheritance. You can use them as-is, tweak them to your needs, or remove them.
* The `game` package is a feature package that represents a *self-contained unit of functionality*, including UI components, business logic, data access, and an
  integration test.

### How to start

The project is built by Maven so follow these steps:

* Navigate to the project root directory in your command line window
* To build run `mvn clean install`
* To start run `java -jar target/game-service-ui-1.0-SNAPSHOT.jar` 

