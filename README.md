# Rest-voting28
Voting System for deciding where to have lunch

## Task
Design and implement a REST API using Spring-Boot/Spring Data JPA.

Build a voting system for deciding where to have lunch.

* 2 types of users: admin and regular users
* Admin can input a restaurant and it's lunch menu of the day (2-5 items usually, just a dish name and price)
* Menu changes each day (admins do the updates)
* Users can vote for a restaurant they want to have lunch at today
* Only one vote counted per user
* If user votes again the same day:
    - If it is before 11:00 we assume that he changed his mind.
    - If it is after 11:00 then it is too late, vote can't be changed

Each restaurant provides a new menu each day.

## Solution

### Technologies
* Java 17
* Spring Boot
* Spring security, Basic authentication
* Spring Data JPA
* Spring cache, Caffeine
* H2 in memory database
* OpenAPI
* Lombok
* JUnit 5, MockMVC

### Description
* Basic authentication with email and password
* 2 types of users: admin and regular users
* Admin has full access to users, restaurants, dishes and menu
* Regular user can read restaurants, dishes, menu and reports
* Regular users can vote on which restaurant they want to have lunch

## REST API documentation
<a href="http://localhost:8080/doc">online documentation</a> 
<br> *the application must be running on localhost*

### Resources
* **/doc** - REST API documentation
* **/api/profile** - user profile
* **/api/admin/users** - users management
* **/api/restaurants** - restaurants info
* **/api/admin/restaurants** - restaurants management
* **/api/admin/dishes** - restaurant dishes management
* **/api/menu** - menu info
* **/api/admin/menu** - menu management
* **/api/vote** - user voting
* **/api/admin/report/guests** - reports with guests details
* **/api/report/counts** - reports with guests counts

### Examples of requests using the curl

#### Get restaurants list
```
curl -X GET
  http://localhost:8080/api/restaurants
  -H 'Accept: application/json'
  -u user@gmail.com:password
```
#### Get restaurant by id
```
curl -X GET
  http://localhost:8080/api/restaurants/1
  -H 'Accept: application/json'
  -u user@gmail.com:password
```
#### Add menu
```
curl -X POST \
  http://localhost:8080/api/admin/menu
  -H 'Accept: application/json'
  -H 'Content-Type: application/json'
  -u admin@gmail.com:admin
  -d '{
  "restaurantId": 1,
  "date": "2023-02-05",
  "items": [
    {
      "dishId": 1,
      "price": 120
    },
    {
      "dishId": 2,
      "price": 140.5
    }
  ]
}' 
```
#### Get menu by date
```
curl -X GET
  http://localhost:8080/api/menu?date=2023-02-03
  -H 'Accept: application/json'
  -u user@gmail.com:password
```
#### Vote
```
curl -X POST
  http://localhost:8080/api/vote
  -H 'Accept: application/json'
  -u user@gmail.com:password
  -H 'Content-Type: application/json' \
  -d '{
  "restaurantId": 1
}'
```
#### Report: get guest list by restaurant id and date
```
curl -X GET
  http://localhost:8080/api/admin/report/guests?restaurantId=1&date=2023-02-04
  -H 'Accept: application/json'
  -u admin@gmail.com:admin
```
