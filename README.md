# Video Rental Store

System for managing rental administration.

## Getting Started

These instructions will explain you how to get a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites

In order to build the project you will have to have [Java 1.8+](http://www.oracle.com/technetwork/java/javase/downloads/index.html), [Docker 17.12.0+](https://docs.docker.com/install/)
and [Docker Compose](https://docs.docker.com/compose/install/) installed on your machine.

You might have some of the required software already installed on you machine. To confirm it, please run the following commands
in your terminal window:

For Java:

    java --version

For Docker:

    docker --version

For Docker Compose:

    docker-compose --version 
    
### Installing and Running

Follow the steps below to quickly bring up the infrastructure. 

> It is assumed that you've opened your terminal window and navigated to the directory of this document.

#### Start all the services with docker-compose

To start all the services run the following command from your terminal window:

    ./run.sh

This script will execute tests and package the back-end services, build the Docker images and install them 
in your local Docker image registry. All of the containers will then be run using **docker-compose**.

> **Important:** 
>Make sure that you have the following ports available in your local machine: **8080** and **3306**. These are
>the ports used by **video-rental-store** and **MySQL**, respectively.

After you have run the `./run.sh` for the first time, all of the services will be containerized. Therefore, for every subsequent infrastructure bootstrap, it is sufficient to run **docker-compose**: 

    docker-compose up

## Running the tests

    ./gradlew clean test
    
## API reference

API reference documentation is available at http://localhost:8080/swagger-ui.html.
To login to the API with the Swagger follow these steps:

Add additional notes about how to deploy this on a live system

## Built With

* [Spring Boot](https://projects.spring.io/spring-boot/) - The web framework used
* [Gradle](https://gradle.org) - Build tool
* [Docker](https://docs.docker.com/install/) - Container packaging


