# About
This is a simple app that takes in weather station values via an API and allows you to query the values using a API request

# Tech used
- Postgres is used for the DB
- Docker compose is used to spin up the DB instance
- Liquibase is used to create the initial schema and any other subsequest tables needed
- Quarkus is the server framework used
- Java is the language
- Maven is the dependancy handler

# How to start
Spin up the docker container for postgres with the command `docker-compose up` ran on the classpath.
Start the app with `mvn quarkus:dev` command also ran in the classpath.

GET and POST queries can be made to `http://localhost/8080/api/v1/weather` endpoint

There is a Postman collection saved in the classpath, **WeatherData.postman_collection.json**. Import that into Postman and you can use the POST/GET calls I have been using to test this app.

Sample data is loaded to the DB from the `import.sql` file in the resources dir. More sample data can be simply added to this file and just a restart of the app is required for it to be persisted to the DB.

## Query Params
**sensor** - this is the name of the sensor, can contain multiple sensors.

**metric** - This can be one or all of _temperature_ / _humidity_ / _windSpeed_

**statistic** - This can be one of _average_ / _sum_ / _min_ / _max_

**dateStart** - This is a date on when to start the query, it is in the format **YYYY-MM-DD**

**dateEnd** - This is a date when to end the query, it is in the format **YYYY-MM-DD**

NOTE - the end date is before the start date, e.g. start date can be today and end date can be one week ago

## Assumptions
- A month lasts 30 days
- Default statistic is average
- Default dateStart is current day
- Default dateEnd is 30 days previous to current day

## Problems
- Sensor should be it's own entity. It's a String in WeatherData object. This should be an entity and have a many to one relationship. I got this working but the problem I faced was when I tried to post a weather data. Each time I tried to link the sensor, it came through as null and failed the validation check
- In the query I use agg functions, which means I need to include the columns in the select statement. The query result expects a WeatherData object in response and because I am only selecting the metric values and not the whole object, the response is of type object. I feel I had to do a hack in order to get this part working and parse the data into a decent response.


## Nuances
- Not returning values to 2 decimal places
- The metric/metrics needs to be specified in the API call as the way the solution for building the SQL query includes the aggregate function in the call
- The statistics is aggregate functions in SQL. If there was other statistics required like medium, mode, standard deviation etc. then the solution would need to be updated to get the statistics in the code and not in the SQL query
- More in depth tests
- More logging