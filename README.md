# ticket-service

## How to run the application

1. First, run gradlew build command
`./gradlew build`

2. Run gradlew bootRun
`./gradlew bootRun`

3. The application will start and you have 2 options to test the 3 endpoints that are exposed in the application.
4. The first option is to test them through Swagger with the url http://localhost:8080/swagger-ui/index.html

<img width="1363" alt="image" src="https://github.com/user-attachments/assets/103eacd7-cb32-492d-81be-f19b0f0129ba">

5. The second option is to use a REST client like Postman and import the curls below.

### Test /availableSeatsCount

```
  curl -X 'GET' \
  'http://localhost:8080/availableSeatsCount' \
  -H 'accept: */*'
```
#### Response Body
``` 
5
```

The response is retrieved from the json file in the /resources/data directory, simulating data retrieved from DB.

<img width="713" alt="image" src="https://github.com/user-attachments/assets/472b95a4-9143-4ed7-b665-f2a35abfb130">


### Test /findAndHoldSeats

#### Without venue level selected

```
curl -X 'POST' \
  'http://localhost:8080/findAndHoldSeats' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "numSeats": 1,
  "customerEmail": "fso2309@gmail.com",
  "secondsToExpire": 10
}'
```

#### With venue level selected

```
curl -X 'POST' \
  'http://localhost:8080/findAndHoldSeats' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "numSeats": 1,
  "minLevel": 1,
  "maxLevel": 10,
  "customerEmail": "fernanda@gmail.com",
  "secondsToExpire": 10
}'
```
#### Reponse Body

```
{
  "seatHoldId": 0,
  "customerEmail": "fernanda@gmail.com",
  "selectedSeats": [
    {
      "seatId": 0,
      "venueLevel": 0,
      "seatStatus": "HELD",
      "price": 0
    }
  ],
  "holdStartDate": "2024-10-11T03:48:25.924Z",
  "holdEndDate": "2024-10-11T03:58:25.924Z"
}
```
<img width="772" alt="image" src="https://github.com/user-attachments/assets/6e11fd60-cfd1-4daa-86fa-15a2ea6fe3ad">


### Test /reserveSeats

```
curl -X 'POST' \
  'http://localhost:8080/reserveSeats?seatHoldId=0&customerEmail=fernanda%40gmail.com' \
  -H 'accept: */*' \
  -d ''
```

#### Response Body

```
WOUJN
```

A json file for the confirmation is created and the hold is deleted.
<img width="293" alt="image" src="https://github.com/user-attachments/assets/96a857ca-66a7-49ed-8db4-00bebef3cae7">

  6. Unit tests are included and run automatically when you execute the gradlew build command.
  7. The tests can be run manually using
     `./gradlew test`
  8. Jacoco reports for code coverage percentage were added to the test step, the generated reports are located in build folder under jacocoHtml folder

     <img width="300" alt="image" src="https://github.com/user-attachments/assets/5718c1bb-49ae-4f3c-9a46-d0c891ff9268">

     <img width="1107" alt="image" src="https://github.com/user-attachments/assets/f06f41ca-2807-4479-97a1-7aff10b940b0">

