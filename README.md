# ticket-service

How to run the application

1. First, run gradlew build command
`./gradlew build`

2. Run gradlew bootRun
`./gradlew bootRun`

3. The application will start and you have 2 options to test the 3 endpoints that are exposed in the application.
4. The first option is to test them through Swagger with the url http://localhost:8080/swagger-ui/index.html

<img width="1363" alt="image" src="https://github.com/user-attachments/assets/103eacd7-cb32-492d-81be-f19b0f0129ba">

5. The second option is to use a REST client like Postman and import the curls below.

Test /availableSeatsCount

`curl -X 'GET' \
  'http://localhost:8080/availableSeatsCount' \
  -H 'accept: */*'`


  Test /findAndHoldSeats

Without venue level selected

  `curl -X 'POST' \
  'http://localhost:8080/findAndHoldSeats' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "numSeats": 1,
  "customerEmail": "fso2309@gmail.com",
  "secondsToExpire": 10
}'`

With venue level selected

`curl -X 'POST' \
  'http://localhost:8080/findAndHoldSeats' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "numSeats": 1,
  "minLevel": 1,
  "maxLevel": 10,
  "customerEmail": "fernanda@gmail.com",
  "secondsToExpire": 10
}'`

Test /reserveSeats

`curl -X 'POST' \
  'http://localhost:8080/reserveSeats?seatHoldId=0&customerEmail=fernanda%40gmail.com' \
  -H 'accept: */*' \
  -d ''`

  6. Unit tests are included and run automatically when you execute the gradlew build command.
  7. The tests can be run manually using
     `./gradlew test`
  8. Jacoco reports for code coverage percentage were added to the test step, the generated reports are located in build folder under jacocoHtml folder

     <img width="300" alt="image" src="https://github.com/user-attachments/assets/5718c1bb-49ae-4f3c-9a46-d0c891ff9268">

     <img width="1107" alt="image" src="https://github.com/user-attachments/assets/f06f41ca-2807-4479-97a1-7aff10b940b0">

