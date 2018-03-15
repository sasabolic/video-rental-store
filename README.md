# Video Rental Store

System for managing the rental administration.

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
    
## API Reference

API reference documentation is available at http://localhost:8080/swagger-ui.html.

### Example API Calls

Example API calls for the customer rental flow. The database is prepopulated with film and customer data. 
In the following examples the customer 'Nikola Tesla' with id '1' will be used:

* To search for customer with name: 'tesla': 

    Request:
    
        curl -X GET "http://localhost:8080/customers?name=tesla" -H "accept: */*"
    
    Response:
    
        [
           {
               "id": 1,
               "first_name": "Nikola",
               "last_name": "Tesla",
               "bonus_points": 0
           }
        ]

* To search for film with title: 'spider': 

    Request:
    
        curl -X GET "http://localhost:8080/films?title=spider" -H "accept: */*"

    Response:
    
        [
            {
                "id": 2,
                "title": "Spider Man",
                "type": "REGULAR_RELEASE",
                "quantity": 4
            },
            {
                "id": 3,
                "title": "Spider Man 2",
                "type": "REGULAR_RELEASE",
                "quantity": 3
            }
        ]
    
* To create new film rentals for customer with id '1' 

    Request:
    
        curl -X POST "http://localhost:8080/customers/1/rentals" -H "accept: */*" -H "Content-Type: application/json" -d "[ { \"film_id\": 1, \"days_rented\": 1 }, { \"film_id\": 2, \"days_rented\": 5 }, { \"film_id\": 3, \"days_rented\": 2 }, { \"film_id\": 4, \"days_rented\": 7 }]"

    Response:
    
        {
          "amount": "SEK 250.00",
          "rentals": [
            {
              "id": 1,
              "film_title": "Matrix 11",
              "days_rented": 1,
              "start_date": "2018-03-14T16:08:17.363728Z",
              "end_date": null
            },
            {
              "id": 2,
              "film_title": "Spider Man",
              "days_rented": 5,
              "start_date": "2018-03-14T16:08:17.409213Z",
              "end_date": null
            },
            {
              "id": 3,
              "film_title": "Spider Man 2",
              "days_rented": 2,
              "start_date": "2018-03-14T16:08:17.424916Z",
              "end_date": null
            },
            {
              "id": 4,
              "film_title": "Out of Africa",
              "days_rented": 7,
              "start_date": "2018-03-14T16:08:17.433951Z",
              "end_date": null
            }
          ]
        }

* To return rentals (after 3 days) for customer with id '1' 

    Request:
    
         curl -X PATCH "http://localhost:8080/customers/1/rentals" -H "accept: */*" -H "Content-Type: application/json" -d "[ { \"rental_id\": 1 }, { \"rental_id\": 2 }, { \"rental_id\": 3 }, { \"rental_id\": 4 }]"

    Response:
    
        {
          "amount": "SEK 110.00",
          "rentals": [
            {
              "id": 1,
              "film_title": "Matrix 11",
              "days_rented": 1,
              "start_date": "2018-03-14T16:08:17Z",
              "end_date": "2018-03-17T16:10:02.121004Z"
            },
            {
              "id": 2,
              "film_title": "Spider Man",
              "days_rented": 5,
              "start_date": "2018-03-14T16:08:17Z",
              "end_date": "2018-03-17T16:10:02.121045Z"
            },
            {
              "id": 3,
              "film_title": "Spider Man 2",
              "days_rented": 2,
              "start_date": "2018-03-14T16:08:17Z",
              "end_date": "2018-03-17T16:10:02.121055Z"
            },
            {
              "id": 4,
              "film_title": "Out of Africa",
              "days_rented": 7,
              "start_date": "2018-03-14T16:08:17Z",
              "end_date": "2018-03-17T16:10:02.121063Z"
            }
          ]
        }

* To get the customer with id '1' 

    Request:
    
        curl -X GET "http://localhost:8080/customers/1" -H "accept: */*"

    Response:
    
        {
          "id": 1,
          "first_name": "Nikola",
          "last_name": "Tesla",
          "bonus_points": 5
        }

    
* In case customer with id '4" does not exist

    Request:
    
        curl -X GET "http://localhost:8080/customers/4" -H "accept: */*"

    Response:
    
        {
          "timestamp": "2018-03-14T17:41:11.013941",
          "status": 404,
          "error": "Not Found",
          "exception": "com.example.videorentalstore.customer.CustomerNotFoundException",
          "message": "Customer with id '4' does not exist",
          "errors": null
        }
    

## Built With

* [Spring Boot](https://projects.spring.io/spring-boot/) - The web framework used
* [Gradle](https://gradle.org) - Build tool
* [Docker](https://docs.docker.com/install/) - Container packaging


