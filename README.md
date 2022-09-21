# Coworking space 2.9
Final project for the module 223

## About the project
This project represents a prototype of the REST API for the Coworking space 2.0 written in spring boot. This system allows to book you a place at the coworking space while it's open. For the admin there're special CRUD operations to keep his coworking space up to date.

## Project setup

Clone this repo into a directory on your system. Make sure the sql file with the schema and the docker-compose.yml are in the same directory. After that make sure you have nothing running on ```port 8080```. Ensure you have Intellij, then open the Springboot project on Intellij

## How to start project

to start the project please navigate to the directory including the docker-compose.yml file. To start the containers for phpmyadmin and mariadb run ```docker compose up```. With the containers in place start the spring application.

To staart the Spring application open it up in intellij, make sure all your dependencies are in place by running ```mvn clean package```. This ensures all the needed dependencies are there. After that refresh the project. After you're done with this step, run by presing the start button on Intellij.

## Test data

if you clone this project, navigate to the docker directory. Here you will find the docker-compose.yml and coworking-space.sql. The docker file executes the sql file on start up. With that, all the data should be loaded for a test db. If you want to check this, logon on phpMyAdmin(localhost:8090).

## Endpoint documentation

Once you have the Springboot API running, you can navigate to ```localhost:8080/swagger-ui/index.html```
