# Rest-voting28
Voting System for deciding where to have lunch

## Task
Design and implement a REST API using Spring-Boot/Spring Data JPA.

Build a voting system for deciding where to have lunch.

2 types of users: admin and regular users
Admin can input a restaurant and it's lunch menu of the day (2-5 items usually, just a dish name and price)
Menu changes each day (admins do the updates)
Users can vote for a restaurant they want to have lunch at today
Only one vote counted per user
If user votes again the same day:
If it is before 11:00 we assume that he changed his mind.
If it is after 11:00 then it is too late, vote can't be changed
Each restaurant provides a new menu each day.

## REST API documentation
<a href="http://localhost:8080/doc">online documentation</a>

### Resources
* **/api/profile** - user profile
* **/api/admin/users** - users management
* **/api/restaurants** - restaurants info
* **/api/admin/restaurants** - restaurants management


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
